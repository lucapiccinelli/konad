package io.konad

import io.konad.Maybe.Companion.maybe
import io.konad.Maybe.Companion.nullable
import io.konad.applicative.builders.on
import io.konad.applicative.flatten
import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind
import io.konad.hkt.Kind
import io.konad.hkt.MonadKind

open class MaybeOf

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

        infix operator fun <T: Any, R: Any> ((T) -> R)?.plus(t: T?): R? = this?.on(t.maybe)?.nullable
        infix operator fun <T: Any, R: Any> ApplicativeFunctorKind<MaybeOf, ((T) -> R)>?.plus(t: T?): R? = this?.on(t.maybe)?.nullable
        infix operator fun <T: Any, R: Any> FunctorKind<MaybeOf, ((T) -> R)>?.plus(t: T?): R? = this?.on(t.maybe)?.nullable
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

infix operator fun <A: Any, B, RESULT> ((A, B) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, RESULT> ((A, B, C) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, RESULT> ((A, B, C, D) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, RESULT> ((A, B, C, D, E) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, RESULT> ((A, B, C, D, E, F) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, RESULT> ((A, B, C, D, E, F, G) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, RESULT> ((A, B, C, D, E, F, G, H) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, RESULT> ((A, B, C, D, E, F, G, H, I) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J,RESULT> ((A, B, C, D, E, F, G, H, I, J) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, RESULT> ((A, B, C, D, E, F, G, H, I, J, K) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) -> RESULT).plus(a: A?) = curry().on(a.maybe)
infix operator fun <A: Any, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W) -> RESULT).plus(a: A?) = curry().on(a.maybe)