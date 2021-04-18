import io.konad.hkt.ApplicativeFunctorKind
import io.konad.hkt.FunctorKind
import io.konad.hkt.Kind
import io.konad.hkt.MonadKind

interface Kind2<out F, out A, out B>: Kind<Kind<F, A>, B>
interface FunctorKind2<out F, out A, out B>: Kind2<F, A, B>, FunctorKind<Kind<F, A>, B>
interface ApplicativeFunctorKind2<F, A, out B>: FunctorKind2<F, A, B>, ApplicativeFunctorKind<Kind<F, A>, B>
interface MonadKind2<F, A, out B>: FunctorKind2<F, A, B>, MonadKind<Kind<F, A>, B>

typealias Kind2_<F, A, B> = Kind<Kind<F, A>, B>
typealias FunctorKind2_<F, A, B> = FunctorKind<FunctorKind<F, A>, B>
