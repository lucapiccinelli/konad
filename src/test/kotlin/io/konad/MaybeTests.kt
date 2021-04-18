package io.konad

import io.konad.Maybe.Companion.on
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MaybeTests : StringSpec({

    "with maybe I can compose nullables without any explicit wrapping" {

        val x: Int? = 1
        val y: Float? = 2.0f
        val z: String? = "3"

        ::testFn.curry() on x on y on z shouldBe 6
    }

    "with maybe I can compose nullables without any explicit wrapping also in case of null" {

        val x: Int? = 1
        val y: Float? = null
        val z: String? = "3"

        ::testFn.curry() on x on y on z shouldBe null
    }

})

fun testFn(a: Int, b: Float, c: String): Float = a + b + c.toInt()