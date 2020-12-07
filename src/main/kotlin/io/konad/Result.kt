package io.konad

sealed class Result<out T>{

    data class Ok<T>(val value: T): Result<T>()
    data class Error(val description: String): Result<Nothing>()

    fun get(): T = when(this){
        is Ok -> value
        is Error -> throw io.konad.ResultException(this)
    }
}