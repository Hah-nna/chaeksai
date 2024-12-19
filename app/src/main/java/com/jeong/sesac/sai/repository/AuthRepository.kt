package com.jeong.sesac.sai.repository

import com.jeong.sesac.sai.repository.interfaces.AuthInterface

/**
 * flow를 사용해 stateFlow로 만들어서 뷰모델로 보내기~
 * */
class AuthRepository : AuthInterface {
    override fun login() {
        TODO("파이어베이스로 로그인 구현 ㄱ")
    }

    override fun signUp() {
        TODO("로그인이 안 되면 새로 가입하게 함")
    }



}