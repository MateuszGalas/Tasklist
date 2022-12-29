package tasklist

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.datetime.*
import java.io.File

class TaskList {
    val taskList = mutableListOf<MutableList<String>>()

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
            0 -> "\u001B[103m \u001B[0m"
            in 1..Int.MAX_VALUE -> "\u001B[102m \u001B[0m"
            else -> "\u001B[101m \u001B[0m"
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
            priority.equals("c", true) -> "\u001B[101m \u001B[0m"
            priority.equals("h", true) -> "\u001B[103m \u001B[0m"
            priority.equals("n", true) -> "\u001B[102m \u001B[0m"
            priority.equals("l", true) -> "\u001B[104m \u001B[0m"
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
        var tasks = ""
        tasks += "+----+------------+-------+---+---+--------------------------------------------+\n"
        tasks += String.format("| %-2s |    %-8s| %-6s| %s | %s |%23s%-21s|\n", "N", "Date", "Time", "P", "D", "Task", "")

        taskList.forEachIndexed { index, strings ->
            tasks += "+----+------------+-------+---+---+--------------------------------------------+\n"
            var str = ""
            tasks += String.format("| %-2d |", index + 1)
            strings.forEach {
                when (taskList[index].indexOf(it)) {
                    0 -> tasks += "%11s |".format(it)
                    1 -> tasks += "%6s |".format(it)
                    2 -> tasks += " %s |".format(it)
                    3 -> tasks += " %s |".format(it)
                    4 -> {
                        for (i in it.indices) {
                            if (i % 44 == 0 && i != 0) {
                                tasks += "%-44s|\n".format(str)
                                tasks += String.format("|%4s|%12s|%7s|%3s|%3s|", "", "", "", "", "")
                                str = ""
                            }
                            str += it[i]
                        }
                        tasks += "%-44s|\n".format(str)
                        str = ""
                    }
                    else -> {
                        tasks += String.format("|%4s|%12s|%7s|%3s|%3s|", "", "", "", "", "")
                        for (i in it.indices) {
                            if (i % 44 == 0 && i != 0) {
                                tasks += "%-44s|\n".format(str)
                                tasks += String.format("|%4s|%12s|%7s|%3s|%3s|", "", "", "", "", "")
                                str = ""
                            }
                            str += it[i]
                        }
                        tasks += "%-44s|\n".format(str)
                        str = ""
                    }
                }
            }
        }
        tasks += "+----+------------+-------+---+---+--------------------------------------------+\n"
        println(tasks)
    }
}

data class Task(val date: String, val time: String, val priority: String, val d: String, val task: MutableList<String>)

fun main() {
    val taskList = TaskList()
    val jsonFile = File("tasklist.json")

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val taskAdapter = moshi.adapter(Task::class.java)
    val type = Types.newParameterizedType(List::class.java, Task::class.java)
    val tasksAdapter = moshi.adapter<List<Task?>>(type)

    if (jsonFile.exists()) {
        val json = jsonFile.readLines()
        for (i in json) {
            val task = tasksAdapter!!.fromJson(i)
            for (k in task!!) {
                taskList.taskList.add(mutableListOf())
                taskList.taskList[taskList.taskList.lastIndex].add(k?.date.toString())
                taskList.taskList[taskList.taskList.lastIndex].add(k?.time.toString())
                taskList.taskList[taskList.taskList.lastIndex].add(k?.priority.toString())
                taskList.taskList[taskList.taskList.lastIndex].add(k?.d.toString())

                for (j in k!!.task) {
                    taskList.taskList[taskList.taskList.lastIndex].add(j)
                }
            }
        }

        jsonFile.writeText("")
    }

    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        when (readln()) {
            "add" -> taskList.add()
            "print" -> taskList.print()
            "delete" -> taskList.print().also { taskList.delete() }
            "edit" -> taskList.print().also { taskList.edit() }
            "end" -> {
                println("Tasklist exiting!")
                break
            }
            else -> println("The input action is invalid")
        }
    }

    if (taskList.taskList.isEmpty()) return
    val list = mutableListOf<String?>()
    taskList.taskList.forEachIndexed { index, strings ->
        var counter = 0
        var date = ""
        var time = ""
        var priority = ""
        var due = ""
        val task = mutableListOf<String>()
        strings.forEach {
            when (counter) {
                0 -> date = it
                1 -> time = it
                2 -> priority = it
                3 -> due = it
                else -> task.add(it)
            }
            counter++
        }

        val taskToJson = Task(date, time, priority, due, task)
        if (!jsonFile.exists()) jsonFile.createNewFile()
        list.add(taskAdapter.toJson(taskToJson))
    }
    jsonFile.appendText(list.toString())
}