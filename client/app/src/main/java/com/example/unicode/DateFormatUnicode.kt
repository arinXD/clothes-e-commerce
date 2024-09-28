package com.example.unicode

import java.text.SimpleDateFormat
import java.util.*

class DateFormatUnicode {
    companion object{
        fun formatDate(dateString: String): String {
            // Parse the date string and format it to "yyyy-MM-dd"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            return outputFormat.format(date)
        }
    }
}