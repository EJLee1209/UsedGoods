package com.dldmswo1209.usedgoods.chatdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.disklrucache.DiskLruCache
import com.dldmswo1209.usedgoods.DBKey.Companion.DB_CHATS
import com.dldmswo1209.usedgoods.DBKey.Companion.DB_USERS
import com.dldmswo1209.usedgoods.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private val auth: FirebaseAuth by lazy{
        Firebase.auth
    }
    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var chatDB: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val chatKey = intent.getLongExtra("chatKey",-1) // ChatListFragment 에서 클릭된 채팅방의 키값을 가져옴

        chatDB = Firebase.database.reference.child(DB_CHATS).child("$chatKey")
        chatDB?.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)  // 채팅 정보를 가져옴
                chatItem ?: return
                chatList.add(chatItem) // 채팅리스트에 추가
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged() // 채팅 갱신
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.sendButton.setOnClickListener { // 채팅 전송 버튼 클릭 이벤트
            // 현재 유저의 이름을 가져오는 과정
            val userDB = Firebase.database.reference.child(DB_USERS).child(auth.currentUser!!.uid)
            userDB.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(com.dldmswo1209.usedgoods.mypage.UserInfo::class.java)
                    val chatItem = ChatItem(
                        senderId = auth.currentUser!!.uid,
                        senderName = user!!.name, // 이름을 가져와서 채팅정보를 생성
                        message = binding.messageEditText.text.toString()
                    )
                    chatDB?.push()?.setValue(chatItem) // db에 저장

                }
                override fun onCancelled(error: DatabaseError) {}

            })
        }


    }
}