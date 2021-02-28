package io.konad.applicative

import io.konad.applicative.builders.ap
import io.konad.applicative.builders.map
import io.konad.hkt.ApplicativeFunctorKind

inline fun <F, T> Collection<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (Collection<T>) -> ApplicativeFunctorKind<F, Collection<T>>): ApplicativeFunctorKind<F, Collection<T>> =
    { c: Collection<T> -> { t: T -> c + t } }.let { accumulate ->
        fold(pureLift(emptyList())) { acc, f -> accumulate map acc ap f }
    }

inline fun <F, T> Sequence<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (Sequence<T>) -> ApplicativeFunctorKind<F, Sequence<T>>): ApplicativeFunctorKind<F, Sequence<T>> =
    { c: Sequence<T> -> { t: T -> c + t } }.let { accumulate ->
        fold(pureLift(emptySequence())) { acc, f -> accumulate map acc ap f }
    }