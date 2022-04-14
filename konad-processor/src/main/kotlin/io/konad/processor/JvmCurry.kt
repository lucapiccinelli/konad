package io.konad.processor

class JvmCurry(private val alphabet: List<String>, private val curry: Curry = SimpleCurry(alphabet)) : Curry {

    override fun generate(maxArgs: Int, name: String, resultTypeName: String): String {
        return "@JvmName(\"$name${joinAlphabet(maxArgs)}$resultTypeName\")${curry.generate(maxArgs, name, resultTypeName)}"
    }

    private fun joinAlphabet(maxArgs: Int) = alphabet
        .take(maxArgs)
        .joinToString("")
}