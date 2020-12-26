package io.konad.laws

import io.konad.Result
import io.konad.error
import io.konad.ok
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class ResultRespectesLaws: StringSpec({

    "Result respects the first functor law (identity)"{
        val id = { x: String -> x }
        checkAll<String> { v -> Result.pure(v).map(id) shouldBe Result.pure(v) }
    }

    "Result respects the second functor law (composition)"{
        val f = { x: Double -> x + 6 }
        val g = { y: Double -> y * y }
        checkAll<Double> { v -> Result.pure(v).map(f).map(g) shouldBe Result.pure(g(f(v))) }
    }

    "Result respects the first monad law (left identity)"{
        val f = { x: Double -> (x + 6).ok() }
        checkAll<Double> { v -> Result.pure(v).flatMap(f) shouldBe f(v) }
    }

    "Result respects the second monad law (right identity)"{
        val f = { x: Double -> Result.pure(x) }
        checkAll<Double> { v -> Result.pure(v).flatMap(f) shouldBe Result.pure(v) }
    }

    "Result respects the third monad law (associativity)"{
        val f = { x: Double -> (x + 6).ok() }
        val g = { y: Double -> (y * y).ok() }
        checkAll<Double> { v -> Result.pure(v).flatMap(f).flatMap(g) shouldBe Result.pure(v).flatMap { x -> f(x).flatMap(g) } }
    }

    "Result respects the third monad law (associativity) on error case 1"{
        val f = { x: Double -> "error".error() }
        val g = { y: Double -> (y * y).ok() }
        checkAll<Double> { v -> Result.pure(v).flatMap(f).flatMap(g) shouldBe Result.pure(v).flatMap { x -> f(x).flatMap(g) } }
    }

    "Result respects the third monad law (associativity) on error case 2"{
        val f = { x: Double -> (x + 6).ok() }
        val g = { y: Double -> "error".error() }
        checkAll<Double> { v -> Result.pure(v).flatMap(f).flatMap(g) shouldBe Result.pure(v).flatMap { x -> f(x).flatMap(g) } }
    }

    "Result respects the first applicative law (identity)"{
        val id = { x: Double -> x }
        checkAll<Double> { v -> Result.pure(v).ap(Result.pure(id)) shouldBe Result.pure(v) }
    }

    "Result respects the second applicative law (Homomorphism)"{
        val f = { x: Double -> x + 6 }
        checkAll<Double> { v -> Result.pure(v).ap(Result.pure(f)) shouldBe Result.pure(f(v)) }
    }

    "Result respects the third applicative law (Interchange)"{
        val f = { x: Double -> x + 6 }
        val u = Result.pure(f)

        checkAll<Double> { v -> Result.pure(v).ap(u) shouldBe u.ap(Result.pure { f2: (Double) -> Double -> f2(v) }) }
    }



    "Result respects the fourth applicative law (Composition)"{
        val compose = { f: (Double) -> String -> { g: (Int) -> Double -> { a: Int -> f(g(a)) } } }
        val g = { x: Double -> x.toString() }
        val f = { x: Int -> x + 6.0 }
        val u = Result.pure(g)
        val v = Result.pure(f)

        checkAll<Int> { w ->
            Result.pure(w).ap(v.ap(u.ap(Result.pure(compose)))) shouldBe Result.pure(w).ap(v).ap(u)
        }
    }


})
