package io.konad

import io.konad.Maybe.Companion.maybe
import io.konad.Maybe.Companion.nullable
import io.konad.applicative.builders.on
import io.konad.applicative.flatten
import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind
import io.konad.hkt.Kind
import io.konad.hkt.MonadKind

typealias MaybeOf = Any?

data class Maybe<out T> private constructor(private val value: T?):
    MonadKind<MaybeOf, T>,
    ApplicativeFunctorKind<MaybeOf, T> {

    companion object{
        fun <T> Kind<MaybeOf, T>.downcast() = this as Maybe<T>

        fun <T: Any> pure(value: T) = Maybe(value)
        val <T: Any> T?.maybe: Maybe<T>
            get() = Maybe(this)
        val <T: Any> Kind<MaybeOf, T>.nullable: T?
            get() = downcast().value

        infix fun <T: Any, R> ((T) -> R)?.on(t: T?): R? = this?.on(t.maybe)?.downcast()?.value
        infix fun <F, T: Any, R> FunctorKind<F, ((T) -> R)>?.on(t: T?): R? = this?.on(t.maybe)?.downcast()?.value
    }

    private inline fun <R> map(fn: (T) -> R): Maybe<R> = flatMap{ Maybe(fn(it)) }
    private inline fun <R> flatMap(fn: (T) -> Maybe<R>): Maybe<R> = when(value){
        null -> Maybe(null)
        else -> fn(value)
    }
    private fun <R> ap(liftedFn: Maybe<((T) -> R)>): Maybe<R> = liftedFn.flatMap(this::map)

    override fun <R> flatMapK(fn: (T) -> MonadKind<MaybeOf, R>): MonadKind<MaybeOf, R> = flatMap { fn(it).downcast() }
    override fun <R> mapK(fn: (T) -> R): FunctorKind<MaybeOf, R> = map(fn)
    override fun <R> apMapK(fn: (T) -> R): ApplicativeFunctorKind<MaybeOf, R> = map(fn)
    override fun <R> apK(liftedFn: FunctorKind<MaybeOf, (T) -> R>): ApplicativeFunctorKind<MaybeOf, R> = ap(liftedFn.downcast())
}

fun <T> Collection<T?>.flatten(): Collection<T>? = asSequence().map { it.maybe }
    .flatten(Maybe.Companion::pure)
    .nullable
    ?.toList()