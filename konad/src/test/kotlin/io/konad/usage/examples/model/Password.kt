package io.konad.usage.examples.model

import io.konad.Result
import io.konad.error
import io.konad.ok

data class Password private constructor(val value: String){

    companion object {
        fun of(passwordValue: String): Result<Password> = if(passwordValue.length >= 6)
            Password(passwordValue).ok()
            else "Password should be at least 6 characters length. It was ${passwordValue.length} characters".error()
    }

}