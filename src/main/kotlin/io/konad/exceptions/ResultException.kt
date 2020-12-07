package io.konad.exceptions

import io.konad.Result

class ResultException(val errors: Result.Errors) : RuntimeException(errors.error.description)