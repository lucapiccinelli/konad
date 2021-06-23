<img src="assets/konad-logo.png" alt="Konad" width="600"/>

---

[![Build and Test](https://github.com/lucapiccinelli/konad/workflows/build-and-test/badge.svg)](https://github.com/lucapiccinelli/konad/actions)
[![Maven Central](http://img.shields.io/maven-central/v/io.github.lucapiccinelli/konad.svg)](https://search.maven.org/search?q=a:konad)

Monads composition API that just works. For OOP developers. It is well suited to compose also Kotlin nullables.

## Why another functional library for Kotlin?

I know, we have [Arrow](https://arrow-kt.io/) that is the best functional library around. Anyway if you only want to do simple tasks, like validating your domain classes, Arrow is a bit of an overkill.

Also, Arrow is a real functional library, with a plenty of functional concepts that you need to digest before being productive. For the typical OOP developer, it has a quite steep learning curve.

## Konad to the OOP rescue

Here it comes Konad. It has only three classes:
 - [**Result**](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/Result.kt): can be `Result.Ok` or `Result.Errors`.
 - [**Validation**](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/Validation.kt): can be `Validation.Success` or `Validation.Fail`.
 - [**Maybe**](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/Maybe.kt): you know this... yet another Optional/Option/Nullable whatever. (But read the [Maybe](#maybe) section below, it will get clear why we need it)
 
These are **monads** and **applicative functors**, so they implement the usual `map`, `flatMap` and `ap` methods. 

Konad exists **with the only purpose** to let you easily compose these three classes.

Advanced use-cases examples are described here:
 - [Nice Kotlin Nullables and Where to Find Them](https://medium.com/swlh/nice-kotlin-nullables-and-where-to-find-them-85d8de481e41?source=friends_link&sk=992c123a45421d26a6e21637e4ecdfcd)
 - [Type-safe Domain Modeling in Kotlin](https://betterprogramming.pub/type-safe-domain-modeling-in-kotlin-425ddbc73732?source=friends_link&sk=2fedd10125b31cf7ca378878de4b3491)

## Getting started

Add the dependency

#### Maven
add in pom.xml
```xml
<dependency>
    <groupId>io.github.lucapiccinelli</groupId>
    <artifactId>konad</artifactId>
    <version>1.2.2</version>
</dependency>
```

#### Gradle
add in build.gradle
```groovy
dependencies {
    implementation "io.github.lucapiccinelli:konad:1.2.2"
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
        fun of(emailValue: String): Result<Email> = if (Regex(EMAIL_REGEX).matches(emailValue))
            Email(emailValue).ok()
            else "$emailValue doesn't match an email format".error()
    }
}

data class PhoneNumber private constructor(val value: String){
    companion object {
        fun of(phoneNumberValue: String): Result<PhoneNumber> = if(Regex(PHONENUMBER_REGEX).matches(phoneNumberValue))
            PhoneNumber(phoneNumberValue).ok()
            else "$phoneNumberValue should match a valid phone number, but it doesn't".error()
    }
}

```

`Email` and `PhoneNumber` constructors are private, so that you can be sure that it can't exist a `User` with invalid contacts. However, the factory methods give you back a `Result<Email>/Result<PhoneNumber>`. 

In order to compose them and get a `Result<User>` you have to do the following

```kotlin

    val userResult: Result<User> = ::User +
        "foo.bar" +
        Email.of("foo.bar") + // This email is invalid -> returns Result.Errors
        PhoneNumber.of("xxx") + // This phone number is invalid -> returns Result.Errors
        "Foo"
    
    when(userResult){
        is Result.Ok -> userResult.toString()
        is Result.Errors -> userResult.toList().joinToString(" - ")
    }.run(::println) // This is going to print "foo.bar doesn't match an email format - xxx should match a valid phone number, but it doesn't
    
    // or
    
    userResult
       .map{ user -> user.toString() }
       .ifError { errors -> errors.description(errorDescriptionsSeparator = " - ") }
       .run(::println)

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

val result: Int? = ::useThem + foo + bar + baz

```

or you can choose to give an explanatory message when something is `null`

```kotlin

val result: Result<Int> = ::useThem +
    foo.ifNull("Foo should not be null") +
    bar.ifNull("Bar should not be null") +
    baz.ifNull("Baz should not be null")

```

<a name="validation"></a>
## Validation

`Validation<A, B>` is like an `Either` monad, but with the left case accumulation. It is similar to `Result<T>` but instead of fixing the error case as a string description, it lets you
decide how you represent the error. Example:

```kotlin

sealed class ResourceError {
    data class BadInput(val description: String) : ResourceError()
    object NotFound : ResourceError()
    object Forbidden : ResourceError()
}

fun readUser(id: String): Validation<ResourceError, User> =
    if (id.isBlank()) ResourceError.BadInput("id should not be blank").fail()
    else repository.findById(id)?.success() ?: ResourceError.NotFound.fail()

readUser("xxx")
    .map { user: User -> println(user) }
    .ifFail { failures: Collection<ResourceError> -> println(failures) }

```

## Flatten

What if you have a `List<Result<T>>` and you want a `Result<List<T>>`? Then use `flatten` extension method.

```kotlin

val r: Result<Collection<Int>> = listOf(Result.Ok(1), Result.Ok(2)).flatten()

```

Errors get cumulated as usual

```kotlin
 val r: Result<Collection<Int>> = listOf(Result.Errors("error1"), Result.Ok(1), Result.Errors("error2"))
    .flatten()

when(r){
    is Result.Ok -> r.value.toString()
    is Result.Errors -> r.description
}.run(::println) // will print error1 - error2
```

Obviously it works also on nullables: `Collection<T?> -> Collection<T>?`

```kotlin
val flattened = setOf("a", null, "c").flatten()

flattened shouldBe null
```

and on Validation

```kotlin
val v: Validation<String, Collection<Int>> = listOf("error1".fail(), 1.success(), "error2".fail()).flatten()
```

## Error enrichment

Sometime you need to add some details on an error, or to transform it. `Result` and `Validation` monads have convenience method for this case.
Examples:

```kotlin
fun checkNotEmpty(value: String) = if(value.isBlank()) "value should not be blank".error() else value.ok()

data class User private constructor(val firstName: String, val lastname: String){
    companion object{
        fun of(firstname: String, lastname: String): Result<User> = ::User.curry()
            .on(checkNotEmpty(firstname))
            .on(checkNotEmpty(lastname))
            .result
    }
}

```

in this example, if both `firstname` and `lastname` are blank, then you will get two errors. Unfortunately both of those errors will have the same description, and you will not be
able to distinct which `value should not be empty`. To fix, there is the method `Result::errorTitle`

```kotlin

fun of(firstname: String, lastname: String): Result<User> = ::User.curry()
    .on(checkNotEmpty(firstname).errorTitle("firstname"))
    .on(checkNotEmpty(lastname).errorTitle("lastname"))
    .result

```

You can find a more detailed specification here:
[ResultTests](https://github.com/lucapiccinelli/konad/blob/master/src/test/kotlin/io/konad/ResultTests.kt#L106)

Similarly, `Validation` has the `mapFail` method, to apply a tranformation on the error case. Examples here
[ValidationTests](https://github.com/lucapiccinelli/konad/blob/master/src/test/kotlin/io/konad/ValidationTests.kt#L101)

In case of accumulated errors, both `errorTitle` and `mapFail` are applied to the entire list of errors.

### Result<T>.field

Since version 1.2.2, there exist an extension method `Result<T>.field` that enables to add an error title in a type-safe manner.

```kotlin
fun of(firstname: String, lastname: String): Result<User> = ::User +
    checkNotEmpty(firstname).field(User::firstname) + 
    checkNotEmpty(lastname).field(User::lastname)
```

In this example, `field` will add the name of the property as an error title, while also checking at compile time if the type 
of the property matches the type of the corresponding constructor parameter 

## Extend with your own composable monads

If you wish to implement your own monads and let them be composable through the `on` **Konad applicative builders**, then you need to implement the interfaces
that are here: [Higher-kinded types](https://github.com/lucapiccinelli/konad/blob/master/src/main/kotlin/io/konad/hkt/unaryHigherKindedTypes.kt)

Actually, to let your type be composable, it is enough to implement the `ApplicativeFunctorKind` interface.

Kotlin doesn't natively supports *Higher-kinded types*. To implement them, Konad is inspired on [how those are implemented in Arrow](https://arrow-kt.io/docs/patterns/glossary/#higher-kinds).
That is why there is the need of `.result` and `.nullable` extension properties.
