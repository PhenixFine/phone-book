package scrapped

private lateinit var PHONE_BOOK: Array<String>

fun quickSortPivotH(sort: Array<String>): Array<String> {
    PHONE_BOOK = sort
    quickSort(0, PHONE_BOOK.lastIndex)
    return sort
}
private fun quickSort(low: Int, high: Int) {
    if (low < high) {
        val pivot = partition(low, high)
        if (pivot != -1) {
            quickSort(low, pivot - 1)
            quickSort(pivot + 1, high)
        }
    }
}

private fun partition(low: Int, high: Int): Int {
    var i = low
    var j = high
    val ij = { i++; j-- }
    val ijReset = { i = low; j = high }
    val pivot = PHONE_BOOK[high]
    var reversed = false
    var inOrder = false

    while (i < j) {
        if (PHONE_BOOK[i] < PHONE_BOOK[i + 1] || PHONE_BOOK[j] > PHONE_BOOK[j - 1]) break
        ij()
        if (i >= j) {
            reversed = true
        }
    }
    if (!reversed) {
        ijReset()
        while (i < j) {
            if (PHONE_BOOK[i] > PHONE_BOOK[i + 1] || PHONE_BOOK[j] < PHONE_BOOK[j - 1]) break
            ij()
            if (i >= j) inOrder = true
        }
    }
    ijReset()
    if (reversed) {
        while (j > i) {
            swapReferences(i, j)
            i++
            j--
        }
    } else if (!inOrder) {
        j = high - 1
        while (true) {
            while (i <= j && PHONE_BOOK[i] < pivot) i++
            while (j > i && PHONE_BOOK[j] > pivot) j--
            if (i >= j) break
            swapReferences(i, j)
        }
        if (PHONE_BOOK[i] > pivot) swapReferences(i, high)
    }

    return if (reversed || inOrder) -1 else i
}

private fun swapReferences(index1: Int, index2: Int) {
    val temp = PHONE_BOOK[index1]
    PHONE_BOOK[index1] = PHONE_BOOK[index2]
    PHONE_BOOK[index2] = temp
}