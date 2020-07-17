package nl.jhvh.sudoku.format

import org.apache.commons.lang3.StringUtils

val lineSeparator: String = System.lineSeparator()

/** @return The length of the longest [toString] of all elements in the [collection] */
fun maxStringLength (collection: Collection<*>): Int = collection.map { s -> s.toString().length } .max()!!

fun List<*>.alignRight(extraLeftPad: Int = 0, extraRightPad: Int = 0): List<String> {
    require(extraLeftPad >= 0, { "extraLeftPad must be non-negative or omitted (current: $extraLeftPad)" })
    require(extraRightPad >= 0, { "extraRightPad must be non-negative or omitted (current: $extraRightPad)" })
    val maxLength = maxStringLength(this)
    val padAction: (String) -> String = { it.padStart(maxLength + extraLeftPad).padEnd(maxLength + extraLeftPad + extraRightPad) }
    return this.map { padAction(it.toString()) }
}

fun List<*>.alignLeft(extraLeftPad: Int = 0, extraRightPad: Int = 0): List<String> {
    require(extraLeftPad >= 0, { "extraLeftPad must be non-negative or omitted (current: $extraLeftPad)" })
    require(extraRightPad >= 0, { "extraRightPad must be non-negative or omitted (current: $extraRightPad)" })
    val maxLength = maxStringLength(this)
    val padAction: (String) -> String = { it.padEnd(maxLength + extraRightPad).padStart(maxLength + extraLeftPad + extraRightPad) }
    return this.map { padAction(it.toString()) }
}

fun List<*>.alignCenter(extraLeftPad: Int = 0, extraRightPad: Int = 0): List<String> {
    require(extraLeftPad >= 0, { "extraLeftPad must be non-negative or omitted (current: $extraLeftPad)" })
    require(extraRightPad >= 0, { "extraRightPad must be non-negative or omitted (current: $extraRightPad)" })
    val maxLength = maxStringLength(this)
    val padAction: (String) -> String = {
        StringUtils.center(it, maxLength).padEnd(maxLength + extraRightPad).padStart(maxLength + extraLeftPad + extraRightPad)
    }
    return this.map { padAction(it.toString()) }
}

private fun Collection<*>.validateEqualSize(other: Collection<*>) {
    if (other.size != this.size) {
        throw IllegalArgumentException("Both collections must have equal sizes! Sizes: left=${this.size}, right=${other.size}")
    }
}

/**
 * Given 2 [List]s, concatenates the [toString] value of each element of the left (receiver) list with the corresponding elemeent
 * of the right ([other]) list
 * @receiver [List]`<*>`
 * @param other [List]`<*>`. Must have the same size as the receiver [List]
 * @return A new [List]`<String>` with the concatenated values.
 */
infix fun List<*>.concatEach(other: List<*>): List<String> {
    validateEqualSize(other)
    return this.mapIndexed { index, t -> t.toString() + other[index].toString() }
}

/**
 * Given a variable number of [List]s, concatenates the [toString] value of each element with the corresponding element
 * of the next [List]s
 * @param lists [Array]`<out [List]<*>>` A variable number of [List]s to have their elements concatenated.
 *        All [lists] must be equal in size.
 * @return A new [List]`<String>` with the concatenated values.
 */
fun concatEach(vararg lists: List<*>): List<String> {
    if (lists.isEmpty()) {
        return emptyList()
    }
    val result = lists[0].map { "" }
    return lists.fold(result, {current, next -> current concatEach next})
}

/**
 * Given 2 [List]s, concatenates the [toString] value of each element of the left (receiver) list with the corresponding element
 * of the right ([other]) list, values of each [List] right padded (left aligned) with respect to that [List]s values
 * @receiver [List]`<*>`
 * @param other [List]`<*>`. Must have the same size as the receiver [List]
 * @return A new [List]`<String>` with the concatenated and aligned values.
 */
infix fun List<*>.concatAlignLeft(other: List<*>): List<String> {
    return this.alignLeft() concatEach other.alignLeft()
}
/**
 * Given a variable number of [List]s, concatenates the [toString] value of each element with the corresponding element
 * of the next [List]s, values of each [List] right padded (left aligned) with respect to that [List]s values
 * @param lists [Array]`<out [List]<*>>` A variable number of [List]s to have their elements concatenated.
 *        All [lists] must be equal in size.
 * @return A new [List]`<String>` with the concatenated and aligned values.
 */
fun concatAlignLeft(vararg lists: List<*>): List<String> {
    if (lists.isEmpty()) {
        return emptyList()
    }
    val result = lists[0].map { "" }
    return lists.fold(result, {current, next -> current concatEach next.alignLeft()})
}

/**
 * Given 2 [List]s, concatenates the [toString] value of each element of the left (receiver) list with the corresponding element
 * of the right ([other]) list, values of each [List] (center aligned) with respect to that [List]s values
 * @receiver [List]`<*>`
 * @param other [List]`<*>`. Must have the same size as the receiver [List]
 * @return A new [List]`<String>` with the concatenated and aligned values.
 */
infix fun List<*>.concatAlignCenter(other: List<*>): List<String> {
    return this.alignCenter() concatEach other.alignCenter()
}

/**
 * Given a variable number of [List]s, concatenates the [toString] value of each element with the corresponding element
 * of the next [List]s, values of each [List] center aligned with respect to that [List]s values
 * @param lists [Array]`<out [List]<*>>` A variable number of [List]s to have their elements concatenated.
 *        All [lists] must be equal in size.
 * @return A new [List]`<String>` with the concatenated and aligned values.
 */
fun concatAlignCenter(vararg lists: List<*>): List<String> {
    if (lists.isEmpty()) {
        return emptyList()
    }
    val result = lists[0].map { "" }
    // val total = listOf(1, 2, 3, 4, 5).fold(0, { total, next -> total + next })
    return lists.fold(result, {current, next -> current concatEach next.alignCenter()})
}

/**
 * Given 2 [List]s, concatenates the [toString] value of each element of the left (receiver) list with the corresponding element
 * of the right ([other]) list, values of each [List] left padded (right aligned) with respect to that [List]s values
 * @receiver [List]`<*>`
 * @param other [List]`<*>`. Must have the same size as the receiver [List]
 * @return A new [List]`<String>` with the concatenated and aligned values.
 */
infix fun List<*>.concatAlignRight(other: List<*>): List<String> {
    return this.alignRight() concatEach other.alignRight()
}

/**
 * Given a variable number of [List]s, concatenates the [toString] value of each element with the corresponding element
 * of the next [List]s, values of each [List] left padded (right aligned) with respect to that [List]s values
 * @param lists [Array]`<out [List]<*>>` A variable number of [List]s to have their elements concatenated.
 *        All [lists] must be equal in size.
 * @return A new [List]`<String>` with the concatenated and aligned values.
 */
fun concatAlignRight(vararg lists: List<*>): List<String> {
    if (lists.isEmpty()) {
        return emptyList()
    }
    val result = lists[0].map { "" }
    // val total = listOf(1, 2, 3, 4, 5).fold(0, { total, next -> total + next })
    return lists.fold(result, {current, next -> current concatEach next.alignRight()})
}

/**
 * Concatenates all elements of the given [List], each elemente ended by a new line ([System.lineSeparator].
 * A [System.lineSeparator] is also added at the very end.
 * @receiver [List]`<*>`
 * @return String The [toString] values of each element, each on a new line.
 */
fun List<*>.toTextLines(): String = this.joinToString(separator = lineSeparator, postfix = lineSeparator)
