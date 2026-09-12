package com.hpcai270

data class Day(
    val number:Int,
    val title:String,
    val topic:String,
    val page1:String,
    val page2:String,
    val task:String,
    val pages: List<String> = listOf(page1, page2)
)
