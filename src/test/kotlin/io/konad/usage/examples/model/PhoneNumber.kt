package io.konad.usage.examples.model

import io.konad.Result

const val PHONENUMBER_REGEX = "^(\\+\\d{2})?\\s?(\\d\\s?)+\$"

data class PhoneNumber private constructor(val value: String){

    companion object {
        fun of(phoneNumberValue: String?): Result<PhoneNumber?> = phoneNumberValue
            ?.let {
                if(Regex(PHONENUMBER_REGEX).matches(phoneNumberValue))
                    Result.Ok(PhoneNumber(phoneNumberValue))
                    else Result.Errors("$phoneNumberValue should match a valid phone number, but it doesn't")
            }
            ?: Result.Ok(null)
    }

}