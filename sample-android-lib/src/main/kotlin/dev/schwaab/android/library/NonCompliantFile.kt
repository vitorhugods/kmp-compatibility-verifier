package dev.schwaab.android.library

import java.io.File
import java.time.Month

val File.isHappy: Boolean get() = true

fun java.time.LocalDate.isOnChristmasDay() {
    dayOfMonth == 25 && month == Month.DECEMBER
}