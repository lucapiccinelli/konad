package io.konad.laws

//import io.konad.*
//import io.konad.Maybe.Companion.maybe
//import io.konad.hkt.FunctorKind
//import io.kotest.core.spec.style.StringSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.property.checkAll
//import io.konad.Result

//open class FunctorLaws<F>(private val pure: (Double) -> FunctorKind<F, Double>): StringSpec({
//
//    "first functor law (identity)"{
//        val id = { x: Double -> x }
//        checkAll<Double> { v -> pure(v).mapK(id) shouldBe pure(v) }
//    }
//
//    "second functor law (composition)"{
//        val f = { x: Double -> x + 6 }
//        val g = { y: Double -> y * y }
//        checkAll<Double> { v -> pure(v).mapK(f).mapK(g) shouldBe pure(g(f(v))) }
//    }
//
//})
//
//class MaybeFunctorLaws(): FunctorLaws<MaybeOf>(Maybe.Companion::pure)
//class ResultFunctorLaws(): FunctorLaws<ResultOf>(Result.Companion::pure)
