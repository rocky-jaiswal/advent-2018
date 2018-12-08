package advent2018

fun insertAtRightPlace(ordered: List<String>, before: String, after: String): List<String> {
    var startPos = ordered.indexOfFirst { it == before }
    val afterPos = ordered.indexOfFirst { it == after }
    if (afterPos != -1) {
        return insertAtRightPlace(ordered.filter { it != after }, before, after)
    }
    if (startPos == ordered.size - 1) {
        return ordered.plus(after)
    }
    while(startPos < ordered.size - 1) {
        startPos += 1
        if (ordered[startPos] > after) {
            return ordered.subList(0, startPos).plus(after).plus(ordered.subList(startPos, ordered.size))
        } else continue
    }
    return ordered.plus(after)
}

fun insertInPlace(ordered: List<String>, before: String, after: String): List<String> {
    if (!ordered.isEmpty() && !ordered.contains(before)) {
        println("hey this looks bad!!") // This is where our problem lies. We made a wrong assumption
    }
    return if (ordered.isEmpty()) {
        ordered.plus(before).plus(after)
    } else {
        insertAtRightPlace(ordered, before, after)
    }
}

fun solve(lines: List<String>): String {
    val regex = Regex("""Step (.) must be finished before step (.) can begin.""")
    var ordered = listOf<String>()
    lines.forEach { line ->
        val groupValues = regex.find(line)!!.groupValues
        val before = groupValues[1]
        val after = groupValues[2]
        ordered = insertInPlace(ordered, before, after)
    }
    return ordered.joinToString("")
}


fun main(args: Array<String>) {
    val lines = readAllLinesFromFile("src/main/resources/day7.txt")
    lines.forEach { println(it) }
    println(solve(lines))
}