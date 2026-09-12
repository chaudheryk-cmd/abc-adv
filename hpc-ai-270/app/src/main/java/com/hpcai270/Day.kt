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
    // Compatibility constructor for the expanded lessons: seven lesson pages,
    // with the final page also serving as the day's task/checkpoint.
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
        number, title, topic, page1, page2, page7,
        listOf(page1, page2, page3, page4, page5, page6, page7)
    )

    // Compatibility constructor for expanded Day 1 data that supplies an
    // explicit page list after the seven page strings.
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
    ) : this(number, title, topic, page1, page2, page7, pages)

    // Compatibility constructor for the earlier expanded Day 1 file.
    constructor(
        number: Int,
        title: String,
        topic: String,
        page1: String,
        page2: String,
        task: String,
        pages: List<String>
    ) : this(number, title, topic, page1, page2, task, pages)
}
