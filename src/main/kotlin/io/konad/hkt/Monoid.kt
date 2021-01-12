package io.konad.hkt

interface Monoid<F, T>{

    fun empty(): Monoid<F, T>
    fun plus(element: T): Monoid<F, T>

}

interface Foldable<F, T>: Monoid<F, T>{

    fun <R> fold(initial: R, accumulator: (R, T) -> R) : R

}

open class CollectionOf
val <T> Monoid<CollectionOf, T>.collection
    get() = this as CollectionMonoid<T>

open class SequenceOf
val <T> Monoid<SequenceOf, T>.sequence
    get() = this as SequenceMonoid<T>


class CollectionMonoid<T>(val collection: Collection<T>): Foldable<CollectionOf, T>, Collection<T> by collection{

    override fun empty(): Monoid<CollectionOf, T> = CollectionMonoid(emptyList())

    override operator fun plus(element: T): Monoid<CollectionOf, T> = CollectionMonoid(this.collection + element)

    override fun <R> fold(initial: R, accumulator: (R, T) -> R): R = collection.fold(initial, accumulator)

}

class SequenceMonoid<T>(val sequence: Sequence<T>): Foldable<SequenceOf, T>, Sequence<T> by sequence{

    override fun empty(): Monoid<SequenceOf, T> = SequenceMonoid(emptySequence())

    override operator fun plus(element: T): Monoid<SequenceOf, T> = SequenceMonoid(this.sequence + element)

    override fun <R> fold(initial: R, accumulator: (R, T) -> R): R = sequence.fold(initial, accumulator)

}

fun <T> Collection<T>.monoid() = CollectionMonoid(this)
fun <T> Sequence<T>.monoid() = SequenceMonoid(this)