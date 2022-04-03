package io.konad

import io.kotest.core.spec.style.StringSpec
import io.konad.applicative.builders.*
import io.kotest.matchers.shouldBe

class FunctionCurryAndApplyTests : StringSpec({

    data class Person(val isCustomer: Boolean, val id: Int, val name: String)
    val expected = Person(false, 4, "Foo")

    "currying a person" {
        val person = ::Person.curry() on false on 4 on "Foo"
        person shouldBe expected
    }

})