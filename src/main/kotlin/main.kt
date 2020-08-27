import java.io.File

fun main() {
    val path = "src/main/kotlin/textFiles/"
    val phoneBook = import(path + "directory.txt")
    val find = import(path + "find.txt")

    search(phoneBook, find)
}

fun import(fileName: String): Array<String> {
    val file = File(fileName)
    return file.readText().split("\n").toTypedArray()
}

fun search(phoneBook: Array<String>, find: Array<String>) {
    var time = 0L
    var found = 0
    val getTime = { System.currentTimeMillis() }
    val formatTime = { String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", time) }

    println("Start searching...")
    time = getTime()
    for (name in find) {
        for (entry in phoneBook) {
            if (entry.contains(name)) {
                found++
                break
            }
        }
    }
    time = getTime() - time
    println("Found $found / ${find.size} entries. Time taken: ${formatTime()}")
}