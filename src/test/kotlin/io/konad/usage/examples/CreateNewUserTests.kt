package io.konad.usage.examples

import io.konad.*
import io.konad.Maybe.Companion.nullable
import io.konad.applicative.builders.*
import io.konad.exceptions.ResultException
import io.konad.usage.examples.model.*
import io.kotest.assertions.throwables.shouldThrow
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
        val contacts: Result<UserContacts> = ::UserContacts +
            Email.of(emailValue) +
            PhoneNumber.of(phoneNumberValue)

        val user: Result<User> = ::User +
            username +
            NameOfAPerson(firstname, lastname).ok() +
            Password.of(passwordValue) +
            contacts +
            jobDescription.ifNull("job description should not be null")

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

    "Example of cumulating errors" {
        val user = ::User.curry()
            .on(username.ok())
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

    "Example of cumulating errors with better error description using field" {
        val user = ::User +
            username.ok() +
            NameOfAPerson(firstname, lastname) +
            Password.of("123").field(User::password) +
            UserContacts.of(emailValue, "xxx").field(User::contacts) +
            "bla"

        val expectedError = """
            password: Password should be at least 6 characters length. It was 3 characters
            contacts.phoneNumber: xxx should match a valid phone number, but it doesn't
            """.trimIndent()
        val ex = shouldThrow<ResultException> { user.get() }
        ex.errors.description("\n") shouldBe expectedError
    }

    "Example of build a nullable User" {
        val userContacts: UserContacts? = ::UserContacts +
            Email.of(emailValue).toNullable() +
            PhoneNumber.of(phoneNumberValue).toNullable()

        val user: User? = ::User +
            username +
            NameOfAPerson(firstname, lastname) +
            Password.of(passwordValue).toNullable() +
            userContacts +
            jobDescription

        user shouldBe expectedUser.get()
    }

    "Example of build a nullable that returns null"{
        val user: User? = ::User.curry()
            .on(username)
            .on(NameOfAPerson(firstname, lastname))
            .on(Password.of(passwordValue).toMaybe())
            .on(::UserContacts.curry() on Email.of(emailValue).toMaybe() on PhoneNumber.of("xxx").toMaybe())
            .on(jobDescription)
            .nullable

        user shouldBe null
    }

    "How to exit the monad with default values"{
        val xx: Int? = 1
        val yy: Int? = null

        val total: Int = ({ x: Int, y: Int -> x + y } +
            xx.ifNull("xx should not be null") +
            yy.ifNull("yy should not be null"))
            .ifError(-1)

        total shouldBe -1
    }

    "How to cheat. (I would suggest not using it)"{
        val xx: Int? = 1
        val yy: Int? = null

        shouldThrow<ResultException>{
            { x: Int, y: Int -> x + y }.curry()
                .on(xx.ifNull("xx should not be null"))
                .on(yy.ifNull("yy should not be null"))
                .result
                .get()
        }
    }
})
