package io.konad.hkt


interface Kind<F, out T>

interface FunctorKind<F, out T>: Kind<F, T>{
    fun <R> mapK(fn: (T) -> R): FunctorKind<F, R>
}

interface ApplicativeFunctorKind<F, out T>: FunctorKind<F, T>{
    fun <R> apK(liftedFn: FunctorKind<F, (T) -> R>): FunctorKind<F, R>
}

interface MonadKind<F, out T>: FunctorKind<F, T>{
    fun <R> flatMapK(fn: (T) -> MonadKind<F, R>): MonadKind<F, R>
}