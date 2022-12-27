package tasklist

import kotlinx.datetime.*

class TaskList() {
    private val taskList = mutableListOf<MutableList<String>>()

    fun add() {
        val list = mutableListOf<String>()
        taskList.add(mutableListOf())
        val priority = addTaskPriority()
        list.add(addDate())
        list.add(addTime())
        list.add(priority)
        taskList[taskList.lastIndex].add(list.joinToString(" "))
        println("Input a new task (enter a blank line to end):")
        while (true) {
            val input = readln().trim()
            if (input.matches("""\s+""".toRegex()) || input == "" && taskList[taskList.lastIndex].size == 1) {
                taskList.removeAt(taskList.lastIndex)
                println("The task is blank")
                break
            }
            if (input.matches("""\s+""".toRegex()) || input == "") break
            taskList[taskList.lastIndex].add(input)
        }
    }

    private fun addTaskPriority(): String {
        println("Input the task priority (C, H, N, L):")
        val priority = readln()
        return when {
            priority.equals("c", true) -> "C"
            priority.equals("h", true) -> "H"
            priority.equals("n", true) -> "N"
            priority.equals("l", true) -> "L"
            else -> addTaskPriority()
        }
    }

    private fun addDate(): String {
        println("Input the date (yyyy-mm-dd):")
        val date = readln().split("-")
        val dateTime: LocalDate
        try {
            dateTime = LocalDate(date[0].toInt(), date[1].toInt(), date[2].toInt())
        } catch (e: Exception) {
            println("The input date is invalid")
            return addDate()
        }
        return dateTime.toString()
    }

    private fun addTime(): String {
        println("Input the time (hh:mm):")
        val date = readln().split(":")
        val dateTime: LocalDateTime
        try {
            dateTime = LocalDateTime(2000, 1, 1, date[0].toInt(), date[1].toInt())
        } catch (e: Exception) {
            println("The input time is invalid")
            return addTime()
        }
        if (dateTime.hour < 10 && dateTime.minute < 10) {
            return "0${dateTime.hour}:0${dateTime.minute}"
        }
        if (dateTime.hour < 10) {
            return "0${dateTime.hour}:${dateTime.minute}"
        }
        if (dateTime.minute < 10) {
            return "${dateTime.hour}:0${dateTime.minute}"
        }
        return "${dateTime.hour}:${dateTime.minute}"
    }

    fun print() {
        if (taskList.isEmpty()) println("No tasks have been input")
        else {
            taskList.forEachIndexed { index, strings ->
                print(String.format("%-2d ", index + 1))
                strings.forEach {
                    if (taskList[index].indexOf(it) == 0) println("%s".format(it))
                    else println("   %s".format(it))
                }
                println()
            }
        }
    }
}

fun main() {
    val taskList = TaskList()

    while (true) {
        println("Input an action (add, print, end):")
        val input = readln()
        when (input) {
            "add" -> taskList.add()
            "print" -> taskList.print()
            "end" -> break
            else -> println("The input action is invalid")
        }
    }

    println("Tasklist exiting!")

}


