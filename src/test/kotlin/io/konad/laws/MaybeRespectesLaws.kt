package io.konad.laws

import io.konad.Maybe
import io.konad.Maybe.Companion.maybe
import io.konad.Result
import io.konad.error
import io.konad.ok
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class MaybeRespectesLaws: StringSpec({

    "Maybe respects the first functor law (identity)"{
        val id = { x: String -> x }
        checkAll<String> { v -> Maybe.pure(v).mapK(id) shouldBe Maybe.pure(v) }
    }

    "Maybe respects the second functor law (composition)"{
        val f = { x: Double -> x + 6 }
        val g = { y: Double -> y * y }
        checkAll<Double> { v -> Maybe.pure(v).mapK(f).mapK(g) shouldBe Maybe.pure(g(f(v))) }
    }

    "Maybe respects the first monad law (left identity)"{
        val f = { x: Double -> (x + 6).maybe }
        checkAll<Double> { v -> Maybe.pure(v).flatMapK(f) shouldBe f(v) }
    }

    "Maybe respects the second monad law (right identity)"{
        val f = { x: Double -> Maybe.pure(x) }
        checkAll<Double> { v -> Maybe.pure(v).flatMapK(f) shouldBe Maybe.pure(v) }
    }

    "Maybe respects the third monad law (associativity)"{
        val f = { x: Double -> (x + 6).maybe }
        val g = { y: Double -> (y * y).maybe }
        checkAll<Double> { v -> Maybe.pure(v).flatMapK(f).flatMapK(g) shouldBe Maybe.pure(v).flatMapK { x -> f(x).flatMapK(g) } }
    }

    "Maybe respects the third monad law (associativity) on null on f"{
        val f = { x: Double -> null.maybe }
        val g = { y: Double -> (y * y).maybe }
        checkAll<Double> { v -> Maybe.pure(v).flatMapK(f).flatMapK(g) shouldBe Maybe.pure(v).flatMapK { x -> f(x).flatMapK(g) } }
    }

    "Maybe respects the third monad law (associativity) on null on g"{
        val f = { x: Double -> (x + 6).maybe }
        val g = { y: Double -> null.maybe }
        checkAll<Double> { v -> Maybe.pure(v).flatMapK(f).flatMapK(g) shouldBe Maybe.pure(v).flatMapK { x -> f(x).flatMapK(g) } }
    }

    "Maybe respects the first applicative law (identity)"{
        val id = { x: Double -> x }
        checkAll<Double> { v -> Maybe.pure(v).apK(Maybe.pure(id)) shouldBe Maybe.pure(v) }
    }

    "Maybe respects the second applicative law (Homomorphism)"{
        val f = { x: Double -> x + 6 }
        checkAll<Double> { v -> Maybe.pure(v).apK(Maybe.pure(f)) shouldBe Maybe.pure(f(v)) }
    }

    "Maybe respects the third applicative law (Interchange)"{
        val f = { x: Double -> x + 6 }
        val u = Maybe.pure(f)

        checkAll<Double> { v -> Maybe.pure(v).apK(u) shouldBe u.apK(Maybe.pure { f2: (Double) -> Double -> f2(v) }) }
    }



    "Maybe respects the fourth applicative law (Composition)"{
        val compose = { f: (Double) -> String -> { g: (Int) -> Double -> { a: Int -> f(g(a)) } } }
        val g = { x: Double -> x.toString() }
        val f = { x: Int -> x + 6.0 }
        val u = Maybe.pure(g)
        val v = Maybe.pure(f)

        checkAll<Int> { w ->
            Maybe.pure(w).apK(v.apK(u.apK(Maybe.pure(compose)))) shouldBe Maybe.pure(w).apK(v).apK(u)
        }
    }


})
