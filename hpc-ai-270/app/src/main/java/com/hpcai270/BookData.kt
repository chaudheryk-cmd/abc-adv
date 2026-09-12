package com.hpcai270

private const val PUBLISHED_DAYS = 100

/**
 * Published study book: Days 1-100.
 *
 * There is deliberately NO page-count limit. Source material is kept intact
 * and the deep-dive material is appended instead of compressing lessons into
 * a fixed page count. Difficult topics can occupy many notebook pages.
 */
fun hpcBookDays(): List<Day> {
    val source = listOf(
        day1Expanded,
        *expandedDays2to5.toTypedArray(),
        *hpcDaysPart2.toTypedArray(),
        *hpcDaysPart3.toTypedArray(),
        *hpcDaysPart4.take(10).toTypedArray(),
        *hpcDaysPart5.toTypedArray(),
        *hpcDaysPart6.toTypedArray(),
        *hpcDaysPart7.toTypedArray(),
        *hpcDaysPart8.toTypedArray(),
        *hpcDaysPart9.toTypedArray(),
        *hpcDaysPart10.toTypedArray(),
        *hpcDaysPart11.toTypedArray()
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
