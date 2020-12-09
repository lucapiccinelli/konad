package io.konad.usage.examples.model

import io.konad.Result
import io.konad.applicative.builders.*
import io.konad.curry
import io.konad.result
import io.konad.ifNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CreateNewUserTests : StringSpec({

    val emailValue = "foo.bar@gmail.com"
    val phoneNumberValue = "+39 4756258"
    val passwordValue = "blablabla"
    val username = "foobar"
    val firstname = "Foo"
    val lastname = "Bar"
    val jobDescription = "developer"

    val expectedUser =
        Email.of(emailValue)
            .flatMap { email ->
        PhoneNumber.of(phoneNumberValue)
            .flatMap { phoneNumber ->
        Password.of(passwordValue)
            .map { password ->
                User(
                    username,
                    NameOfAPerson(firstname, lastname),
                    password,
                    UserContacts(email, phoneNumber),
                    jobDescription
                )
            } } }

    "How to build a new user" {
        val user = ::User.curry()
            .on(username)
            .on(NameOfAPerson(firstname, lastname))
            .on(Password.of(passwordValue))
            .on(::UserContacts.curry() on Email.of(emailValue) on PhoneNumber.of(phoneNumberValue))
            .on(jobDescription.ifNull("job description should not be null"))
            .result

        user shouldBe expectedUser
    }


    "How to build a new user in pure style" {

        val user = ::User.curry()
            .apply(username)
            .apply(NameOfAPerson(firstname, lastname))
            .map(Password.of(passwordValue))
            .ap(::UserContacts.curry() on Email.of(emailValue) on PhoneNumber.of(phoneNumberValue))
            .pure(jobDescription)
            .result

        user shouldBe expectedUser
    }

    "Example of errors" {
        val user = ::User.curry()
            .on(username)
            .on(NameOfAPerson(firstname, lastname))
            .on(Password.of(passwordValue))
            .on(::UserContacts.curry() on Email.of(emailValue) on PhoneNumber.of("xxx"))
            .on(null.ifNull("job description should not be null"))
            .result

        val errors: Result.Errors? = when(user){
            is Result.Ok -> null
            is Result.Errors -> user
        }

        errors shouldNotBe null
        errors?.toList()?.joinToString(",") { it.description } shouldBe "xxx should match a valid phone number, but it doesn't,job description should not be null"
    }
})
