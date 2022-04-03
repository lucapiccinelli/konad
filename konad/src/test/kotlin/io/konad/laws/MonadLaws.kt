package io.konad.laws

import io.konad.generators.functionAToB
import io.konad.hkt.MonadKind
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.checkAll

abstract class MonadLaws<F, T, R>(
    private val pure: (T) -> MonadKind<F, T>,
    private val monadGen1: Arb<MonadKind<F, T>>,
    private val monadGen2: Arb<MonadKind<F, R>>,
    private val valueGen1: Arb<T>,
): StringSpec({

    "first monad law (left identity)" {
        Arb.bind(Arb.functionAToB<T, MonadKind<F, T>>(monadGen1), valueGen1){ f, v -> f to v}
        .checkAll { (f, v) ->
            pure(v).flatMapK(f) shouldBe f(v)
        }
    }

    "second monad law (right identity)"{
        checkAll(monadGen1){ m ->
            m.flatMapK { pure(it) } shouldBe m
        }
    }

    "third monad law (associativity)"{
        Arb.bind(
            Arb.functionAToB<T, MonadKind<F, T>>(monadGen1),
            Arb.functionAToB<T, MonadKind<F, R>>(monadGen2),
            valueGen1){ f, g, v -> Triple(f , g,  v) }
        .checkAll { (f, g, v) ->

            pure(v).flatMapK(f).flatMapK(g) shouldBe pure(v).flatMapK { x -> f(x).flatMapK(g) }

        }
    }

})