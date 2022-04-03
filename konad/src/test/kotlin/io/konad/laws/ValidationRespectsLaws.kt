package io.konad.laws

import io.konad.Validation
import io.konad.ValidationOf
import io.konad.generators.functionAToB
import io.konad.generators.validation
import io.konad.hkt.Kind
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string

class ValidationFunctorLaws: FunctorLaws<Kind<ValidationOf, Int>, Double, Double>(
    Arb.validation(Arb.int(), Arb.double()),
    Arb.double())
class ValidationMonadLaws: MonadLaws<Kind<ValidationOf, Int>, Double, String>(
    Validation.Companion::pure,
    Arb.validation(Arb.int(), Arb.double()),
    Arb.validation(Arb.int(), Arb.string()),
    Arb.double())
class ValidationApplicativeLaws: ApplicativeLaws<Kind<ValidationOf, Int>, Double>(
    Validation.Companion::pure,
    Validation.Companion::pure,
    Validation.Companion::pure,
    Validation.Companion::pure,
    Arb.validation(Arb.int(), Arb.double()),
    Arb.validation(Arb.int(), Arb.functionAToB(Arb.double())),
    Arb.double())