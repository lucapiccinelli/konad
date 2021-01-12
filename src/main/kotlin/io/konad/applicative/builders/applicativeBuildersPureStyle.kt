package io.konad.applicative.builders

import io.konad.hkt.*

infix fun <F, T, R> ((T) -> R).map(f: FunctorKind<F, T>): FunctorKind<F, R> = on(f)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.ap(f: ApplicativeFunctorKind<F, T>): ApplicativeFunctorKind<F, R> = on(f)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.pure(t: T): FunctorKind<F, R> = on(t)
infix fun <T, R> ((T) -> R).apply(t: T): R = on(t)


inline fun <F, T> Collection<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (Collection<T>) -> ApplicativeFunctorKind<F, Collection<T>>): ApplicativeFunctorKind<F, Collection<T>> =
    monoid()
        .flatten(pureLift(emptyList()).apMapK { it.monoid() })
        .apMapK { it.collection.collection }

inline fun <F, T> Sequence<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (Sequence<T>) -> ApplicativeFunctorKind<F, Sequence<T>>): ApplicativeFunctorKind<F, Sequence<T>> =
    monoid()
        .flatten(pureLift(emptySequence()).apMapK { it.monoid() })
        .apMapK { it.sequence.sequence }


fun <F, C, T> Foldable<C, ApplicativeFunctorKind<F, T>>.flatten(initial: ApplicativeFunctorKind<F, Monoid<C, T>>): ApplicativeFunctorKind<F, Monoid<C, T>> =
    { monoid: Monoid<C, T> -> { t: T -> monoid.plus(t) } }.let { accumulate ->
        fold(initial){ acc, f ->
            accumulate map acc ap f
        }
    }