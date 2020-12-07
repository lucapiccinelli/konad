package io.konad

import io.konad.exceptions.ResultException
import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind
import io.konad.hkt.Kind
import io.konad.hkt.MonadKind

sealed class Result<out T>: ApplicativeFunctorKind<ResultOf, T>, MonadKind<ResultOf, T>{

    data class Ok<T>(val value: T): Result<T>()
    data class Errors(val error: Error, val prev: Errors? = null): Result<Nothing>(){
        constructor(description: String) : this(Error(description))

        fun toList(): Collection<Error> = (prev
            ?.let { prev.toList() }
            ?: emptyList()) +
            listOf(error)
    }

    companion object{
        fun <T> pure(value: T) = Ok(value)
    }

    inline fun <R> map(fn: (T) -> R): Result<R> = flatMap { Ok(fn(it)) }

    inline fun <R> flatMap(fn: (T) -> Result<R>): Result<R> = when(this){
        is Ok -> fn(this.value)
        is Errors -> this
    }

    fun <R> ap(liftedFn: Result<(T) -> R>): Result<R> = when(liftedFn){
        is Ok -> map(liftedFn.value)
        is Errors -> when(this){
            is Ok -> liftedFn
            is Errors -> Errors(error, liftedFn)
        }
    }

    fun get(): T = when(this){
        is Ok -> value
        is Errors -> throw ResultException(this)
    }

    override fun <R> mapK(fn: (T) -> R): FunctorKind<ResultOf, R> = map(fn)
    override fun <R> flatMapK(fn: (T) -> MonadKind<ResultOf, R>): MonadKind<ResultOf, R> = flatMap { fn(it).result }
    override fun <R> apK(liftedFn: FunctorKind<ResultOf, (T) -> R>): FunctorKind<ResultOf, R> = ap(liftedFn.result)
}

fun <T> Result<T>.ifError(defaultValue: T) = ifError { defaultValue }

fun <T> Result<T>.ifError(errorHandler: (Result.Errors) -> T) = when(this){
    is Result.Ok -> value
    is Result.Errors -> errorHandler(this)
}

fun <T> T?.toResult(errorMessage: String) = this
    ?.run { Result.Ok(this) }
    ?: Result.Errors(errorMessage)

open class ResultOf
val <T> Kind<ResultOf, T>.result
    get() = this as Result<T>