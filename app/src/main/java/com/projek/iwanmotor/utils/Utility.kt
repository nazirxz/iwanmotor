package com.projek.iwanmotor.utils

import android.widget.TextView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object Utility {
    fun TextView.dateNow() {
        this.text = getDate()
    }

    fun getDate(): String {
        val dateTime = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM y", Locale("id","ID"))
        return dateTime.format(formatter)
    }
}