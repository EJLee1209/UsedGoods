package com.dldmswo1209.usedgoods.mypage

data class UserInfo(
    val userId: String,
    val name: String
){
    constructor(): this("","")
}
