package tasklist

class TaskList() {
    private val taskList = mutableListOf<MutableList<String>>()

    fun add() {
        println("Input a new task (enter a blank line to end):")
        taskList.add(mutableListOf())
        while (true) {
            val input = readln().trim()
            if (input.matches("""\s+""".toRegex()) || input == "" && taskList[taskList.lastIndex].isEmpty()){
                println("The task is blank")
                break
            }
            if (input.matches("""\s+""".toRegex()) || input == "") break
            taskList[taskList.lastIndex].add(input)
        }
        if (taskList[taskList.lastIndex].isEmpty()) taskList.removeAt(taskList.lastIndex)
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


