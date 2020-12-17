package io.konad

import io.konad.Maybe.Companion.maybe
import io.konad.Maybe.Companion.nullable
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
        val value = Result.Ok(1)
        val error3 = Result.Errors("pear")

        val person: Result<Person> = ::Person.curry()
            .on(error1)
            .on(value)
            .on(error3)
            .result

        val ex = shouldThrow<ResultException> { person.get() }
        ex.errors.toList() shouldBe listOf(error1.error, error3.error)
    }

    "When i have a collection of results I want to flatten it as a result of a collection" {

        listOf(Result.Ok(1), Result.Ok(2))
            .flatten(Result.Companion::pure)
            .result shouldBe Result.Ok(listOf(1, 2))

    }

    "When i have a collection of results with more than one error I want to have the complete list of errors" {

        val ex = shouldThrow<ResultException> {
            listOf(Result.Errors("x"), Result.Ok(1), Result.Errors("y"))
                .flatten(Result.Companion::pure)
                .result
                .get()
        }

        ex.errors.description shouldBe "x - y"
    }

    "GIVEN a collection of nullables WHEN any is null THEN the flattening is null as well" {
        val listOfNullables: Collection<String?> = setOf("", null, "")

        val flattened: Collection<String>? = listOfNullables
            .map { it.maybe }
            .flatten(Maybe.Companion::pure)
            .nullable

        flattened shouldBe null
    }

    "GIVEN a collection of nullables WHEN no element is null THEN the flattening should success" {
        val listOfNullables: Set<String?> = setOf("a", "b", "c")

        val flattened: Collection<String>? = listOfNullables
            .map { it.maybe }
            .flatten(Maybe.Companion::pure)
            .nullable

        val expectedSet: Set<String>? = setOf("a", "b", "c")
        flattened shouldBe expectedSet
    }

})