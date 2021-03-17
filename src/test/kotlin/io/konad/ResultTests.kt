package io.konad

import io.konad.applicative.builders.on
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
        1.ifNull("error") shouldBe Result.Ok(1)
    }

    "null to Result: value should convert to null"{
        null.ifNull("error") shouldBe Result.Errors("error")
    }

    "Can use ok extension method to create a new Result.Ok" {
        1.ok() shouldBe Result.Ok(1)
    }

    "Can use error extension method to create a new Result.Errors" {
        "booom".error() shouldBe Result.Errors("booom")
    }

    "Result.Ok folds first" {
        1.ok().fold({ it + 1 }, { it.toList().size }) shouldBe 2
    }

    "Result.Errors folds as second" {
        "booom".error().fold({ "ok" }, { it.description }) shouldBe "booom"
    }

    "can use description() with a separator to obtain the error descriptions of multiple Result.Errors" {
        val errors = Result.Errors(Error("z"), Result.Errors(Error("y"), Result.Errors(Error("x"))))

        errors.description(errorDescriptionsSeparator = ",") shouldBe "x,y,z"
    }

    "can enrich the error description prefixing it with a title" {
        val errors: Result<Any> = Result.Errors(Error("y"), Result.Errors(Error("x")))
        val newError = errors.errorTitle("banana")

        (newError as Result.Errors).description(errorDescriptionsSeparator = ",") shouldBe "banana: x,banana: y"
    }
})
