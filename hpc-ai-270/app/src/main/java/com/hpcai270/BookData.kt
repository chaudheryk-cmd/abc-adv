package com.hpcai270

/** The published reader currently contains only the first 45 days. */
fun hpcBookDays(): List<Day> = listOf(
    day1Expanded,
    *hpcDaysPart1.drop(1).toTypedArray(),
    *hpcDaysPart2.toTypedArray(),
    *hpcDaysPart3.toTypedArray(),
    *hpcDaysPart4.take(10).toTypedArray(),
    *hpcDaysPart5.toTypedArray()
).take(45)
