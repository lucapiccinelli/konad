package io.konad.usage.examples.model

import io.konad.Result

data class Password private constructor(val value: String){

    companion object {
        fun of(passwordValue: String): Result<Password> = if(passwordValue.length >= 6)
            Result.Ok(Password(passwordValue))
            else Result.Errors("Password should be at least 6 characters length. It was ${passwordValue.length} characters")
    }

}