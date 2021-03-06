package io.konad.hkt


interface Kind<out F, out T>

interface FunctorKind<out F, out T>: Kind<F, T>{
    fun <R> mapK(fn: (T) -> R): FunctorKind<F, R>
}

interface ApplicativeFunctorKind<F, out T>: FunctorKind<F, T>{
    fun <R> apMapK(fn: (T) -> R): ApplicativeFunctorKind<F, R>
    fun <R> apK(liftedFn: FunctorKind<F, (T) -> R>): ApplicativeFunctorKind<F, R>
}

interface MonadKind<F, out T>: FunctorKind<F, T>{
    fun <R> flatMapK(fn: (T) -> MonadKind<F, R>): MonadKind<F, R>
}
