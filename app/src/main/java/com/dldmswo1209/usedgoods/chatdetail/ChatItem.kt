package com.dldmswo1209.usedgoods.chatdetail

data class ChatItem (
    val senderId: String,
    val senderName: String,
    val message: String
    ){
    constructor(): this("","","")
}
