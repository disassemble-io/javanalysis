package samples

var iincVar = 0

fun iinc() {
    var a = 0
    var b = 1
    var c = a
    for (c in 0..10) {
        iincVar++
    }
}