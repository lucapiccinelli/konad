package io.konad.exceptions

data class EitherException(val data: Any?) : RuntimeException(data.toString())
