package io.konad.laws

import io.konad.Maybe
import io.konad.MaybeOf
import io.konad.generators.functionAToB
import io.konad.generators.maybe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.string

class MaybeFunctorLaws: FunctorLaws<MaybeOf, Double, Double>(Arb.maybe(Arb.double()), Arb.double())
class MaybeMonadLaws: MonadLaws<MaybeOf, Double, String>(
    Maybe.Companion::pure,
    Arb.maybe(Arb.double()),
    Arb.maybe(Arb.string()),
    Arb.double())
class MaybeApplicativeLaws: ApplicativeLaws<MaybeOf, Double>(
    Maybe.Companion::pure,
    Maybe.Companion::pure,
    Maybe.Companion::pure,
    Maybe.Companion::pure,
    Arb.maybe(Arb.double()),
    Arb.maybe(Arb.functionAToB(Arb.double())),
    Arb.double())