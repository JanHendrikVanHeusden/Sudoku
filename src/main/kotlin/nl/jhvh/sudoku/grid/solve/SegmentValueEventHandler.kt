package nl.jhvh.sudoku.grid.solve

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

import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.segment.GridSegment

class SegmentValueEventHandler: SegmentValueEventHandlable {

    /**
     * Handle [SetCellValueEvent]s where the [SetCellValueEvent.eventSource] is a [CellValue] within the given [segment].
     * @throws IllegalStateException when the [SetCellValueEvent.eventSource] is not a [CellValue] within the [segment];
     *                               this would indicate an unexpected situation that we are not prepared to handle
     */
    @Throws(IllegalStateException::class)
    override fun handleSetCellValueEvent(valueEvent: SetCellValueEvent, segment: GridSegment) {
//        log().trace { "$valueEvent handled by $segment" }
//        check (segment.cells.contains(valueEvent.eventSource.cell)) {
//            "${SetCellValueEvent::class.simpleName} should be handled only by a ${GridSegment::class.simpleName}s containing the eventSource!" +
//                    " valueEvent=$valueEvent, segment=$segment" }
//        segment.cells.forEach {
//            if (it.cellValue is NonFixedValue) {
//                if (it.cellValue === valueEvent.eventSource) {
//                    it.cellValue.clearValueCandidates()
//                    it.cellValue.unsubscribe(segment, valueEvent.type)
//                } else {
//                    it.cellValue.removeValueCandidate(valueEvent.newValue)
//                }
//            }
//        }
    }

    override fun handleRemoveCandidatesEvent(valueEvent: CellRemoveCandidatesEvent, segment: GridSegment) {
//        log().trace { "$valueEvent handled by $segment" }
//        val eventSource = valueEvent.eventSource
//        val updatedValueCandidates = eventSource.getValueCandidates()
//        when (updatedValueCandidates.size) {
//            0 -> {
//                valueEvent.eventSource.unsubscribe(segment, valueEvent.type)
//                if (!eventSource.isSet) {
//                    throw GridNotSolvableException("Grid is not solvable: no candidate values anymore in $eventSource. valueEvent=$valueEvent")
//                }
//            }
//            1 -> {
//                // allow null because the valueCandidates might be cleared by a concurrent thread / coroutine
//                val intValue = updatedValueCandidates.firstOrNull()
//                if (intValue != null) {
//                    eventSource.setValue(intValue)
//                }
//            }
//            else -> {
//                // TODO("Not implemented yet")
//            }
//        }
    }

}