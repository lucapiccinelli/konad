package io.konad.usage.examples.model

import io.konad.applicative.builders.on
import io.konad.curry
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CreateNewUserTests : StringSpec({

    "How to build a new user" {
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

        val user = User.of(username, firstname, lastname, passwordValue, emailValue, phoneNumberValue, jobDescription)

        user shouldBe expectedUser
    }

})