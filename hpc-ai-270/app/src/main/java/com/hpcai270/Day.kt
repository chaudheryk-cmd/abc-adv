package com.hpcai270

data class Day(
    val number: Int,
    val title: String,
    val topic: String,
    val page1: String,
    val page2: String,
    val task: String,
    val pages: List<String> = listOf(page1, page2)
) {
    // Expanded lessons may provide seven page strings; the final page is the checkpoint/task.
    constructor(
        number: Int,
        title: String,
        topic: String,
        page1: String,
        page2: String,
        page3: String,
        page4: String,
        page5: String,
        page6: String,
        page7: String
    ) : this(
        number,
        title,
        topic,
        page1,
        page2,
        page7,
        listOf(page1, page2, page3, page4, page5, page6, page7)
    )
}
