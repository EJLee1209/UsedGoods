package com.dldmswo1209.usedgoods.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dldmswo1209.usedgoods.MainActivity
import com.dldmswo1209.usedgoods.R
import com.dldmswo1209.usedgoods.databinding.ActivityMainBinding
import com.dldmswo1209.usedgoods.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {
    private var mBinding: FragmentHomeBinding? = null
    private val binding get() = mBinding!!
    private lateinit var articleAdapter: ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentHomeBinding.bind(view)

        articleAdapter = ArticleAdapter()
        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
            add(ArticleModel("0","aaaa",1000000, "5000원",""))
            add(ArticleModel("0","bbbb",2000000, "1000원",""))
        })

        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = articleAdapter

    }

}