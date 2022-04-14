package io.konad.processor

class SimpleCurry(private val genericsAlphabet: List<String>? = null) : Curry {
    companion object {
        val baseAlphabet = listOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z"
        )
    }

    override fun generate(maxArgs: Int, name: String, resultTypeName: String): String {
        val genericsTable = getGenericsTable(maxArgs)
        val genericsList = genericsTable.joinToString(", ") { it }

        return "fun <$genericsList, $resultTypeName> (($genericsList) -> $resultTypeName).$name() = ${
            recurseCurry(genericsTable, genericsTable)
        }"
    }

    private fun getGenericsTable(maxArgs: Int) = (genericsAlphabet
        ?: GenericsHelper.generateAlphabet(baseAlphabet, (maxArgs / baseAlphabet.size) + 1)).take(maxArgs)

    private fun recurseCurry(args: List<String>, currentArgs: List<String>): String {
        val currentArg = currentArgs[0]
        val currentArgDeclaration = "${currentArg.lowercase()}: $currentArg"
        return when (currentArgs.size) {
            1 -> "{ $currentArgDeclaration -> this(${args.joinToString(", ") { it.lowercase() }}) }"
            else -> "{ $currentArgDeclaration -> ${recurseCurry(args, currentArgs.drop(1))} }"
        }
    }
}
