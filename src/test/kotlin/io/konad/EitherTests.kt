package io.konad

import io.konad.applicative.builders.on
import io.konad.exceptions.EitherException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EitherTests : StringSpec({

    "left case should throw"{
        shouldThrow<EitherException> { Either.Left("ciao").get() }
    }

    "right case should return the value"{
        Either.Right("ciao").get() shouldBe "ciao"
    }

    "If the result is Right, when i get the result or default, then it returns the value"{
        Either.Right(1).ifLeft(2) shouldBe 1
    }

    "If the result is OK, when i get the result or default, then it returns the default"{
        Either.Left("booom").ifLeft(2) shouldBe 2
    }

    "mapping the right case should execute the mapping function"{
        Either.Right(1)
            .map { it.toString() }
            .get() shouldBe "1"
    }

    "flatMapping the right case should execute the mapping function"{
        Either.Right(1)
            .flatMap { Either.Right(it.toString()) }
            .ifLeft(2) shouldBe "1"
    }

    "flatMapping the left case should not the mapping function"{
        val right: Either<String, Int> = Either.Right(1)
        right
            .flatMap { Either.Left("booom") }
            .ifLeft(2) shouldBe 2
    }

    "flatMapping starting from a left case returns left"{
        val right: Either<String, Int> = Either.Left("booom")
        right
            .flatMap { Either.Right(1) }
            .ifLeft(2) shouldBe 2
    }

    "flatMapping starting from a left short-circuits on the first left"{
        val right: Either<String, String> = Either.Left("booom 1")
        right
            .flatMap { Either.Left("booom 2") }
            .ifLeft { it } shouldBe "booom 1"
    }

    "mapping the left case, must not tranform the content"{
        Either.Left("booom").map { it } shouldBe Either.Left("booom")
    }

    "A lifted function can be applied" {
        val liftedFn = Either.pure { x: Int -> x + 1 }
        val y: Either<String, Int> = Either.pure(1).ap(liftedFn)

        y.get() shouldBe 2
    }

    "either can be composed"{
        val f: (Int, String, Double) -> String = { _, _, _ -> "ciao"}
        val out: Either<String, String> = f.curry()
            .on("error".left())
            .on("".right())
            .on(0.0)
            .either()

        out.ifLeft { it } shouldBe "error"
    }

})