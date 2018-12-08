package advent2018

data class Node(val noOfChildNodes: Int, val noOfMDEntries: Int)

class Tree(var input: List<Int>) {

    private var tree: List<Node> = listOf()
    private var metadata: List<Int> = listOf()
    private var noOfNeededMDEntries: Int = 0

    private fun addNode(num1: Int, num2: Int) {
//        println(num1)
//        println(num2)
//        println(input)
        if (noOfNeededMDEntries == input.size) {
            metadata = metadata.plus(input)
            noOfNeededMDEntries = 0
//            println(tree)
            return
        }

        noOfNeededMDEntries += num2

        this.tree = this.tree.plus(Node(num1, num2))
        input = input.subList(2, input.size)

        if (num1 == 0) { // we are in leaf node
            metadata = metadata.plus(input.take(num2))
            input = input.subList(num2, input.size)
            noOfNeededMDEntries -= num2
        }
    }

    fun run(): List<Int> {
        // 1. Start
        this.addNode(this.input[0], this.input[1])

        // 2. Keep going
        while(noOfNeededMDEntries != 0) {
            this.addNode(this.input[0], this.input[1])
        }

        return metadata//.sum()
    }
}

fun solve(input: List<Int>): List<Int> {
    return Tree(input).run()
}

fun main(args: Array<String>) {
    val input = readTextFromFile("src/main/resources/day8.txt").split(" ").map{ it.toInt() }
//    input.forEach { println(it) }
    println(solve(input))
}