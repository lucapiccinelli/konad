package io.konad.applicative.builders

import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind

infix fun <F, T, R> ((T) -> R).map(f: FunctorKind<F, T>): FunctorKind<F, R> = on(f)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.ap(f: ApplicativeFunctorKind<F, T>): ApplicativeFunctorKind<F, R> = on(f)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.pure(t: T): FunctorKind<F, R> = on(t)
infix fun <T, R> ((T) -> R).apply(t: T): R = on(t)


inline fun <F, T> Collection<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (Collection<T>) -> ApplicativeFunctorKind<F, Collection<T>>): ApplicativeFunctorKind<F, Collection<T>> =
    fold(pureLift(emptyList())) { acc, f ->
        { c: Collection<T> -> { t: T -> c + t } } map acc ap f
    }

inline fun <F, T> Sequence<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (Sequence<T>) -> ApplicativeFunctorKind<F, Sequence<T>>): ApplicativeFunctorKind<F, Sequence<T>> =
    fold(pureLift(emptySequence())) { acc, f ->
        { c: Sequence<T> -> { t: T -> c + t } } map acc ap f
    }