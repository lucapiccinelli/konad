package io.konad.hkt

import io.konad.applicative.flatten

open class CollectionOf
val <T> Monoid<CollectionOf, T>.collection
    get() = this as CollectionMonoid<T>

class CollectionMonoid<T>(val collection: Collection<T>): Foldable<CollectionOf, T>, Collection<T> by collection{

    companion object{
        fun <T> empty(): CollectionMonoid<T> = CollectionMonoid(emptyList())
    }

    override operator fun plus(element: T): Monoid<CollectionOf, T> = CollectionMonoid(this.collection + element)

    override fun <R> fold(initial: R, accumulator: (R, T) -> R): R = collection.fold(initial, accumulator)

}

fun <T> Collection<T>.monoid() = CollectionMonoid(this)

inline fun <F, T> Collection<ApplicativeFunctorKind<F, T>>.flatten(pureLift: (CollectionMonoid<T>) -> ApplicativeFunctorKind<F, CollectionMonoid<T>>): ApplicativeFunctorKind<F, Collection<T>> =
    monoid()
        .flatten(pureLift(CollectionMonoid.empty()))
        .apMapK { it.collection.collection }