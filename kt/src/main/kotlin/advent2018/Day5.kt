package advent2018

import java.io.File

fun isOppositeCase(st1: String, st2:String): Boolean {
    return st1 != st2 && st1.toLowerCase() == st2.toLowerCase()
}

tailrec fun reduce(str: String): String {
    val stArr = str.split("")
    var cancellable = listOf<Int>()
    var canDo = true

    stArr.forEachIndexed { idx, ch ->
        if (canDo && idx+1 < stArr.size && isOppositeCase(ch, stArr[idx + 1])) {
            cancellable = cancellable.plus(idx)
            cancellable = cancellable.plus(idx + 1)
            canDo = false
        }
    }

    val newStr = stArr.filterIndexed fi@{ idx, _ ->
        return@fi !cancellable.contains(idx)
    }.filter{ !it.isNullOrBlank() }.joinToString("")

    return if (newStr == str) newStr else reduce(newStr)
}

fun readStringFromFile(fileName: String) = File(fileName).readText()

fun main(args: Array<String>) {
    println(reduce(readStringFromFile("src/main/resources/day5.txt")).length)
}