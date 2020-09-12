package search

import TrackTime

abstract class Search(protected val phoneBook: Array<String>) {
    protected var find = ""
    protected var returnNum: Int? = null
    protected val lastIndex = if (phoneBook.isEmpty()) -1 else phoneBook.lastIndex
    private val time = TrackTime()
    private var found = 0

    fun find(name: String): Int {
        if (phoneBook.isEmpty()) return -1
        reset()
        find = name
        time.start()
        findWork()
        time.stop()
        if (returnNum != null && returnNum != -1) found++
        return returnNum ?: -1
    }

    fun found() = found

    fun timeElapsed() = time.elapsed()

    fun resetStats() {
        time.resetTime()
        found = 0
    }

    protected abstract fun findWork()

    private fun reset() {
        if (returnNum != null) returnNum = null
    }
}