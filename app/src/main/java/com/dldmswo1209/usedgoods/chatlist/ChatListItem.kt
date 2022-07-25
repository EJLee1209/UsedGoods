package com.dldmswo1209.usedgoods.chatlist

data class ChatListItem(
    val buyerId: String,
    val sellerId: String,
    val itemTitle: String,
    val key: Long
){
    constructor(): this("","","",0)
}