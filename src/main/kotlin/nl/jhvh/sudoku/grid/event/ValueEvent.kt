package nl.jhvh.sudoku.grid.event

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

import nl.jhvh.sudoku.grid.model.cell.CellValue

/** Base interface for events of interest with regard to [CellValue]s, e.g. setting values, removing candidates, etc. */
interface ValueEvent {

    val eventSource: CellValue

    val type: ValueEventType

    override fun toString(): String
}