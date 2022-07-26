package com.dldmswo1209.usedgoods.home

data class ArticleModel (
    val sellerId: String,
    val sellerName: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
){
    constructor(): this("","","",0,"","")
}