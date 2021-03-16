package io.konad.laws

import io.konad.Result
import io.konad.ResultOf
import io.konad.generators.functionAToB
import io.konad.generators.result
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.string

class ResultFunctorLaws: FunctorLaws<ResultOf, Double, Double>(Arb.result(Arb.double()), Arb.double())
class ResultMonadLaws: MonadLaws<ResultOf, Double, String>(
    Result.Companion::pure,
    Arb.result(Arb.double()),
    Arb.result(Arb.string()),
    Arb.double())
class ResultApplicativeLaws: ApplicativeLaws<ResultOf, Double>(
    Result.Companion::pure,
    Result.Companion::pure,
    Result.Companion::pure,
    Result.Companion::pure,
    Arb.result(Arb.double()),
    Arb.result(Arb.functionAToB(Arb.double())),
    Arb.double())