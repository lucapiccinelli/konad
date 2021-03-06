package io.konad

import ApplicativeFunctorKind2
import Kind2
import MonadKind2
import io.konad.applicative.builders.on
import io.konad.applicative.flatten
import io.konad.exceptions.EitherException
import io.konad.hkt.*

open class ValidationOf
fun <A, B> Kind<Kind<ValidationOf, A>, B>.validation() =
    this as Validation<A, B>

sealed class Validation<A, out B>: MonadKind2<ValidationOf, A, B>, ApplicativeFunctorKind2<ValidationOf, A, B> {
    data class Success<A, out B>(val success: B): Validation<A, B>()
    data class Fail<A>(val fail: A, val prev: Fail<A>? = null): Validation<A, Nothing>(){

        fun failures(): Collection<A> = (prev
            ?.run { failures() }
            ?: emptyList()) +
            listOf(fail)

        fun <D> transform(fn: (A) -> D): Fail<D> =
            Fail(fail = fn(fail), prev = prev?.transform(fn))
    }

    companion object{
        fun <A, B> pure(value: B): Validation<A, B> = Success(value)
    }

    fun get(): B = when(this){
        is Fail -> throw EitherException(fail)
        is Success -> success
    }

    inline fun <C> map(fn: (B) -> C): Validation<A, C> =
        flatMap { Success(fn(it)) }

    fun <D> mapFail(fn: (A) -> D): Validation<D, B> = when(this){
        is Success -> Success(success)
        is Fail -> transform(fn)
    }

    fun <D> mapAllFailures(fn: (Collection<A>) -> D): Validation<D, B> = when(this){
        is Success -> Success(success)
        is Fail -> Fail(fn(failures()))
    }


    inline fun <C> flatMap(fn: (B) -> Validation<A, C>): Validation<A, C> = when(this){
        is Success -> fn(success)
        is Fail -> this
    }

    fun <C> ap(liftedFn: Validation<A, (B) -> C>): Validation<A, C> = when(liftedFn){
        is Success -> map(liftedFn.success)
        is Fail -> when(this){
            is Success -> liftedFn
            is Fail -> Fail(fail, prev?.let { Fail(it.fail, liftedFn) } ?: liftedFn)
        }
    }

    override fun <R> apMapK(fn: (B) -> R): ApplicativeFunctorKind<Kind<ValidationOf, A>, R> = map(fn)

    override fun <R> apK(liftedFn: FunctorKind<Kind<ValidationOf, A>, (B) -> R>): ApplicativeFunctorKind<Kind<ValidationOf, A>, R> = ap(liftedFn.validation())

    override fun <R> mapK(fn: (B) -> R): FunctorKind<Kind<ValidationOf, A>, R> = map(fn)

    override fun <R> flatMapK(fn: (B) -> MonadKind<Kind<ValidationOf, A>, R>): MonadKind<Kind<ValidationOf, A>, R> = flatMap { fn(it).validation() }
}

fun <A, B> Validation<A, B>.ifFail(default: B): B = when(this){
    is Validation.Fail -> default
    is Validation.Success -> success
}

fun <A, B> Validation<A, B>.ifFail(errorHandler: (Collection<A>) -> B): B = when(this){
    is Validation.Fail -> errorHandler(failures())
    is Validation.Success -> success
}

fun <A, B> Collection<Validation<A, B>>.flatten(): Validation<A, Collection<B>> =
    flatten(Validation.Companion::pure)
    .validation()

fun <A, B> Result<B>.ifErrors(errorTransform: (Result.Errors) -> A): Validation<A, B> = when(this){
    is Result.Ok -> value.success()
    is Result.Errors -> errorTransform(this).fail()
}

fun <A, B> B?.ifNullValidation(errorTransform: () -> A): Validation<A, B> = this
    ?.run { this.success() }
    ?: errorTransform().fail()

fun <T> Result<T>.toValidation(): Validation<Error, T> = when(this){
    is Result.Ok -> value.success()
    is Result.Errors -> toFail(this)
}

fun <A, B> B.success(): Validation.Success<A, B> = Validation.Success(this)
fun <A> A.fail(): Validation.Fail<A> = Validation.Fail(this)

private fun toFail(errors: Result.Errors): Validation.Fail<Error> =
    Validation.Fail(fail = errors.error, prev = errors.prev?.run(::toFail))
