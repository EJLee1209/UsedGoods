package com.dldmswo1209.usedgoods.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.dldmswo1209.usedgoods.DBKey.Companion.DB_USERS
import com.dldmswo1209.usedgoods.DBKey.Companion.NAME
import com.dldmswo1209.usedgoods.R
import com.dldmswo1209.usedgoods.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyPageFragment: Fragment(R.layout.fragment_mypage) {
    private var mBinding : FragmentMypageBinding? = null
    private val binding get() = mBinding!!
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMypageBinding.bind(view)

        binding.signInOutButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(auth.currentUser == null){
                // 로그인
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) {  task ->
                        if(task.isSuccessful){
                            successSignIn()
                        }else{
                            Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                // 로그아웃
                auth.signOut()
                binding.emailEditText.text.clear()
                binding.emailEditText.isEnabled = true
                binding.passwordEditText.text.clear()
                binding.passwordEditText.isEnabled = true

                binding.signInOutButton.text = "로그인"
                binding.signInOutButton.isEnabled = false
                binding.signUpButton.isEnabled = false
            }
        }
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        sucessSignUp()
                    }else{
                        Toast.makeText(context, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

        }
        binding.emailEditText.addTextChangedListener {
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signInOutButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
        binding.passwordEditText.addTextChangedListener{
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signInOutButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null){
            binding.emailEditText.text.clear()
            binding.emailEditText.isEnabled = true
            binding.passwordEditText.text.clear()
            binding.passwordEditText.isEnabled = true
            binding.signInOutButton.text = "로그인"
            binding.signInOutButton.isEnabled = false
            binding.signUpButton.isEnabled = false
        }else{
            binding.emailEditText.setText(auth.currentUser?.email)
            binding.emailEditText.isEnabled = false
            binding.passwordEditText.setText("************")
            binding.passwordEditText.isEnabled = false
            binding.signInOutButton.text = "로그아웃"
            binding.signInOutButton.isEnabled = true
            binding.signUpButton.isEnabled = false
        }
    }
    private fun successSignIn(){
        if(auth.currentUser == null){
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        binding.emailEditText.isEnabled = false
        binding.passwordEditText.isEnabled = false
        binding.signUpButton.isEnabled = false
        binding.signInOutButton.text = "로그아웃"
    }

    private fun sucessSignUp(){
        // 회원가입 성공 -> 닉네임 설정을 위한 팝업을 띄워줌
        val editText = EditText(requireContext()) // EditText 생성
        AlertDialog.Builder(requireContext()) // AlertDialog 생성
            .setTitle(getString(R.string.write_name)) // title 설정
            .setView(editText) // View 설정
            .setPositiveButton("저장"){_,_ -> // 저장 버튼
                if(editText.text.isEmpty()){ // editText 가 비어있으면
                    sucessSignUp() // 다시 팝업을 띄움
                }else{
                    saveUserInfo(editText.text.toString())
                }

            }
            .setCancelable(false) // 뒤로가기 비활성화
            .show()
    }
    private fun saveUserInfo(name: String){
        val userId = auth.currentUser!!.uid
        // database의 Users -> userId(현재 로그인한 유저의 uid)
        val currentUserDB = Firebase.database.reference.child(DB_USERS).child(userId)
        // DB에 UserInfo 객체로 저장
        val user = UserInfo(userId, name)
        currentUserDB.setValue(user)
        Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
    }
}