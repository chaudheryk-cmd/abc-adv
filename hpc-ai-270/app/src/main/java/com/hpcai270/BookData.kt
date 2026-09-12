package com.hpcai270

private const val PUBLISHED_DAYS = 45

/**
 * Published study book: Days 1-45 only.
 * Days 2-5 use the expanded field notes instead of the old short drafts.
 * Every day is presented as exactly two physical notebook pages.
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

    return source.map(::twoPageDay)
}

private fun twoPageDay(day: Day): Day {
    if (day.pages.size <= 2) {
        val deeper = day.pages.getOrElse(1) { "" } + "\n\n" + depthNote(day.number)
        return Day(day.number, day.title, day.topic, day.page1, deeper, day.task, listOf(day.page1, deeper))
    }

    val split = (day.pages.size + 1) / 2
    val left = day.pages.take(split).joinToString("\n\n")
    val right = day.pages.drop(split).joinToString("\n\n") + "\n\n" + depthNote(day.number)

    return Day(
        number = day.number,
        title = day.title,
        topic = day.topic,
        page1 = left,
        page2 = right,
        task = day.task,
        pages = listOf(left, right)
    )
}
