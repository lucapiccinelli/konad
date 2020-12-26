package io.konad

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.kotest.property.forAll

class LawsTests: StringSpec({

    "Result respects first functor law (identity)"{
        val id = { x: Any -> x }
        checkAll<String> { v -> Result.pure(v).map(id) shouldBe Result.pure(v) }
    }

    "Result respects first functor law (composition)"{
        val f = { x: Double -> x + 6 }
        val g = { y: Double -> y * y }
        checkAll<Double> { v -> Result.pure(v).map(f).map(g) shouldBe Result.pure(g(f(v))) }
    }

})