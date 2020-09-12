import search.JumpSearch
import search.LinearSearch
import java.io.File

private const val PATH = "src/main/kotlin/textFiles/"
private val PHONE_BOOK = import(PATH + "directory.txt", true)
private val SORT_TIME = TrackTime()

fun main() {
    val find = import(PATH + "find.txt")

    search(find)
}

fun import(fileName: String, moveNumbers: Boolean = false): Array<String> {
    val tempList = mutableListOf<String>()
    val numToEnd = { phoneLine: String ->
        val num = phoneLine.indexOf(' ')
        phoneLine.substring(num + 1) + " " + phoneLine.substring(0, num)
    }

    File(fileName).forEachLine {
        tempList.add(if (moveNumbers) numToEnd(it) else it)
    }

    return tempList.toTypedArray()
}

fun search(find: Array<String>) {
    val linearSearch = LinearSearch(PHONE_BOOK)
    val jumpSearch: JumpSearch
    val strStart = { add: String -> "Start searching ($add)..." }
    val formatTime = { time: Long -> String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", time) }
    val strFound = { found: Int, time: Long -> "Found $found / ${find.size} entries. Time taken: ${formatTime(time)}" }
    val strSortTime = { "Sorting time: " + formatTime(SORT_TIME.elapsed()) }
    val strSearchTime = { time: Long -> "Searching time: " + formatTime(time) }

    println(strStart("linear search"))
    for (name in find) linearSearch.find(name)
    println(strFound(linearSearch.found(), linearSearch.timeElapsed()))
    println("\n" + strStart("bubble sort + jump search"))
    if (sort(linearSearch.timeElapsed() * 10)) {
        jumpSearch = JumpSearch(PHONE_BOOK)
        for (name in find) jumpSearch.find(name)
        println(strFound(jumpSearch.found(), jumpSearch.timeElapsed() + SORT_TIME.elapsed()))
        println(strSortTime())
        println(strSearchTime(jumpSearch.timeElapsed()))
    } else {
        linearSearch.resetStats()
        for (name in find) linearSearch.find(name)
        println(strFound(linearSearch.found(), linearSearch.timeElapsed() + SORT_TIME.elapsed()))
        println(strSortTime() + " - STOPPED, moved to linear search")
        println(strSearchTime(linearSearch.timeElapsed()))
    }
}

fun sort(timeLimit: Long): Boolean {
    var changed = true
    var stop = false

    while (changed && !stop) {
        changed = false
        SORT_TIME.start()
        for (num in PHONE_BOOK.indices) {
            if (num != PHONE_BOOK.lastIndex) {
                if (PHONE_BOOK[num] > PHONE_BOOK[num + 1]) {
                    if (!changed) changed = true
                    val hold = PHONE_BOOK[num]
                    PHONE_BOOK[num] = PHONE_BOOK[num + 1]
                    PHONE_BOOK[num + 1] = hold
                }
            }
        }
        SORT_TIME.stop()
        if (SORT_TIME.elapsed() > timeLimit) stop = true
    }
    return !stop
}