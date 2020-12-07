package io.konad

import io.konad.exceptions.ResultException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ResultTests: StringSpec({

    "If the result is OK, when i get the result, then it returns the value" {
        val x = Result.Ok(1)
        x.get() shouldBe 1
    }

    "If the result is Error, when i get the result, then it throws an Error" {
        val x = Result.Error("error")
        shouldThrow<ResultException> {
            x.get()
        }
    }

})

