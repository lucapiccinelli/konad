package io.konad

data class Error(val description: String, val title: String? = null, val code:Int = 0)