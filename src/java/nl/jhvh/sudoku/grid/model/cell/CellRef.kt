package nl.jhvh.sudoku.grid.model.cell

/**
 * A [CellRef] can be constructed (and represented) by a combination of:
 *  * digits (usually a single digit) for the row number of a [Cell],
 *  * letters (usually a single letter) for the column indicator of a [Cell].
 * E.g. `2C` would indicate a [Cell] on the intercept of the 2nd row ([Row]) with the 3th column ([Col])
 * @param x The internal, technical representation of the [Col]'s sequence number; zero based.
 *          So the 5th column (indicated by "`E`") would correspond with [x]-value **`4`**.
 * @param x The internal, technical representation of the [Row]'s sequence number; zero based.
 *          So the 2nd column (indicated by "`2`") would correspond with [y]-value **`1`**.
 */
data class CellRef(val x: Int, val y: Int) {
    val rowRef: String
    val colRef: String
    val cellRef: String

    constructor(cellRef: String) : this(getRowFromCellRef(cellRef), getColFromCellRef(cellRef)) {}
    constructor(rowRef: String, colRef: String) : this(colRefToIndex(colRef.trim { it <= ' ' }.toUpperCase()),
            rowRefToIndex(rowRef.trim { it <= ' ' }.toUpperCase())) {}

    private val theHashCode = (31 + x + y) * 31

    /** [hashCode] based on [x] and [y] only  */
    override fun hashCode(): Int = theHashCode

    /** [equals] based on [x] and [y] only */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val cellRef = other as CellRef
        if (x != cellRef.x) {
            return false
        }
        return (y == cellRef.y)
    }

    init {
        rowRef = indexToRowRef(y)
        colRef = indexToColRef(x)
        cellRef = rowRef + colRef
    }
}
