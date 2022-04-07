package io.konad.processor

object GenericsHelper{
    tailrec fun generateAlphabet(baseAlphabet: List<String>, depth: Int, alphabet: List<String> = emptyList()): List<String> = when(depth) {
        0 -> alphabet
        else -> generateAlphabet(baseAlphabet, depth - 1, baseAlphabet.map { it.repeat(depth) } + alphabet)
    }
}