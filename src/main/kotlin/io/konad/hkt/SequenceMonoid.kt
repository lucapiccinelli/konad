package io.konad.hkt

import io.konad.applicative.flatten


open class SequenceOf
val <T> Monoid<SequenceOf, T>.sequence
    get() = this as SequenceMonoid<T>

class SequenceMonoid<T>(val sequence: Sequence<T>): Foldable<SequenceOf, T>, Sequence<T> by sequence{
    companion object{
        fun <T> empty(): SequenceMonoid<T> = SequenceMonoid(emptySequence())
    }

    override operator fun plus(element: T): Monoid<SequenceOf, T> = SequenceMonoid(this.sequence + element)

    override fun <R> fold(initial: R, accumulator: (R, T) -> R): R = sequence.fold(initial, accumulator)

}

fun <T> Sequence<T>.monoid() = SequenceMonoid(this)

inline fun <F, T> Sequence<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (SequenceMonoid<T>) -> ApplicativeFunctorKind<F, SequenceMonoid<T>>): ApplicativeFunctorKind<F, Sequence<T>> =
    monoid()
        .flatten(pureLift(SequenceMonoid.empty()))
        .apMapK { it.sequence.sequence }