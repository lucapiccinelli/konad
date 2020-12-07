package io.konad

import io.konad.exceptions.ResultException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ResultTests: StringSpec({

    "If the result is OK, when i get the result, then it returns the value" {
        val x: Result<Int> = Result.Ok(1)
        x.get() shouldBe 1
    }

    "If the result is Error, when i get the result, then it throws an Error" {
        val x: Result<Int> = Result.Errors("error")
        shouldThrow<ResultException> { x.get() }
    }

    "If the result is OK, when i get the result or default, then it returns the value" {
        val x: Result<Int> = Result.Ok(1)
        x.ifError(2) shouldBe 1
    }

    "If the result is Error, when i get the result or default, then it returns the default" {
        val x: Result<Int> = Result.Errors("bla")
        x.ifError(2) shouldBe 2
    }

    "Mapping an Ok result, when i get the result, i get the mapped result " {
        val x: Result<Int> = Result.Ok(1).map { x -> x + 1 }
        x.get() shouldBe 2
    }

    "Mapping an Error result, when i get the result, then it throws an Error" {
        val x: Result<Int> = Result.Errors("error").map { x: Int -> x + 1 }
        shouldThrow<ResultException> { x.get() }
    }

    "FlatMapping an Ok result, when i get the result, i get the mapped result " {
        val x: Result<Int> = Result.Ok(1).flatMap { x -> Result.Ok(x + 1) }
        x.get() shouldBe 2
    }

    "FlatMapping an Error result, when i get the result, then it throws an Error" {
        val x: Result<Int> = Result.Errors("error").flatMap { x: Int -> Result.Ok(x + 1) }
        shouldThrow<ResultException> { x.get() }
    }

    "FlatMapping an Ok result with a function that returns an error, when i get the result, then it throws an Error" {
        val x: Result<Int> = Result.Ok(1).flatMap { Result.Errors("banana") }
        val ex = shouldThrow<ResultException> { x.get() }
        ex.errors.error.description shouldBe "banana"
    }

    "FlatMapping an Error result with a function that returns an error, when i get the result, then it throws the first Error" {
        val x: Result<Int> = Result.Errors("error1").flatMap { Result.Errors("banana") }
        val ex = shouldThrow<ResultException> { x.get() }
        ex.errors.error.description shouldBe "error1"
    }

    "A lifted function can be applied" {
        val liftedFn = Result.pure { x: Int -> x + 1 }
        val y: Result<Int> = Result.pure(1).ap(liftedFn)

        y.get() shouldBe 2
    }

    "Applying a function in error status to an Error result, the errors gets accumulated" {
        val liftedFn: Result<(Int) -> Int> = Result.Errors("fn error")
        val y: Result<Int> = Result.Errors("value error").ap(liftedFn)

        y.ifError { errors -> errors.toList().joinToString(",") { it.description } } shouldBe "fn error,value error"
    }

    "null to Result: value should convert to Ok"{
        1.toResult("error") shouldBe Result.Ok(1)
    }

    "null to Result: value should convert to null"{
        null.toResult("error") shouldBe Result.Errors("error")
    }
})


