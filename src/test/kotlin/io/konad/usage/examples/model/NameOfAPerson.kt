package io.konad.usage.examples.model;

data class NameOfAPerson(
    val firstname: String,
    val lastname: String){

    val displayName = "$firstname $lastname"
}