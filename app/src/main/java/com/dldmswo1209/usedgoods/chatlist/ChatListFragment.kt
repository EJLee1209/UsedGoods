package com.dldmswo1209.usedgoods.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dldmswo1209.usedgoods.DBKey.Companion.CHILD_CHAT
import com.dldmswo1209.usedgoods.DBKey.Companion.DB_USERS
import com.dldmswo1209.usedgoods.R
import com.dldmswo1209.usedgoods.chatdetail.ChatRoomActivity
import com.dldmswo1209.usedgoods.databinding.FragmentChatlistBinding
import com.dldmswo1209.usedgoods.home.ArticleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {
    private var mBinding: FragmentChatlistBinding? = null
    private val binding get() = mBinding!!
    private lateinit var chatListAdapter: ChatListAdapter
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val chatRoomList = mutableListOf<ChatListItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentChatlistBinding.bind(view)

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            // 채팅방으로 이동하는 코드
            context?.let{
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key)
                startActivity(intent)
            }
        })
        chatRoomList.clear()

        binding.chatListRecyclerView.adapter = chatListAdapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)
        if(auth.currentUser == null){
            return
        }
        val chatDB = Firebase.database.reference.child(DB_USERS).child(auth.currentUser!!.uid).child(CHILD_CHAT)
        chatDB.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java) // ChatListItem 객체 형태로 채팅방 정보를 가져옴
                    model ?: return

                    chatRoomList.add(model) // 채팅방 리스트에 추가
                }
                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged() // 뷰 갱신
    }
}