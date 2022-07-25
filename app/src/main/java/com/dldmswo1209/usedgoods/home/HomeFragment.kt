package com.dldmswo1209.usedgoods.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dldmswo1209.usedgoods.R
import com.dldmswo1209.usedgoods.databinding.FragmentHomeBinding
import com.dldmswo1209.usedgoods.mypage.DBKey.Companion.DB_ARTICLES
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {
    private var mBinding: FragmentHomeBinding? = null
    private val binding get() = mBinding!!
    private lateinit var articleAdapter: ArticleAdapter
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java) // 데이터베이스에서 가져오는데 ArticleModel 객체로 가져옴
            articleModel ?: return // null 처리

            articleList.add(articleModel) // 리스트에 추가
            articleAdapter.submitList(articleList)
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }
    private lateinit var articleDB: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentHomeBinding.bind(view)

        articleList.clear() // 게시물 리스트 초기화(중복 되서 생성되는 경우를 방지)
        articleDB = Firebase.database.reference.child(DB_ARTICLES) // Realtime DB reference 생성
        articleAdapter = ArticleAdapter() // 어답터 생성

        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = articleAdapter // 리사이클러뷰 어답터 연결

        articleDB.addChildEventListener(listener) // Realtime DB 에서 게시물 리스트를 가져옴

        binding.addFloatingButton.setOnClickListener { // 게시물 작성 버튼 클릭 이벤트
//            if(auth.currentUser == null){
//                Snackbar.make(view, "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
            val intent = Intent(requireContext(), AddArticleActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        articleAdapter.notifyDataSetChanged() // 게시물 업데이트
    }
    override fun onDestroy() {
        super.onDestroy()

        articleDB.removeEventListener(listener)
    }

}