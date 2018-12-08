package advent2018

import java.io.File

fun readAllLinesFromFile(fileName: String) = File(fileName).readLines()

fun readTextFromFile(fileName: String) = File(fileName).readText()