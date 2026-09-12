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
    // Expanded lessons can contain any number of lesson pages. The final page
    // is treated as the day's checkpoint/task by the reader.
    constructor(
        number: Int,
        title: String,
        topic: String,
        vararg lessonPages: String
    ) : this(
        number = number,
        title = title,
        topic = topic,
        page1 = lessonPages.getOrElse(0) { "" },
        page2 = lessonPages.getOrElse(1) { "" },
        task = lessonPages.lastOrNull() ?: "",
        pages = lessonPages.toList()
    )
}
