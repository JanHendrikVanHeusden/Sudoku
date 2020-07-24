package nl.jhvh.sudoku.format

/**
 * Interface to support formatting of Sudoku elements to a human readable format.
 *
 * The design patterns used is [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] method in this interface represents the 'accept' method of the classic Visitor Pattern.
 */
interface Formattable {

    /**
     * Output a Sudoku element (typically the [Grid], but may also be a [GridSegment] like a [Cell] or [Row]),
     * in a human readable or machine readable (e.g. HTML) way
     *
     * @param formatter The [SudokuFormatter] implementation to be accepted by the element to print.
     *  * The element to print will accept the [SudokuFormatter] as a delegate for the print formatting
     * @return The Sudoku element as a formatted [FormattableList]
     */
    fun format(formatter: SudokuFormatter): FormattableList

    /** @return The maximum length of the value in a cell.
     *   E.g. for a `3*3` block size, the maximum value is 9, so the length (number of digits) of that is 1.
     *   For a `4*4` block size, the maximum value is 16, so the length is 2. Etc.
     */
    val maxValueLength: Int


    /** [List]<[String]> with [toString] overridden for of formatting grid or grid elements */
    class FormattableList(collection: List<String> = emptyList()):
            List<String> by ArrayList<String>(collection) {

        /**
         * Produces desired results when formatting grid or grid elements
         * @return The content of each line, separated by [lineSeparator] = [System.lineSeparator]
         */
        override fun toString(): String {
            return this.joinToString(separator = lineSeparator)
        }

        /** [equals] based [List.equals] */
        override fun equals(other: Any?): Boolean {
            return this.toList().equals(other)
        }

        /** [hashCode] based [List.hashCode] */
        override fun hashCode(): Int {
            return this.toList().hashCode()
        }
    }

}
