package io.konad.applicative.builders

import io.konad.Maybe.Companion.maybe
import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind

infix fun <F, T, R> ((T) -> R).on(f: FunctorKind<F, T>): FunctorKind<F, R> = f.mapK(this)
infix fun <F, T, R> ((T) -> R).on(f: ApplicativeFunctorKind<F, T>): ApplicativeFunctorKind<F, R> = f.apMapK(this)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.on(f: ApplicativeFunctorKind<F, T>): ApplicativeFunctorKind<F, R> = f.apK(this)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.on(t: T): FunctorKind<F, R> = mapK { it(t) }
infix fun <T, R> ((T) -> R).on(t: T): R = this(t)

