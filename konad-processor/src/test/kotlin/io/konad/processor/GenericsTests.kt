package io.konad.processor

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class GenericsTests : FunSpec({
    context("alphabet generation"){
        withData(
            1 to listOf("A", "B", "C"),
            2 to listOf("A", "B", "C", "AA", "BB", "CC"),
            3 to listOf("A", "B", "C", "AA", "BB", "CC", "AAA", "BBB", "CCC"),
        ){ (depth, expectedAlphabet) ->
            GenericsHelper.generateAlphabet(listOf("A", "B", "C"), depth) shouldBe expectedAlphabet
        }
    }
})