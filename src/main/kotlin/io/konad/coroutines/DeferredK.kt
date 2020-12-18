package io.konad.coroutines

import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind
import io.konad.hkt.Kind
import io.konad.hkt.MonadKind
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DeferredK<out T>(val value: Deferred<T>, override val coroutineContext: CoroutineContext):
    Deferred<T> by value,
    ApplicativeFunctorKind<DeferredKOf, T>,
    CoroutineScope
{
    inline fun <R> map(crossinline fn: (T) -> R): DeferredK<R> =
        DeferredK(async { fn(value.await()) }, coroutineContext)

    suspend fun <R> flatMap(fn: (T) -> DeferredK<R>): DeferredK<R> =
        fn(value.await())

    suspend fun <R> ap(fn: DeferredK<(T) -> R>): DeferredK<R> =
        flatMap { x -> fn.map { f -> f(x) } }

    override fun <R> mapK(fn: (T) -> R): FunctorKind<DeferredKOf, R> = mapK(fn)
    override fun <R> apK(liftedFn: FunctorKind<DeferredKOf, (T) -> R>): ApplicativeFunctorKind<DeferredKOf, R> = TODO()

}

open class DeferredKOf

fun <T> CoroutineScope.deferredK(valueProducer: () -> T): DeferredK<T> =
    DeferredK(async { valueProducer() }, coroutineContext)


fun <T> CoroutineScope.pure(value: T): DeferredK<T> = deferredK { value }


val <T> Kind<DeferredKOf, T>.defferred
    get() = this as DeferredK<T>
