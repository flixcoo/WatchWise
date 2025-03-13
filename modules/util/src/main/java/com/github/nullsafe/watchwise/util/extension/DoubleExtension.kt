package com.github.nullsafe.watchwise.util.extension

inline val Double.Companion.empty get() = 0.0

fun Double.fiveStarRating(): Double = this / 2 //TODO should to remove

fun Double.formatVoteAverage(digits: Int) = "%.${digits}f".format(this)