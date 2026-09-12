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
    // Expanded lessons may provide any number of lesson pages.
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

    // Compatibility constructor for expanded lessons that explicitly provide
    // the pages named argument after seven positional page strings.
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
        page7: String,
        pages: List<String>
    ) : this(
        number = number,
        title = title,
        topic = topic,
        page1 = page1,
        page2 = page2,
        task = pages.lastOrNull() ?: page7,
        pages = pages
    )
}
