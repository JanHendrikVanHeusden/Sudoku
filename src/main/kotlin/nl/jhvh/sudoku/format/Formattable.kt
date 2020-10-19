package nl.jhvh.sudoku.format

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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

    /** The length of the highest possible value in a cell; depending on the grid size. E.g.
     *   * `3*3` block size (`9*9` grid): highest possible value is 9 => max length = 1
     *   * `4*4` block size (`16*16` grid): highest possible value is 16 => max length is 2
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
            return this.toList() == other
        }

        /** [hashCode] based [List.hashCode] */
        override fun hashCode(): Int {
            return this.toList().hashCode()
        }
    }

}
