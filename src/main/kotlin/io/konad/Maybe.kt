package io.konad

import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind
import io.konad.hkt.Kind
import io.konad.hkt.MonadKind

typealias MaybeOf = Any?

class Maybe<out T> private constructor(private val value: T?):
    MonadKind<MaybeOf, T>,
    ApplicativeFunctorKind<MaybeOf, T> {

    companion object{
        private fun <T> Kind<MaybeOf, T>.downcast() = this as Maybe<T>

        fun <T> T?.toMaybe(): ApplicativeFunctorKind<MaybeOf, T> = Maybe(this)
        fun <T> Kind<MaybeOf, T>.toNullable(): T? = downcast().value
    }

    private inline fun <R> map(fn: (T) -> R): Maybe<R> = flatMap{ Maybe(fn(it)) }
    private inline fun <R> flatMap(fn: (T) -> Maybe<R>): Maybe<R> = when(value){
        null -> Maybe(null)
        else -> fn(value)
    }
    private fun <R> ap(liftedFn: Maybe<((T) -> R)>): Maybe<R> = liftedFn.flatMap(this::map)

    override fun <R> flatMapK(fn: (T) -> MonadKind<MaybeOf, R>): MonadKind<MaybeOf, R> = flatMap { fn(it).downcast() }
    override fun <R> mapK(fn: (T) -> R): FunctorKind<MaybeOf, R> = map(fn)
    override fun <R> apK(liftedFn: FunctorKind<MaybeOf, (T) -> R>): FunctorKind<MaybeOf, R> = ap(liftedFn.downcast())
}