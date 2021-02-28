package io.konad.applicative.builders

import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind

infix fun <F, T, R> ((T) -> R).map(f: FunctorKind<F, T>): FunctorKind<F, R> = on(f)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.ap(f: ApplicativeFunctorKind<F, T>): ApplicativeFunctorKind<F, R> = on(f)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.pure(t: T): FunctorKind<F, R> = on(t)
infix fun <T, R> ((T) -> R).apply(t: T): R = on(t)