package search

// based on code from: https://www.geeksforgeeks.org/binary-search/
class BinarySearch(numbers: Array<String>) : Search(numbers) {
    private var left = 0
    private var right = lastIndex
    private val middle = { left + (right - left) / 2 }

    override fun findWork() {
        reset()
        while (returnNum == null) {
            search()
        }
    }

    private fun reset() {
        if (right != lastIndex) right = lastIndex
        if (left != 0) left = 0
    }

    private fun search() {
        if (right >= left) {
            val mid = middle()
            when {
                checkPhone(mid) == find -> {
                    returnNum = mid
                    return
                }
                checkPhone(mid) > find -> {
                    right = mid - 1
                    return
                }
                else -> {
                    left = mid + 1
                    return
                }
            }
        }
        returnNum = -1
    }
}