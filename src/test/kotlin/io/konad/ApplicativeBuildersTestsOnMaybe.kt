package io.konad

import io.konad.Maybe.Companion.toMaybe
import io.konad.Maybe.Companion.toNullable
import io.kotest.core.spec.style.StringSpec
import io.konad.applicative.builders.*
import io.konad.exceptions.ResultException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

class ApplicativeBuildersTestsOnMaybe : StringSpec({

    data class Person(val isCustomer: Boolean, val id: Int, val name: String)
    val expected = Person(false, 4, "Foo")

    "Build a Result of a Person from intermediate results. Example of Result composition" {
        val person: Person? = ::Person.curry()
            .on(false.toMaybe())
            .on(4.toMaybe())
            .on( "Foo")
            .toNullable()

        person shouldBe expected
    }

    "When building a Result of a Person from any null value, then person should be null" {
        val person: Person? = ::Person.curry()
            .on(false.toMaybe())
            .on(null.toMaybe())
            .on( "Foo")
            .toNullable()

        person shouldBe null
    }

})