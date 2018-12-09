package advent2018

class Foo() {
    private var dependencies: MutableMap<String, List<String>> = mutableMapOf()
    private var ordered: MutableList<Pair<String, Int>> = mutableListOf()

    fun addDependency(before: String, after: String) {
        if (dependencies.get(before) == null || dependencies.get(before)!!.isEmpty()) {
            dependencies.put(before, listOf(after))
        } else {
            dependencies.put(before, dependencies.get(before)!!.plus(after))
        }
    }

    private fun findRoot(): String {
        return dependencies.keys.find { key ->
            !dependencies.values.flatten().contains(key)
        }!!
    }

    private fun findDependencies(elem: String, level: Int) {
        if (ordered.find{elem == it.first} != null) { // it already exists and needs to move
            ordered.removeIf { elem == it.first }
        }
        ordered.add(Pair(elem, level))
        if (dependencies[elem] != null) {
            dependencies[elem]!!.sorted().forEach { findDependencies(it, level + 1) }
        }
    }

    private fun specialSort() {

        fun lowerLevelLater(): Int? {
            var x: Int? = null
            ordered.forEachIndexed { idx, _ ->
                if (idx + 1 < ordered.size && ordered[idx].second > ordered[idx+1].second && ordered[idx].first > ordered[idx+1].first) x = idx
            }
            return x
        }

        while (lowerLevelLater() != null) {
            val n = lowerLevelLater()
            val x = ordered[n!!]
            val y = ordered[n+1]
            ordered[n] = y
            ordered[n+1] = x
        }
    }

    fun solve(): String {
        val root = findRoot()
        findDependencies(root, 1)
        specialSort()
        return ordered.map{it.first}.joinToString("")
    }

}

fun buildAndSolve(lines: List<String>): String {
    val foo = Foo()
    val regex = Regex("""Step (.) must be finished before step (.) can begin.""")

    lines.forEach { line ->
        val groupValues = regex.find(line)!!.groupValues
        val before = groupValues[1]
        val after = groupValues[2]
        foo.addDependency(before, after)
    }
    return foo.solve()
}


fun main(args: Array<String>) {
    val lines = readAllLinesFromFile("src/main/resources/day7.txt")
//    lines.forEach { println(it) }
    println(buildAndSolve(lines))
}