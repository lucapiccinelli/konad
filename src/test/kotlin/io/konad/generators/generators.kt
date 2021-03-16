package io.konad.generators

import io.konad.*
import io.konad.Maybe.Companion.maybe
import io.kotest.property.Arb
import io.kotest.property.Gen
import io.kotest.property.arbitrary.*


fun <T> Arb.Companion.result(okGen: Arb<T>): Arb<Result<T>> =
    arbitrary { rs -> rs.random.nextDouble(1.0).run { if(this > 0.5)
        okGen.next(rs).ok()
        else Arb.string().next(rs).error()
    }}

fun <T> Arb.Companion.maybe(valueGen: Arb<T>): Arb<Maybe<T>> =
    valueGen.orNull().map { it.maybe }

fun <A, B> Arb.Companion.validation(failGen: Arb<A>, successGen: Arb<B>): Arb<Validation<A, B>> =
    arbitrary { rs -> rs.random.nextDouble(1.0).run { if(this > 0.5)
        successGen.next(rs).success()
        else failGen.next(rs).fail()
    }}

fun <A, B> Arb.Companion.functionAToB(genB: Arb<B>): Arb<(A) -> B> = genB.map{ b -> { b } }
