package samples

var caseVar = 0

fun tableswitch() {
    when (caseVar) {
        1 -> {
            println("A")
        }
        2 -> {
            println("B")
        }
        else -> {
            println("C")
        }
    }
}