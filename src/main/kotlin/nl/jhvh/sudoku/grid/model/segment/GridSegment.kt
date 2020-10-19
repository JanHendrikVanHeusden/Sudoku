package nl.jhvh.sudoku.grid.model.segment

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

import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventListener
import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.util.log

/**
 * A [GridSegment] is an abstraction (super class) of the collections of [Cell]s that each, when solved,
 * contain all defined values of the Sudoku.
 *
 * Concrete subclasses are [Col] (column), [Row] and [Block].
 *
 * A functional synonym for [GridSegment] is **Group**.
 */
abstract class GridSegment (grid: Grid) : GridElement(grid), ValueEventListener {

    abstract val cells: Set<Cell>

    override fun onEvent(valueEvent: ValueEvent) {
        log().trace { "$this received event: $valueEvent" }
        if (!cells.contains(valueEvent.eventSource.cell)) {
            // not expected to be called, so warning:
            // it would mean we subscribed to events outside the segment
            log().warn { "Only events from ${CellValue::class.simpleName}s within the ${GridSegment::class.simpleName}" +
                    " should be handled by $this; event=$valueEvent" }
            return
        }
        when (valueEvent) {
            is SetCellValueEvent -> {
                grid.handleSetCellValueEvent(valueEvent, this)
            }
            is CellRemoveCandidatesEvent -> {
                grid.handleRemoveCandidatesEvent(valueEvent, this)
            }
            else -> {
                // not expected to be called, so warning:
                // it would mean we subscribed to an event type that we are not willing to handle
                log().warn { "Unhandled event type: ${valueEvent.javaClass.simpleName}; event=$valueEvent; " +
                        "${GridSegment::class.simpleName}=$this" }
            }
        }
    }

}

/** Abstraction for [GridSegment] subclasses [Col] (column) and [Row] */
abstract class LinearSegment(grid: Grid) : GridSegment(grid)

/** Abstraction for [GridSegment] subclass [Block] */
abstract class SquareSegment(grid: Grid) : GridSegment(grid)
