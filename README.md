# Konad

[![Build and Test](https://github.com/lucapiccinelli/konad/workflows/build-and-test/badge.svg)](https://github.com/lucapiccinelli/konad/actions)

Simple Kotlin monads for every day error handling.

## Why another functional library for Kotlin?

I know, we have [Arrow](https://arrow-kt.io/) that is the best functional library around. Anyway if you only want to do simple tasks, like validating your domain classes, Arrow is a bit an overkill.

Also, Arrow is a real functional library, with a plenty of functional concepts that you need to digest before being productive. For the typical OOP developer, it has a quite steep learning curve.

## Konad to the OOP rescue

Here it comes Konad. It has only two classes:
 - **Result**: can be Ok or Errors.
 - **Maybe**: you know this... another Optional. (But read the below description, it will get clear why we need it)
 
Konad exists **with the only purpose** to let you easily compose those two classes.

   
... WIP to be continued ...