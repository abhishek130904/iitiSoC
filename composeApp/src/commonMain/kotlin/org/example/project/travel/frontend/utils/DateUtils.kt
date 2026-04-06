package org.example.project.travel.frontEnd.utils

import kotlinx.datetime.LocalDate

fun formatDate(date: LocalDate): String {
    val dayOfWeek = date.dayOfWeek.name.take(3)
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val month = date.month.name.take(3)
    val year = date.year.toString().takeLast(2)
    return "$dayOfWeek, $day $month $year"
}

fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
} 