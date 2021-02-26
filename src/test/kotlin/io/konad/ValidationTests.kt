package io.konad

import io.konad.applicative.builders.on
import io.konad.exceptions.EitherException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EitherTests : StringSpec({

    "left case should throw"{
        shouldThrow<EitherException> { Validation.Fail("ciao").get() }
    }

    "right case should return the value"{
        Validation.Success("ciao").get() shouldBe "ciao"
    }

    "If the result is Right, when i get the result or default, then it returns the value"{
        Validation.Success(1).ifLeft(2) shouldBe 1
    }

    "If the result is OK, when i get the result or default, then it returns the default"{
        Validation.Fail("booom").ifLeft(2) shouldBe 2
    }

    "mapping the right case should execute the mapping function"{
        Validation.Success(1)
            .map { it.toString() }
            .get() shouldBe "1"
    }

    "flatMapping the right case should execute the mapping function"{
        Validation.Success(1)
            .flatMap { Validation.Success(it.toString()) }
            .ifLeft(2) shouldBe "1"
    }

    "flatMapping the left case should not the mapping function"{
        val right: Validation<String, Int> = Validation.Success(1)
        right
            .flatMap { Validation.Fail("booom") }
            .ifLeft(2) shouldBe 2
    }

    "flatMapping starting from a left case returns left"{
        val right: Validation<String, Int> = Validation.Fail("booom")
        right
            .flatMap { Validation.Success(1) }
            .ifLeft(2) shouldBe 2
    }

    "flatMapping starting from a left short-circuits on the first left"{
        val right: Validation<String, String> = Validation.Fail("booom 1")
        right
            .flatMap { Validation.Fail("booom 2") }
            .ifLeft { it } shouldBe "booom 1"
    }

    "mapping the left case, must not tranform the content"{
        Validation.Fail("booom").map { it } shouldBe Validation.Fail("booom")
    }

    "A lifted function can be applied" {
        val liftedFn = Validation.pure { x: Int -> x + 1 }
        val y: Validation<String, Int> = Validation.pure(1).ap(liftedFn)

        y.get() shouldBe 2
    }

    "either can be composed"{
        val f: (Int, String, Double) -> String = { _, _, _ -> "ciao"}
        val out: Validation<String, String> = f.curry()
            .on("error".fail())
            .on("".success())
            .on(0.0)
            .validation()

        out.ifLeft { it } shouldBe "error"
    }

})