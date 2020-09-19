package nl.jhvh.sudoku.grid.solve

import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.NOT_STARTED
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.SOLVE_SINGULAR_VALUES
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

/**
 * Initialization for [GridSolver] grid mocks is computationally & performance wise relatively heavy.
 * This class contains tests of [GridSolver] that do not require grid initialization.
 * Basic tests that do require such initialization are in class [GridSolverTest].
 */
internal class GridSolverBasicTests {

    private lateinit var subject: GridSolver

    @BeforeEach
    fun setUp() {
        subject = GridSolver()
    }

    @Test
    fun `GridSolver constructor`() {
        // given
        val spiedSubject = spyk(subject)
        // when, then
        assertThat(spiedSubject.gridToSolve).isNull()
        verify { spiedSubject.gridToSolve }
        // verify that the constructor did not call any other stuff
        // especially lazy initialization of some variables should not happen
        confirmVerified(spiedSubject)
    }

    @Test
    fun isSolved() {
        // given
        val subject = object : GridSolver() {
            fun setPhase(phase: GridSolvingPhase) {
                this.solvingPhase = phase
            }
        }
        assertThat(subject.solvingPhase).isEqualTo(NOT_STARTED)
        assertThat(subject.solvingPhase.isSolving).isFalse()
        // when, then
        assertThat(subject.isSolving).isFalse()

        // given
        subject.setPhase(SOLVE_SINGULAR_VALUES)
        assertThat(subject.solvingPhase).isEqualTo(SOLVE_SINGULAR_VALUES)
        assertThat(subject.solvingPhase.isSolving).isTrue()
        // when, then
        assertThat(subject.isSolving).isTrue()
    }

    @Test
    fun `set Grid to null should fail`() {
        val spiedSubject = spyk(subject)
        // given, when, then
        assertFailsWith<IllegalArgumentException> { spiedSubject.gridToSolve = null }
        verify { spiedSubject setProperty "gridToSolve" value null }
        // verify no more calls were made
        confirmVerified()
    }

    @Test
    fun `set Grid to another value should fail`() {
        // given
        val spiedSubject = spyk(subject)
        val grid1: Grid = mockk(relaxed = true)
        val grid2: Grid = mockk(relaxed = true)

        spiedSubject.gridToSolve = grid1
        assertThat(spiedSubject.gridToSolve === grid1).isTrue()
        clearMocks(spiedSubject, answers = false, recordedCalls = true, exclusionRules = false)

        // when, then
        assertFailsWith<IllegalStateException> { spiedSubject.gridToSolve = grid2 }
        verify { spiedSubject setProperty "gridToSolve" value grid2 }
        // verify no more calls were made
        confirmVerified(spiedSubject)
    }

    /**
     * [gridToSolvePropertyName] is used in log messages & error messages.
     * So we test correctness of the name to avoid confusing messages (in case of refactoring etc.)
     */
    @Test
    fun `check that gridToSolvePropertyName holds the correct property name`() {
        val gridSolver = GridSolver()
        val getterName = "get" + gridToSolvePropertyName.capitalize()
        // would fail with NoSuchMethodException if the property does not exist
        gridSolver.javaClass.getMethod(getterName)
    }

}
