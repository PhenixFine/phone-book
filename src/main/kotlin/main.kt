import search.BinarySearch
import search.JumpSearch
import search.LinearSearch
import java.io.File

private const val PATH = "src/main/kotlin/textFiles/"
private val PORT_DIRECTORY = { import(PATH + "directory.txt", true) }
private var PHONE_BOOK = PORT_DIRECTORY()
private val FIND = import(PATH + "find.txt")
private val BUBBLE_SORT_TIME = TrackTime()
private val QUICK_SORT_TIME = TrackTime()
private const val CUTOFF = 10

fun main() {
    val timeLimit = linSearch() * 10

    bubbleJump(timeLimit)
    PHONE_BOOK = PORT_DIRECTORY()
    quickBinary(timeLimit)
    hashSearch(timeLimit)
}

private fun import(fileName: String, moveNumbers: Boolean = false): Array<String> {
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

private fun linSearch(stopped: Boolean = false, timeElapsed: Long = 0L): Long {
    val linearSearch = LinearSearch(PHONE_BOOK)
    val add = if (stopped) " - STOPPED, moved to linear search" else ""

    if (!stopped) println(strStart("linear search"))
    for (name in FIND) linearSearch.find(name)
    printLines(linearSearch.found(), stopped, timeElapsed, add, linearSearch.timeElapsed())
    return linearSearch.timeElapsed()
}

private fun bubbleJump(timeLimit: Long) {
    println("\n" + strStart("bubble sort + jump search"))
    if (bubbleSort(timeLimit)) {
        val jumpSearch = JumpSearch(PHONE_BOOK)
        for (name in FIND) jumpSearch.find(name)
        printLines(jumpSearch.found(), true, BUBBLE_SORT_TIME.elapsed(), "", jumpSearch.timeElapsed())
    } else linSearch(true, BUBBLE_SORT_TIME.elapsed())
}

private fun quickBinary(timeLimit: Long) {
    println("\n" + strStart("quick sort + binary search"))
    QUICK_SORT_TIME.start()
    quickSort(0, PHONE_BOOK.lastIndex)
    QUICK_SORT_TIME.stop()
    if (QUICK_SORT_TIME.elapsed() < timeLimit) {
        val binarySearch = BinarySearch(PHONE_BOOK)
        for (name in FIND) binarySearch.find(name)
        printLines(binarySearch.found(), true, QUICK_SORT_TIME.elapsed(), "", binarySearch.timeElapsed())
    } else linSearch(true, QUICK_SORT_TIME.elapsed())
}

private fun hashSearch(timeLimit: Long) {
    val hashPhone = hashMapOf<String, Int>()
    val hashTime = TrackTime()
    val hashSearchTime = TrackTime()
    var hashFound = 0
    val getName = { i: Int -> PHONE_BOOK[i].substring((0 until PHONE_BOOK[i].lastIndexOf(' '))) }
    val getNumber =
        { i: Int -> PHONE_BOOK[i].substring(PHONE_BOOK[i].lastIndexOf(' ') + 1..PHONE_BOOK[i].lastIndex).toInt() }

    println("\n" + strStart("Start searching (hash table)..."))
    hashTime.start()
    for (entry in PHONE_BOOK.indices) hashPhone[getName(entry)] = getNumber(entry)
    hashTime.stop()

    if (hashTime.elapsed() < timeLimit) {
        hashSearchTime.start()
        for (name in FIND) if (hashPhone.containsKey(name)) hashFound++
        hashSearchTime.stop()
        printLines(hashFound, true, hashTime.elapsed(), "", hashSearchTime.elapsed(), true)
    } else linSearch(true, hashTime.elapsed())
}

private fun bubbleSort(timeLimit: Long): Boolean {
    var changed = true
    var stop = false

    while (changed && !stop) {
        changed = false
        BUBBLE_SORT_TIME.start()
        for (num in PHONE_BOOK.indices) {
            if (num != PHONE_BOOK.lastIndex) {
                if (PHONE_BOOK[num] > PHONE_BOOK[num + 1]) {
                    if (!changed) changed = true
                    swapReferences(num, num + 1)
                }
            }
        }
        BUBBLE_SORT_TIME.stop()
        if (BUBBLE_SORT_TIME.elapsed() > timeLimit) stop = true
    }
    return !stop
}

// Quick Sort Implementation with median-of-three partitioning and insertion sort ( for when sub section is small enough )
// modified from: https://bit.ly/35Cr3Lq
private fun quickSort(low: Int, high: Int) {
    if (low + CUTOFF > high) insertionSort(low, high) else {
        val middle = (low + high) / 2

        if (PHONE_BOOK[middle] < PHONE_BOOK[low]) swapReferences(low, middle)
        if (PHONE_BOOK[high] < PHONE_BOOK[low]) swapReferences(low, high)
        if (PHONE_BOOK[high] < PHONE_BOOK[middle]) swapReferences(middle, high)

        swapReferences(middle, high - 1)
        val pivot = PHONE_BOOK[high - 1]
        var i = low
        var j = high - 1

        while (true) {
            while (PHONE_BOOK[++i] < pivot);
            while (pivot < PHONE_BOOK[--j]);

            if (i >= j) break
            swapReferences(i, j)
        }
        swapReferences(i, high - 1)
        quickSort(low, i - 1)
        quickSort(i + 1, high)
    }
}

private fun swapReferences(index1: Int, index2: Int) {
    val temp = PHONE_BOOK[index1]
    PHONE_BOOK[index1] = PHONE_BOOK[index2]
    PHONE_BOOK[index2] = temp
}

private fun insertionSort(low: Int, high: Int) {
    for (p in low + 1..high) {
        val temp = PHONE_BOOK[p]
        var j = p
        while (j > low && temp < PHONE_BOOK[j - 1]) {
            PHONE_BOOK[j] = PHONE_BOOK[j - 1]
            j--
        }
        PHONE_BOOK[j] = temp
    }
}

private fun formatTime(time: Long) = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", time)

private fun printLines(
    found: Int,
    sort: Boolean,
    sortTime: Long,
    add: String,
    searchTime: Long,
    create: Boolean = false
) {
    println("Found $found / ${FIND.size} entries. Time taken: ${formatTime(sortTime + searchTime)}")
    if (sort) {
        println((if (create) "Creating" else "Sorting") + " time: " + formatTime(sortTime) + add)
        println("Searching time: " + formatTime(searchTime))
    }
}

private fun strStart(add: String) = "Start searching ($add)..."