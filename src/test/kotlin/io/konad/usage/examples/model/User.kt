package io.konad.usage.examples.model

import io.konad.Result
import io.konad.applicative.builders.on
import io.konad.curry
import io.konad.result
import io.konad.toResult

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
        ): Result<User> = ::User.curry()
            .on(username)
            .on(NameOfAPerson(firstname, lastname))
            .on(Password.of(password))
            .on(::UserContacts.curry() on Email.of(email) on PhoneNumber.of(phoneNumber))
            .on(jobDescription.toResult("job description should not be null"))
            .result


    }
}