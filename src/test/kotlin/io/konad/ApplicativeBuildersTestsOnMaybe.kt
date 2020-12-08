package io.konad

import io.konad.Maybe.Companion.maybe
import io.konad.Maybe.Companion.nullable
import io.konad.applicative.builders.on
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ApplicativeBuildersTestsOnMaybe : StringSpec({

    data class Person(val isCustomer: Boolean, val id: Int, val name: String)
    val expected = Person(false, 4, "Foo")

    "Build a Result of a Person from intermediate results. Example of Result composition" {
        val person: Person? = ::Person.curry()
            .on(false.maybe)
            .on(4.maybe)
            .on( "Foo")
            .nullable

        person shouldBe expected
    }

    "When building a Result of a Person from any null value, then person should be null" {
        val person: Person? = ::Person.curry()
            .on(false.maybe)
            .on(null.maybe)
            .on( "Foo")
            .nullable

        person shouldBe null
    }

})