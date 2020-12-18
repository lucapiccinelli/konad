package io.konad

import io.konad.coroutines.DeferredK
import io.konad.coroutines.deferredK
import io.konad.coroutines.pure
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DeferredKTests : StringSpec({

    "it should map deferred data" {

        val d: DeferredK<Int> = deferredK { 1 }

        d.map { it + 1 }.await() shouldBe 2
    }

    "it should ap function on deferred data" {

        val d: DeferredK<Int> = deferredK { 1 }

        d.ap(pure { it + 1 }).await() shouldBe 2
    }

})
