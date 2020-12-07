package io.konad

import io.konad.exceptions.ResultException
import io.konad.hkt.Kind

sealed class Result<out T>: Kind<ResultOf, T>{

    data class Ok<T>(val value: T): Result<T>()
    data class Error(val description: String): Result<Nothing>()

    companion object{
        fun <T> pure(value: T) = Ok(value)
    }

    inline fun <R> map(fn: (T) -> R): Result<R> = flatMap { Ok(fn(it)) }

    inline fun <R> flatMap(fn: (T) -> Result<R>): Result<R> = when(this){
        is Ok -> fn(this.value)
        is Error -> this
    }

    fun <R> ap(liftedFn: Result<(T) -> R>): Result<R> = liftedFn.flatMap { map(it) }

    fun get(): T = when(this){
        is Ok -> value
        is Error -> throw ResultException(this)
    }
}

fun <T> Result<T>.ifError(defaultValue: T) = ifError { defaultValue }

fun <T> Result<T>.ifError(errorHandler: (Result.Error) -> T) = when(this){
    is Result.Ok -> value
    is Result.Error -> errorHandler(this)
}

open class ResultOf
val <T> Kind<ResultOf, T>.result
    get() = this as Result<T>