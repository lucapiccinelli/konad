package io.konad

data class Error(val description: String, val title: String? = null, val code:Int = 0){
    override fun toString(): String = title?.run { "$this: $description" } ?: description
}
fun Collection<Error>.string(separator: String = ",") = joinToString(separator) { it.toString() }