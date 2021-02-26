package io.konad

import io.konad.exceptions.EitherException
import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind
import io.konad.hkt.Kind
import io.konad.hkt.MonadKind

sealed class Either<out L, out R>: ApplicativeFunctorKind<EitherOf, R>, MonadKind<EitherOf, R> {
    data class Left<out L>(val left: L): Either<L, Nothing>()
    data class Right<out R>(val right: R): Either<Nothing, R>()

    companion object{
        fun <R> pure(value: R) = Either.Right(value)
    }

    fun get(): R = when(this){
        is Left -> throw EitherException(left)
        is Right -> right
    }

    inline fun <X> map(fn: (R) -> X): Either<L, X> =
        flatMap { Right(fn(it)) }

    override fun <X> mapK(fn: (R) -> X): FunctorKind<EitherOf, X> = map(fn)
    override fun <X> apMapK(fn: (R) -> X): ApplicativeFunctorKind<EitherOf, X> = map(fn)
    override fun <X> flatMapK(fn: (R) -> MonadKind<EitherOf, X>): MonadKind<EitherOf, X> = flatMap { fn(it).either }
    override fun <X> apK(liftedFn: FunctorKind<EitherOf, (R) -> X>): ApplicativeFunctorKind<EitherOf, X> = ap(liftedFn.either)
}

open class EitherOf
val <R> Kind<EitherOf, R>.either
    get() = this as Either<*, R>

@Suppress("UNCHECKED_CAST")
fun <L, R> Kind<EitherOf, R>.either() =
    this as Either<L, R>

inline fun <L, R, X> Either<L, R>.flatMap(fn: (R) -> Either<L, X>): Either<L, X> = when(this){
    is Either.Left -> this
    is Either.Right -> fn(right)
}

fun <L, R, X> Either<L, R>.ap(fn: Either<L, (R) -> X>): Either<L, X> =
    fn.flatMap(::map)

fun <L, R> Either<L, R>.ifLeft(default: R): R = when(this){
    is Either.Left -> default
    is Either.Right -> right
}

fun <L, R> Either<L, R>.ifLeft(errorHandler: (L) -> R): R = when(this){
    is Either.Left -> errorHandler(left)
    is Either.Right -> right
}

fun <R> R.right(): Either.Right<R> = Either.Right(this)
fun <L> L.left(): Either.Left<L> = Either.Left(this)