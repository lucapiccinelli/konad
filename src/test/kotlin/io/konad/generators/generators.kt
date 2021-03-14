package io.konad.generators

import io.konad.Maybe
import io.konad.Maybe.Companion.maybe
import io.konad.Result
import io.konad.error
import io.konad.ok
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*


fun <T> Arb.Companion.result(okGen: Arb<T>): Arb<Result<T>> =
    arbitrary { rs -> rs.random.nextDouble(1.0).run { if(this > 0.5)
        okGen.next(rs).ok()
        else Arb.string().next(rs).error() }
    }

fun <T> Arb.Companion.maybe(valueGen: Arb<T>): Arb<Maybe<T>> =
    valueGen.orNull().map { it.maybe }

fun <A, B> Arb.Companion.functionAToB(genB: Arb<B>): Arb<(A) -> B> = genB.map{ b -> { b } }
