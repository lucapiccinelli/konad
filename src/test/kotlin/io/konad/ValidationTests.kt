package io.konad

import io.konad.applicative.builders.on
import io.konad.exceptions.EitherException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValidationTests : StringSpec({

    "fail case should throw"{
        shouldThrow<EitherException> { Validation.Fail("ciao").get() }
    }

    "success case should return the value"{
        Validation.Success("ciao").get() shouldBe "ciao"
    }

    "If the result is success, when i get the result or default, then it returns the value"{
        Validation.Success(1).ifFail(2) shouldBe 1
    }

    "If the result is OK, when i get the result or default, then it returns the default"{
        Validation.Fail("booom").ifFail(2) shouldBe 2
    }

    "mapping the success case should execute the mapping function"{
        Validation.Success(1)
            .map { it.toString() }
            .get() shouldBe "1"
    }

    "flatMapping the success case should execute the mapping function"{
        Validation.Success(1)
            .flatMap { Validation.Success(it.toString()) }
            .ifFail(2) shouldBe "1"
    }

    "flatMapping the fail case should not the mapping function"{
        val success: Validation<String, Int> = Validation.Success(1)
        success
            .flatMap { Validation.Fail("booom") }
            .ifFail(2) shouldBe 2
    }

    "flatMapping starting from a fail case returns fail"{
        val success: Validation<String, Int> = Validation.Fail("booom")
        success
            .flatMap { Validation.Success(1) }
            .ifFail(2) shouldBe 2
    }

    "flatMapping starting from a fail short-circuits on the first fail"{
        val success: Validation<String, String> = Validation.Fail("booom 1")
        success
            .flatMap { Validation.Fail("booom 2") }
            .ifFail { it.first() } shouldBe "booom 1"
    }

    "mapping the fail case, must not tranform the content"{
        Validation.Fail("booom").map { it } shouldBe Validation.Fail("booom")
    }

    "A lifted function can be applied" {
        val liftedFn = Validation.pure { x: Int -> x + 1 }
        val y: Validation<String, Int> = Validation.pure(1).ap(liftedFn)

        y.get() shouldBe 2
    }

    "validation fail gets accumulated"{
        val x: Validation<TestError, Int> = TestError.fail()
        val fnError: Validation<TestError, (Int) -> Int> = TestError.fail()
        val result = x.ap(fnError)

        result.ifFail { it.size } shouldBe 2
    }

    "validation can be composed"{
        val f: (Int, String, Double) -> String = { _, _, _ -> "ciao"}
        val out: Validation<String, String> = f.curry()
            .on("error".fail())
            .on("".success())
            .on(0.0)
            .validation()

        out.ifFail { it.first() } shouldBe "error"
    }

    "validation can be flattened"(){
        val l: List<Validation<TestError, Int>> = listOf(0.success(), TestError.fail(), 1.success())

        l.flatten() shouldBe TestError.fail()
    }

    "validation can map fail value"{
        1.fail().mapFail { it + 1 }.ifFail { it.first().toString() } shouldBe "2"
    }

    "validation can map all failed values"{
        1.fail().mapAllFailures { MappedError("mapped", it.toList()) }.ifFail { it.first() } shouldBe MappedError("mapped", listOf(1))
    }

    "mapping the failers of a success, return the success value"{
        1.success().mapAllFailures { MappedError("mapped", it.toList()) }.ifFail { it.first() } shouldBe 1
    }

    "mapping the fail of a success, return the success value"{
        1.success().mapFail { it }.ifFail { it } shouldBe 1
    }

})

internal object TestError
internal data class MappedError<out T>(val title: String, val errors: Collection<T>)