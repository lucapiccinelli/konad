package io.konad.usage.examples.model

import io.konad.applicative.builders.plus
import io.konad.field
import io.konad.plus

data class UserContacts(
    val email: Email? = null,
    val phoneNumber: PhoneNumber? = null){

    companion object{
        fun of(email: String?, phoneNumber: String?) =
            ::UserContacts +
                Email.of(email).field(UserContacts::email) +
                PhoneNumber.of(phoneNumber).field(UserContacts::phoneNumber)
    }
}
