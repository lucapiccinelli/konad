package io.konad.applicative.builders

import io.konad.curry
import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind

infix fun <F, T, R> ((T) -> R).on(f: FunctorKind<F, T>): FunctorKind<F, R> = f.mapK(this)
infix fun <F, T, R> ((T) -> R).on(f: ApplicativeFunctorKind<F, T>): ApplicativeFunctorKind<F, R> = f.apMapK(this)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.on(f: ApplicativeFunctorKind<F, T>): ApplicativeFunctorKind<F, R> = f.apK(this)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.on(t: T): FunctorKind<F, R> = mapK { it(t) }
infix fun <T, R> ((T) -> R).on(t: T): R = this(t)

infix fun <FU, A, B, RESULT> ((A, B) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
infix fun <FU, A, B, C, RESULT> ((A, B, C) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, RESULT> ((A, B, C, D) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, RESULT> ((A, B, C, D, E) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, RESULT> ((A, B, C, D, E, F) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, RESULT> ((A, B, C, D, E, F, G) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, RESULT> ((A, B, C, D, E, F, G, H) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, RESULT> ((A, B, C, D, E, F, G, H, I) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J,RESULT> ((A, B, C, D, E, F, G, H, I, J) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, RESULT> ((A, B, C, D, E, F, G, H, I, J, K) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
fun <FU, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> ((A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W) -> RESULT).on(a: FunctorKind<FU, A>) = curry().on(a)
