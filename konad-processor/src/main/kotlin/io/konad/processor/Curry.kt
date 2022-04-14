package io.konad.processor

interface Curry {
    fun generate(maxArgs: Int, name: String = "curry", resultTypeName: String = "RESULT"): String
}