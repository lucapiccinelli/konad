# Konad

[![Build and Test](https://github.com/lucapiccinelli/konad/workflows/build-and-test/badge.svg)](https://github.com/lucapiccinelli/konad/actions)

Simple Kotlin monads for every day error handling.

## Why another functional library for Kotlin?

I know, we have [Arrow](https://arrow-kt.io/) that is the best functional library around. Anyway if you only want to do simple tasks, like validating your domain classes, Arrow is a bit of an overkill.

Also, Arrow is a real functional library, with a plenty of functional concepts that you need to digest before being productive. For the typical OOP developer, it has a quite steep learning curve.

## Konad to the OOP rescue

Here it comes Konad. It has only two classes:
 - [**Result**](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/Result.kt): can be Ok or Errors.
 - [**Maybe**](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/Maybe.kt): you know this... yet another Optional/Option/Nullable whatever. (But read the below description, it will get clear why we need it)
 
Konad exists **with the only purpose** to let you easily compose those two classes.

## Usage example

Let's say you have a `User` class, that has an `Email` and a `PhoneNumber`. Email and PhoneNumber are built so that they can only be constructed using a factory method. It will return a `Result.Errors` type if the value passed is not valid.

```kotlin

data class User(val username: String, val email: Email, val phoneNumber: PhoneNumber, val firstname: String)

data class Email private constructor (val value: String) {
    companion object{
        fun of(emailValue: String) = if (Regex(EMAIL_REGEX).matches(emailValue))
            Result.Ok(Email(emailValue))
            else Result.Errors("$emailValue doesn't match an email format")
    }
}

data class PhoneNumber private constructor(val value: String){
    companion object {
        fun of(phoneNumberValue: String): Result<PhoneNumber?> = if(Regex(PHONENUMBER_REGEX).matches(phoneNumberValue))
            Result.Ok(PhoneNumber(phoneNumberValue))
            else Result.Errors("$phoneNumberValue should match a valid phone number, but it doesn't")
    }
}

```

`Email` and `PhoneNumber` constructors are private, so that you can be sure that it can't exist a `User` with invalid contacts. However, the factory methods give you back a `Result<Email>/Result<PhoneNumber>`. 
In order to compose them and get a Result<User> you have to do

```kotlin

    val user: Result<User> = ::User.curry()
        .on("foo.bar")
        .on(Email.of("foo.bar")) // This email is invalid
        .on(PhoneNumber.of("xxx")) // This phone number is invalid
        .on("Foo")
        .result
    
    when(user){
        is Result.Ok -> user.toString()
        is Result.Errors -> user.toList().joinToString(" - ") { it.error.description }  
    }.run { println(this) } // This is going to print "foo.bar doesn't match an email format - xxx should match a valid phone number, but it doesn't

```

For those that are in love with the functional naming, you can choose this other style

```kotlin

    val user: Result<User> = ::User.curry()
        .apply("foo.bar")
        .map(Email.of("foo.bar")) 
        .ap(PhoneNumber.of("xxx"))
        .pure("Foo")
        .result

```