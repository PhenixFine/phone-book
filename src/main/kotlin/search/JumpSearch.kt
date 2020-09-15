package search

// based on code from:
// https://www.geeksforgeeks.org/jump-search/
// https://hyperskill.org/learn/step/3548
class JumpSearch(phoneBook: Array<String>) : Search(phoneBook) {
    private var currentRight = 0
    private var prevRight = 0
    private var jump = 0

    override fun findWork() {
        reset()
        for (i in 1..3) {
            when (i) {
                1 -> findBlock(lastIndex)
                2 -> checkBlock()
                3 -> linForward()
            }
            if (returnNum != null) break
        }
    }

    private fun reset() {
        if (currentRight != 0) currentRight = 0
        if (prevRight != 0) prevRight = 0
        jump = Math.sqrt((lastIndex + 1).toDouble()).toInt()
    }

    private fun findBlock(indexLast: Int) {
        while (checkPhone(currentRight) < find) {
            prevRight = currentRight
            currentRight = Math.min(currentRight + jump, indexLast)
            if (currentRight == indexLast && checkPhone(indexLast) < find) {
                returnNum = -1
                return
            }
        }

        if (checkPhone(currentRight) == find) returnNum = currentRight else if (prevRight >= currentRight - 1) {
            returnNum = -1
        }
    }

    private fun checkBlock() {
        val setJump = { jump = Math.sqrt(Math.max((currentRight - 1) - (prevRight), 1).toDouble()).toInt() }
        setJump()

        while (jump >= 2 && returnNum == null) {
            val indexLast = currentRight - 1
            currentRight = Math.min(prevRight + jump, lastIndex)
            findBlock(indexLast)
            setJump()
        }
    }

    private fun linForward() {
        while (checkPhone(prevRight) < find) {
            prevRight++
            if (prevRight > currentRight) {
                returnNum = -1
                return
            }
        }
        returnNum = if (checkPhone(prevRight) == find) prevRight else -1
    }
}