package com.hpcai270

private const val PUBLISHED_DAYS = 45

/**
 * Published study book: Days 1-45 only.
 *
 * There is deliberately NO page-count limit. A day's source material is
 * kept intact, and the deep-dive material is appended instead of compressing
 * the lesson into a fixed two-page spread. Difficult topics can occupy many
 * notebook pages. Depth matters more than symmetry.
 */
fun hpcBookDays(): List<Day> {
    val source = listOf(
        day1Expanded,
        *expandedDays2to5.toTypedArray(),
        *hpcDaysPart2.toTypedArray(),
        *hpcDaysPart3.toTypedArray(),
        *hpcDaysPart4.take(10).toTypedArray(),
        *hpcDaysPart5.toTypedArray()
    ).distinctBy { it.number }
        .sortedBy { it.number }
        .take(PUBLISHED_DAYS)

    return source.map { day ->
        val deepDive = depthNote(day.number)
        val pages = if (deepDive.isBlank()) day.pages else day.pages + deepDive

        Day(
            number = day.number,
            title = day.title,
            topic = day.topic,
            page1 = pages.firstOrNull() ?: "",
            page2 = pages.getOrElse(1) { "" },
            task = day.task,
            pages = pages
        )
    }
}
