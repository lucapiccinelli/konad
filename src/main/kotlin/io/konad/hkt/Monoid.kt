package io.konad.hkt

interface Monoid<F, T>: Kind<F, T>{

    fun plus(element: T): Monoid<F, T>

}

interface Foldable<F, T>: Monoid<F, T>{

    fun <R> fold(initial: R, accumulator: (R, T) -> R) : R

}
