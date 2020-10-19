package nl.jhvh.sudoku.format.boxformat.withcandidates

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

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.simple.SimpleCellValueFormatter
import nl.jhvh.sudoku.format.simple.fixedValueMarker
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.util.alignCenter
import org.apache.commons.lang3.StringUtils

const val setValueMarkerLeft: Char = '»'
const val setValueMarkerRight: Char = '«'

/**
 * Basic stateless [CellValue] formatter for textual output, typically to the console.
 * To be used in other (typically text based) formatters.
 */
class CellValueWithCandidatesFormatter: SimpleCellValueFormatter() {

    /**
     * A basic text format representation of the [cellValue], with unsolvedCells showing their candidate values.
     * > However, candidate values are not shown in all cases:
     * * Candidate values are only shown when each candidate value is a single digit; so only for grid sizes up to 9.
     *   For grid sizes larger than 9, the output of [SimpleCellValueFormatter.format] is returned (so without candidates)
     *   to avoid impractically high and wide outputs.
     * * For solved grids, no candidates are present, so in this case the method also returns the output of
     *   [SimpleCellValueFormatter.format], so without candidates (these are absent anyway)
     *   [setValueMarkerLeft], and `digit`.
     * @param cellValue [CellValue] to format
     * @return The formatted [CellValue] as a [FormattableList], with [Grid.blockSize] [String]s
     * > The resulting [String] may include one or more of the following characters: `space`, [fixedValueMarker],
     */
    override fun format(cellValue: CellValue): FormattableList {
        val grid: Grid = cellValue.grid
        if (grid.gridSize > 9 || grid.isSolved) {
            // Bigger grid sizes will become impractically high and wide, so just return super method (without candidates)
            // For solved grids, there are no candidates left, so makes no sense printing those (absent) candidates
            return super.format(cellValue)
        }

        val formattedLength = grid.blockSize * 2 + 1
        val formattedHeight = grid.blockSize
        val strList: MutableList<String> = ArrayList(formattedHeight)
        if (cellValue.isSet) {
            if (formattedHeight > 2) {
                strList.add("")
            }
            val valueWithMarker = if (cellValue.isFixed) "$fixedValueMarker${cellValue.value}" else "$setValueMarkerLeft ${cellValue.value} $setValueMarkerRight"
            strList.add(StringUtils.center(valueWithMarker, formattedLength))

            for (i in strList.size until formattedHeight) {
                strList.add("")
            }
        } else {
            val nonFixedValue = cellValue as NonFixedValue
            val allCandidatesPresent = nonFixedValue.getValueCandidates().size == grid.gridSize
            val strBuilder = StringBuilder(formattedLength)
            (0 until grid.blockSize).forEach { lineNr ->
                strBuilder.clear().append(' ')
                (0 until grid.blockSize).forEach { horPos ->
                    val candidateStr: String
                    if (allCandidatesPresent) {
                        // If no candidates have been removed yet, we will just show an empty cell
                        candidateStr = "  "
                    } else {
                        // Candidates have been removed, show the ones that are present yet
                        val value = horPos + grid.blockSize * lineNr + 1
                        candidateStr = if (!nonFixedValue.getValueCandidates().contains(value)) "  " else value.toString() + " "
                    }
                    strBuilder.append(candidateStr)
                }
                strList.add(StringUtils.center(strBuilder.toString(), formattedLength))
            }
        }
        return FormattableList(strList.alignCenter())
    }

}
