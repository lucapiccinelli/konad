package io.konad.processor

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class CurryGeneratorTests : FunSpec({
    context("can nest as many curries as input requires"){
        withData(
            1 to "fun <A, RESULT> ((A) -> RESULT).curry() = { a: A -> this(a) }",
            2 to "fun <A, B, RESULT> ((A, B) -> RESULT).curry() = { a: A -> { b: B -> this(a, b) } }",
            3 to "fun <A, B, C, RESULT> ((A, B, C) -> RESULT).curry() = { a: A -> { b: B -> { c: C -> this(a, b, c) } } }",
        ){ (depth, expectedCurryFn) ->
            Curry(depth).generate() shouldBe expectedCurryFn
        }
    }
})


