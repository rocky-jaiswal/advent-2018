package advent2018

import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class GuardAction {
    WAKE_UP, FALL_SLEEP, BEGIN_SHIFT, ERROR
}

data class TimeStampedAction(val date: LocalDate, val time: LocalTime, val action: GuardAction)

data class Guard(val id: String)

data class Message(val date: LocalDate, val time: LocalTime, val guard: Guard?, val action: GuardAction)

fun readLinesFromFile(fileName: String) = File(fileName).readLines()

fun linesToMessages(lines: List<String>): List<Message> {
    return lines.map m@{
        val strPart = it.split("] ")
        var guard: Guard? = null

        val dateAndTime = strPart[0]
        val date = LocalDate.parse(dateAndTime.substring(1, 11))
        val time = LocalTime.parse(dateAndTime.substring(12, 17), DateTimeFormatter.ofPattern("HH:mm"))

        val lastPart = strPart[1]
        var action = when (lastPart.trim()) {
            "wakes up" -> GuardAction.WAKE_UP
            "falls asleep" -> GuardAction.FALL_SLEEP
            else -> GuardAction.ERROR
        }
        if ("""Guard #\d+ begins shift""".toRegex().matches(lastPart.trim())) {
            action = GuardAction.BEGIN_SHIFT
            guard = Guard(lastPart.split(" ")[1])
        }

        return@m Message(date = date, time = time, guard = guard, action = action)
    }
}

fun aggregateSleepTime(messagesPerGuard: List<Message>?): Long {
  var sleepTime = listOf<Long>()
  var foo = listOf<LocalTime>()

    if (messagesPerGuard != null) {
        messagesPerGuard.forEach {
            if (it.action == GuardAction.FALL_SLEEP) {
                foo = foo.plus(it.time)
            }
            if (it.action == GuardAction.WAKE_UP) {
                sleepTime = sleepTime.plus(ChronoUnit.MINUTES.between(foo.last(), it.time))
                foo = listOf()
            }
        }
    }
  return sleepTime.sum()
}

fun addGuard(messagesByDate: List<Message>): Map<Guard?, List<Message>> {
    val withGuardAdded = messagesByDate.mapIndexed m@{ idx, message ->
        var i = idx
        if (message.guard == null) {
            while (messagesByDate[i].guard == null) {
                i -= 1
            }
        }
        return@m Message(message.date, message.time, messagesByDate[i].guard, message.action)
    }
    val byGuard = withGuardAdded.groupBy { it.guard }
    return byGuard
}

fun asleepMinutes(messagesByDate: List<Message>) {
    val byGuard = addGuard(messagesByDate)
    val lazyGuard = byGuard.filter { it != null && it.key?.id.equals("#857") }
    val sleepingTimesForLazyGuard = byGuard.get(Guard("#857"))!!
            .filter { it.action != GuardAction.BEGIN_SHIFT }
            .withIndex()
            .groupBy { it.index / 2 }
            .map { it.value.map { it.value.time } }

    var allMinutes = (0..59).map{ it to 0 }.toMap()
    var sleepingMinutes = listOf<Int>()

    allMinutes.keys.forEach { min ->
        val str = if (min.toString().length == 1) "0$min" else "$min"
        val time: LocalTime = LocalTime.parse("00:$str", DateTimeFormatter.ofPattern("HH:mm"))
        sleepingTimesForLazyGuard.forEach {
            if (time.isAfter(it.first()) && time.isBefore(it.last())) {
                sleepingMinutes = sleepingMinutes.plus(min)
            }
        }
    }
    sleepingMinutes.groupBy { it }.forEach{ println(it )}
}

fun perGuardSleepTime(messagesByDate: List<Message>): List<HashMap<Guard?, Long>> {
    val byGuard = addGuard(messagesByDate)
    return byGuard.keys.map { hashMapOf(it to aggregateSleepTime(byGuard.get(it))) }
}

fun findLaziestGuard(perGuardSleepTime: List<HashMap<Guard?, Long>>): List<HashMap<Guard?, Long>> {
    return perGuardSleepTime.sortedBy { it -> it.values.sum() }
}

fun main(args: Array<String>) {
    val lines = readLinesFromFile("src/main/resources/day4.txt")
    val messages = linesToMessages(lines)
    val messagesByDate = messages.sortedWith(compareBy(Message::date, Message::time))

    val perGuardSleepTime = perGuardSleepTime(messagesByDate)
    println(findLaziestGuard(perGuardSleepTime).last())
    println(asleepMinutes(messagesByDate))
}




