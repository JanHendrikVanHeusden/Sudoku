package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.format.element.RowFormatter
import nl.jhvh.sudoku.format.leftBorder
import nl.jhvh.sudoku.format.rightBorder
import nl.jhvh.sudoku.grid.model.segment.Row

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter row: Row (instead of element: Row)
class RowBoxFormatter: RowFormatter, BoxBorderingFormatter<Row> {

    private val cellFormatter = CellBoxFormatter()

    override fun format(row: Row): FormattableList {
        val topBorder =
                listOf(getTopLeftEdge(row)) concatEach getTopBorder(row) as List<String> concatEach listOf(getTopRightEdge(row))
        val valueWithLeftRightBorders =
                getLeftBorder(row) as List<String> concatEach nakedFormat(row) as List<String> concatEach getRightBorder(row) as List<String>
        val bottomBorder =
                listOf(getBottomLeftEdge(row)) concatEach getBottomBorder(row) as List<String> concatEach listOf(getBottomRightEdge(row))
        return FormattableList(topBorder + valueWithLeftRightBorders + bottomBorder)
    }

    override fun nakedFormat(row: Row): FormattableList {
        val leftCellsWithRightBorder = concatEach(*row.cellList.dropLast(1).map {
            cellFormatter.nakedFormat(it) concatEach cellFormatter.getRightBorder(it)
        }.toTypedArray())
        val rightCellNaked = cellFormatter.nakedFormat(row.cellList.last()) as List<String>
        return FormattableList(leftCellsWithRightBorder concatEach rightCellNaked)
    }

    override fun getLeftBorder(row: Row): FormattableList {
        val leftBorderChar = row.cellList.first().leftBorder()
        return FormattableList(nakedFormat(row).map { leftBorderChar.toString() })
    }

    override fun getRightBorder(row: Row): FormattableList {
        val rightBorderChar = row.cellList.first().rightBorder()
        return FormattableList(nakedFormat(row).map { rightBorderChar.toString() })
    }

    override fun getTopBorder(row: Row): FormattableList {
        val leftCellsTopBordersWithRightEdge = concatEach(*row.cellList.dropLast(1).map {
            cell -> cellFormatter.getTopBorder(cell).map { border -> border + cellFormatter.getTopRightEdge(cell)}
        }.toTypedArray())
        val rightCellTopBorder = cellFormatter.getTopBorder(row.cellList.last()) as List<String>
        return FormattableList(leftCellsTopBordersWithRightEdge concatEach rightCellTopBorder)
    }

    override fun getBottomBorder(row: Row): FormattableList {
        val leftCellsBottomBordersWithRightEdge = concatEach(*row.cellList.dropLast(1).map {
            cell -> cellFormatter.getBottomBorder(cell).map { border -> border + cellFormatter.getBottomRightEdge(cell)}
        }.toTypedArray())
        val rightCellBottomBorder = cellFormatter.getBottomBorder(row.cellList.last()) as List<String>
        return FormattableList(leftCellsBottomBordersWithRightEdge concatEach rightCellBottomBorder)
    }

    override fun getTopLeftEdge(row: Row): String = cellFormatter.getTopLeftEdge(row.cellList.first())

    override fun getTopRightEdge(row: Row): String = cellFormatter.getTopRightEdge(row.cellList.last())

    override fun getBottomLeftEdge(row: Row): String = cellFormatter.getBottomLeftEdge(row.cellList.first())

    override fun getBottomRightEdge(row: Row): String = cellFormatter.getBottomRightEdge(row.cellList.last())

}
