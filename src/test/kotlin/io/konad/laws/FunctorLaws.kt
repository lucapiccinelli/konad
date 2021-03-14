package io.konad.laws

import io.konad.hkt.FunctorKind
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

import io.konad.*
import io.konad.generators.functionAToB
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.map

abstract class FunctorLaws<F, T, R>(
    private val functorGen: Arb<FunctorKind<F, T>>,
    private val tranformedValueGen: Arb<R>
    ): StringSpec({


    "first functor law (identity)"{
        val id: (T) -> T = { x: T -> x }
        checkAll(functorGen) { functor -> functor.mapK(id) shouldBe functor }
    }

    "second functor law (composition)"{
        Arb.bind(
            functorGen,
            Arb.functionAToB<T, R>(tranformedValueGen),
            Arb.functionAToB<R, R>(tranformedValueGen) ){ functor, f, g -> Triple(functor, f, g) }

        .checkAll { (functor, f, g) ->
            functor.mapK(f).mapK(g) shouldBe functor.mapK { v -> g(f(v)) }
        }

    }

})