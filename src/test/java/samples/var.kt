package samples

val array = arrayOf(1, 2, 3)

fun swap(a: Int, b: Int) {
    val temp = array[a]
    array[a] = b
    array[b] = temp
}