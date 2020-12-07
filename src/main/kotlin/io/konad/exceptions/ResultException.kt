package io.konad.exceptions

import io.konad.Result

class ResultException(val error: Result.Error) : RuntimeException(error.description)