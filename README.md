<img src="assets/konad-logo.png" alt="Konad" width="600"/>

---

[![Build and Test](https://github.com/lucapiccinelli/konad/workflows/build-and-test/badge.svg)](https://github.com/lucapiccinelli/konad/actions)

Simple Kotlin monads for every day error handling.

## Why another functional library for Kotlin?

I know, we have [Arrow](https://arrow-kt.io/) that is the best functional library around. Anyway if you only want to do simple tasks, like validating your domain classes, Arrow is a bit of an overkill.

Also, Arrow is a real functional library, with a plenty of functional concepts that you need to digest before being productive. For the typical OOP developer, it has a quite steep learning curve.

## Konad to the OOP rescue

Here it comes Konad. It has only two classes:
 - [**Result**](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/Result.kt): can be `Result.Ok` or `Result.Errors`.
 - [**Maybe**](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/Maybe.kt): you know this... yet another Optional/Option/Nullable whatever. (But read the [Maybe](#maybe) section below, it will get clear why we need it)
 
These are **monads** and **applicative functors**, so they implement the usual `map`, `flatMap` and `ap` methods. 

Konad exists **with the only purpose** to let you easily compose these two classes.

## Getting started

Add the dependency

#### Maven
add in pom.xml
```xml
<dependency>
    <groupId>io.github.lucapiccinelli</groupId>
    <artifactId>konad</artifactId>
    <version>1.0.1</version>
</dependency>
```

#### Gradle
add in build.gradle
```groovy
dependencies {
    implementation "io.github.lucapiccinelli:konad:1.0.0"
}
```

## Usage example

*For an exaustive list of usage examples, please refer to test suite [CreateNewUserTests.kt](https://github.com/lucapiccinelli/konad/blob/master/src/test/kotlin/io/konad/usage/examples/CreateNewUserTests.kt)
and to [ResultTests.kt](https://github.com/lucapiccinelli/konad/blob/master/src/test/kotlin/io/konad/ResultTests.kt)*

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

In order to compose them and get a `Result<User>` you have to do the following

```kotlin

    val user: Result<User> = ::User.curry()
        .on("foo.bar")
        .on(Email.of("foo.bar")) // This email is invalid -> returns Result.Errors
        .on(PhoneNumber.of("xxx")) // This phone number is invalid -> returns Result.Errors
        .on("Foo")
        .result
    
    when(user){
        is Result.Ok -> user.toString()
        is Result.Errors -> user.toList().joinToString(" - ") { it.error.description }  
    }.run { println(this) } // This is going to print "foo.bar doesn't match an email format - xxx should match a valid phone number, but it doesn't

```

## The pure functional style.

Composition happens thanks to concepts named **functors** and **applicative Functors**.

I chose to stay simple and practical, then all the methods that implement composition are called `on` (See [applicativeBuilders.kt](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/applicative/builders/applicativeBuilders.kt)).
However, for those who love the functional naming, you can choose this other style. (See [applicativeBuildersPureStyle.kt](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/applicative/builders/applicativeBuildersPureStyle.kt))

```kotlin

    val user: Result<User> = ::User.curry()
        .apply("foo.bar")
        .map(Email.of("foo.bar")) 
        .ap(PhoneNumber.of("xxx"))
        .pure("Foo")
        .result

```

<a name="maybe"></a>
## Maybe

`Maybe` is needed only to wrap Kotlin *nullables* and bring them to a **higher-kinded type** (see [unaryHigherKindedTypes.kt](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/hkt/unaryHigherKindedTypes.kt)). 
In this way `on`, can be used to compose nullables. 

Its constructor is private because **you should avoid using it** in order to express *optionality*. Kotlin nullability is perfect for that purpose.

### How to compose nullables
If ever you tried to compose *nullables* in Kotlin, then probably you ended up having something like the following

```kotlin

val foo: Int? = 1
val bar: String? = "2"
val baz: Float? = 3.0f

fun useThem(x: Int, y: String, z: Float): Int = x + y.toInt() + z.toInt()

val result1: Int? = foo
    ?.let { bar
    ?.let { baz
    ?.let { useThem(foo, bar, baz) } } }

// or

val result2: Int? = if(foo != null && bar != null && baz != null) 
    useThem(foo, bar, baz) 
    else null

```

This is not very clean. And it gets even worse if would like to give an error message when a `null` happens.

Using Konad, nullables can be composed as follows 

```kotlin

val result: Int? = ::useThem.curry() 
    .on(foo.maybe) 
    .on(bar.maybe) 
    .on(baz.maybe)
    .nullable

```

or you can choose to give an explanatory message when something is `null`

```kotlin

val result: Result<Int> = ::useThem.curry() 
    .on(foo.ifNull("Foo should not be null")) 
    .on(bar.ifNull("Bar should not be null")) 
    .on(baz.ifNull("Baz should not be null"))
    .result

```

## Flatten

What if you have a `List<Result<T>>` and you want a `Result<List<T>>`? Then use `flatten` extension method.

```kotlin

val r: Result<List<Int>> = listOf(Result.Ok(1), Result.Ok(2))
    .flatten(Result.Companion::pure)
    .result

```

Errors gets cumulated as usual

```kotlin
val r: Result<List<String>> = listOf(Result.Errors("error1"), Result.Ok(1), Result.Errors("error2"))
    .flatten(Result.Companion::pure)
    .result

println(r.description) // will print error1 - error2
```

Obviously it works also on nullables: `Collection<T?> -> Collection<T>?`

```kotlin
setOf("a", null, "c")
    .map { it.maybe }
    .flatten(Maybe.Companion::pure)
    .nullable

flattened shouldBe null
```

## Extend with your own composable monads

If you wish to implement your own monads and let them be composable through the `on` **Konad applicative builders**, then you need to implement the interfaces
that are here: [Higher-kinded types](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/hkt/unaryHigherKindedTypes.kt)

Actually, to let your type be composable, it is enough to implement the `ApplicativeFunctorKind` interface.

Kotlin doesn't natively supports *Higher-kinded types*. To implement them, Konad is inspired on [how those are implemented in Arrow](https://arrow-kt.io/docs/patterns/glossary/#higher-kinds).
That is why there is the need of `.result` and `.nullable` extension properties.
