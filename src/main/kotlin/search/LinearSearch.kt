package search

class LinearSearch(phoneBook: Array<String>) : Search(phoneBook) {
    override fun findWork() {
        for (num in phoneBook.indices) {
            if (phoneBook[num].contains(find)) {
                returnNum = num
                break
            }
        }
    }
}