package io.konad

import io.kotest.core.spec.style.StringSpec
import io.konad.applicative.builders.*
import io.konad.exceptions.ResultException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

class ApplicativeBuildersTests : StringSpec({

    data class Person(val isCustomer: Boolean, val id: Int, val name: String)
    val expected = Person(false, 4, "Foo")

    "Build a Result of a Person from intermediate results. Example of Result composition" {
        val person: Result<Person> = ::Person.curry()
            .on(Result.pure(false))
            .on(Result.pure(4))
            .on( "Foo")
            .result

        person.get() shouldBe expected
    }

    "When building a Result of a Person from intermediate error results, then the errors get accumulated" {
        val error1 = Result.Errors("banana")
        val error2 = Result.Errors("apple")
        val error3 = Result.Errors("pear")

        val person: Result<Person> = ::Person.curry()
            .on(error1)
            .on(error2)
            .on(error3)
            .result

        val ex = shouldThrow<ResultException> { person.get() }
        ex.errors.toList() shouldBe listOf(error1.error, error2.error, error3.error)
    }

    "When building a Result of a Person from intermediate error results and Ok result, then the errors get accumulated as expected" {
        val error1 = Result.Errors("banana")
        val error2 = Result.Ok(1)
        val error3 = Result.Errors("pear")

        val person: Result<Person> = ::Person.curry()
            .on(error1)
            .on(error2)
            .on(error3)
            .result

        val ex = shouldThrow<ResultException> { person.get() }
        ex.errors.toList() shouldBe listOf(error1.error, error3.error)
    }

})