package io.konad.usage.examples.model

import io.konad.*
import io.konad.Result.Companion.plus
import io.konad.applicative.builders.on
import io.konad.applicative.builders.plus

data class User(
    val username: String,
    val name: NameOfAPerson,
    val password: Password,
    val contacts: UserContacts?,
    val jobDescription: String
) {
    companion object {
        fun of(
            username: String,
            firstname: String,
            lastname: String,
            password: String,
            email: String? = null,
            phoneNumber: String? = null,
            jobDescription: String? = null
        ): Result<User> = ::User +
            username +
            NameOfAPerson(firstname, lastname).ok() +
            Password.of(password) +
            (::UserContacts + Email.of(email) + PhoneNumber.of(phoneNumber)) +
            jobDescription.ifNull("job description should not be null")
    }
}