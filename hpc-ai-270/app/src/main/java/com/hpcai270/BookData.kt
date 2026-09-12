package com.hpcai270

private const val PUBLISHED_DAYS = 45

/**
 * The book is a real two-page-per-day field manual.
 * Existing lesson material is preserved and flowed into two physical pages
 * so the reader behaves like a 2-page/day book rather than a 7-page/day slide deck.
 */
fun hpcBookDays(): List<Day> {
    val source = listOf(
        day1Expanded,
        *hpcDaysPart1.drop(1).toTypedArray(),
        *hpcDaysPart2.toTypedArray(),
        *hpcDaysPart3.toTypedArray(),
        *hpcDaysPart4.take(10).toTypedArray(),
        *hpcDaysPart5.toTypedArray()
    ).take(PUBLISHED_DAYS)

    return source.map { twoPageDay(it) }
}

private fun twoPageDay(day: Day): Day {
    if (day.pages.size <= 2) return day

    // Preserve all existing lesson material while presenting it as the
    // intended two physical book pages. The pages remain vertically scrollable
    // on a phone so no lesson text is silently discarded.
    val split = (day.pages.size + 1) / 2
    val left = day.pages.take(split).joinToString("\n\n")
    val right = day.pages.drop(split).joinToString("\n\n")

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
