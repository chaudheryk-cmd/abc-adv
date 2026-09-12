package com.hpcai270

private const val PUBLISHED_DAYS = 270

/**
 * Complete 270-day HPC + AI field manual.
 *
 * Lessons are variable-length. No artificial page limit is imposed: every day
 * can contain as many notebook pages as the topic requires, plus deep-dive
 * material for the earlier foundation days.
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
        *hpcDaysPart11.toTypedArray(),
        *hpcDaysPart12.toTypedArray(),
        *hpcDaysPart13.toTypedArray(),
        *hpcDaysPart14.toTypedArray(),
        *hpcDaysPart15.toTypedArray(),
        *hpcDaysPart16.toTypedArray(),
        *hpcDaysPart17.toTypedArray(),
        *hpcDaysPart18.toTypedArray(),
        *hpcDaysPart19.toTypedArray(),
        *hpcDaysPart20.toTypedArray(),
        *hpcDaysPart21.toTypedArray()
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
