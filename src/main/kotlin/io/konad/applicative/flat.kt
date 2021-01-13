package io.konad.applicative

import io.konad.applicative.builders.ap
import io.konad.applicative.builders.map
import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.Foldable
import io.konad.hkt.Monoid

fun <F, C, T> Foldable<C, ApplicativeFunctorKind<F, T>>.flatten(initial: ApplicativeFunctorKind<F, Monoid<C, T>>): ApplicativeFunctorKind<F, Monoid<C, T>> =
    { monoid: Monoid<C, T> -> { t: T -> monoid.plus(t) } }.let { accumulate ->
        fold(initial){ acc, f ->
            accumulate map acc ap f
        }
    }
