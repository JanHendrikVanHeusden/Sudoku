package nl.jhvh.sudoku.grid.event.cellvalue

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
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue

/**
 * [ValueEvent] to inform interested listeners of setting a [CellValue]'s value.
 * @property eventSource The [Cell] that is the source of this [SetCellValueEvent]
 * @property newValue The value to which the [Cell] is set
 */
class SetCellValueEvent (override val eventSource: CellValue, val newValue: Int) : ValueEvent {

    override val type: ValueEventType = SET_CELL_VALUE

    override fun toString(): String {
        return "${this.javaClass.simpleName}: (eventSource=$eventSource, newValue=$newValue)"
    }

}
