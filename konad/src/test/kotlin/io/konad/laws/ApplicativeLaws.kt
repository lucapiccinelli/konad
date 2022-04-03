package io.konad.laws

import io.konad.generators.functionAToB
import io.konad.hkt.ApplicativeFunctorKind
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.checkAll

abstract class ApplicativeLaws<F, T>(
    private val pureT: (T) -> ApplicativeFunctorKind<F, T>,
    private val pureF: ((T) -> T) -> ApplicativeFunctorKind<F, (T) -> T>,
    private val pureInterchange: (((T) -> T) -> T) -> ApplicativeFunctorKind<F, ((T) -> T) -> T>,
    private val pureComposition: (((T) -> T) -> ((T) -> T) -> (T) -> T) -> ApplicativeFunctorKind<F, ((T) -> T) -> ((T) -> T) -> (T) -> T>,
    private val applicativeGen1: Arb<ApplicativeFunctorKind<F, T>>,
    private val applicativeGenF: Arb<ApplicativeFunctorKind<F, (T) -> T>>,
    private val valueGen1: Arb<T>
): StringSpec({

    "first applicative law (identity)" {
        val id: (T) -> T = { x: T -> x }
        checkAll(applicativeGen1) { applicative -> applicative.apK(pureF(id)) shouldBe applicative }
    }

    "second applicative law (Homomorphism)" {
        Arb.bind(valueGen1, Arb.functionAToB<T, T>(valueGen1)){ v, f -> v to f }
        .checkAll { (v, f) ->
            pureT(v).apK(pureF(f)) shouldBe pureT(f(v))
        }
    }

    "third applicative law (Interchange)" {
        Arb.bind(valueGen1, applicativeGenF){ v, applicativeF -> v to applicativeF }
        .checkAll { (v, applicativeF) ->

            pureT(v).apK(applicativeF) shouldBe applicativeF.apK(pureInterchange { f2: (T) -> T -> f2(v) })
        }
    }

    "fourth applicative law (Composition)" {
        val compose = { f: (T) -> T -> { g: (T) -> T -> { a: T -> f(g(a)) } } }
        Arb.bind(
            applicativeGen1,
            Arb.functionAToB<T, T>(valueGen1),
            Arb.functionAToB<T, T>(valueGen1)
        ){ applicative, f, g -> Triple(applicative , f, g)}
        .checkAll { (applicative, f, g) ->
            val u = pureF(g)
            val v = pureF(f)

            applicative.apK(v.apK(u.apK(pureComposition(compose)))) shouldBe applicative.apK(v).apK(u)
        }
    }

})