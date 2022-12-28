package tasklist

import kotlinx.datetime.*

class TaskList {
    private val taskList = mutableListOf<MutableList<String>>()

    fun add() {
        taskList.add(mutableListOf())
        val priority = addTaskPriority()
        val date = addDate()

        taskList[taskList.lastIndex].add(date.toString())
        taskList[taskList.lastIndex].add(addTime())
        taskList[taskList.lastIndex].add(priority)
        taskList[taskList.lastIndex].add(countDays(date))
        addTask(taskList.lastIndex)
    }

    private fun countDays(date: LocalDate): String {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date

        return when (currentDate.daysUntil(date)) {
            0 -> "T"
            in 1..Int.MAX_VALUE -> "I"
            else -> "O"
        }
    }

    private fun addTask(index: Int) {
        println("Input a new task (enter a blank line to end):")
        while (true) {
            val input = readln().trim()
            if (input.matches("""\s+""".toRegex()) || input == "" && taskList[index].size == 4) {
                taskList.removeAt(index)
                println("The task is blank")
                break
            }
            if (input.matches("""\s+""".toRegex()) || input == "") break
            taskList[index].add(input)
        }
    }

    fun edit() {
        if (taskList.isEmpty()) return
        println("Input the task number (1-${taskList.size}):")
        try {
            val index = readln().toInt()
            if (index !in 1..taskList.size) throw Exception()
            editField(index)
        } catch (e: Exception) {
            println("Invalid task number")
            return edit()
        }
    }

    private fun editField(index: Int) {
        println("Input a field to edit (priority, date, time, task):")
        val taskNumber = index - 1
        when (readln()) {
            "date" -> {
                val date = addDate()
                val days = countDays(date)
                taskList[taskNumber][0] = date.toString()
                taskList[taskNumber][3] = days
            }

            "time" -> taskList[taskNumber][1] = addTime()
            "priority" -> taskList[taskNumber][2] = addTaskPriority()
            "task" -> {
                taskList[taskNumber].subList(4, taskList[taskNumber].size).clear()
                addTask(taskNumber)
            }

            else -> {
                println("Invalid field")
                return editField(index)
            }
        }
        println("The task is changed")
    }

    fun delete() {
        if (taskList.isEmpty()) return
        println("Input the task number (1-${taskList.size}):")
        try {
            val index = readln()
            taskList.removeAt(index.toInt() - 1)
        } catch (e: Exception) {
            println("Invalid task number")
            return delete()
        }
        println("The task is deleted")
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

    private fun addDate(): LocalDate {
        println("Input the date (yyyy-mm-dd):")
        val date = readln().split("-")
        val dateTime: LocalDate
        try {
            dateTime = LocalDate(date[0].toInt(), date[1].toInt(), date[2].toInt())
        } catch (e: Exception) {
            println("The input date is invalid")
            return addDate()
        }
        return dateTime
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
        if (taskList.isEmpty()) {
            println("No tasks have been input")
            return
        }
        taskList.forEachIndexed { index, strings ->
            var str = ""
            print(String.format("%-2d ", index + 1))
            strings.forEach {
                if (taskList[index].indexOf(it) < 4) {
                    str += " $it"
                } else if (taskList[index].indexOf(it) == 4) {
                    println(str.trim())
                    println("   %s".format(it))
                } else {
                    println("   %s".format(it))
                }
            }
            println()
        }
    }
}

fun main() {
    val taskList = TaskList()

    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        when (readln()) {
            "add" -> taskList.add()
            "print" -> taskList.print()
            "delete" -> taskList.print().also { taskList.delete() }
            "edit" -> taskList.print().also { taskList.edit() }
            "end" -> println("Tasklist exiting!").also { return }
            else -> println("The input action is invalid")
        }
    }
}