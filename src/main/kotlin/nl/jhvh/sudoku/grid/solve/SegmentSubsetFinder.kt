package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.model.segment.GridSegment

/**
 * Sudoku structure, base rules
 * * A [Block] is a square of `b * b` [Cell]s
 * * A [Grid] is a square of `n * n` [Cell]s, where `n = b * b`
 * * A [Grid] consists of [GridSegment]s:
 *    * [Row]s, [Col]s (aka columns) and [Block]s.
 * * Each [GridSegment] can contain numbers 1..n, each number at most one time
 * * Each **solved** [GridSegment] must contain all numbers `1..n` (inclusive)
 *
 * Rules that help to solve a Sudoku, derived from the structure & base rules above;
 * * Where the 3rd and 4th are extensions of the previous ones;
 *   assume that in a give [GridSegment] `n` [Cell]s have not been solved yet (values unknown yet).
 * 1. If a [Cell] of a [GridSegment] contains a certain number ([CellValue], solution),
 *    that number can be removed from the candidates of all other [Cell]s of the [GridSegment]
 * 2. If a [Cell] has only 1 candidate value, that candidate can be set as its [CellValue] (solution)
 * 3. If any combination of `p` [Cell]s (`1 < p < n`) within a [GridSegment] contain only a certain combination
 *    of `p` candidates, these candidates restrict the solution of these [Cell]s to these values;
 *    so these candidates can be removed from all other [Cell]s of this [GridSegment]
 * 4. If any combination of `q` candidates (`1 < q < n`) is present only in `q` [Cell]s of a [GridSegment],
 *    these restrict the solution of these [Cell]s to these values; so all other candidates can be removed from
 *    these [Cell]s within the [GridSegment] that contain all of these `q` [Cell]s.
 * 5. If any combination of `q` candidates (`1 < q < b`) is present only in those [Cell]s of a [Block] that are in
 *    a single [LinearSegment] ([Row] or [Col], these restrict the solution of the [Cell]s in the same [LinearSegment]
 *    that are not part of that [Block]: these candidates can not be present in any of those other [Cell]s, so these
 *    values can be removed from the candidates of the other [Cell]s in the linear segment.
 *
 * It seems to me that with these basic rules, any Sudoku should be solvable, because basically there are no more rules
 * in Sudoku than the structure rules (*) and the derived rules (1-5).
 *
 * Several less or more smart additional strategies exist, e.g.
 *  * Trial and error: try some possible solution, and if it proves wrong, go back to the situation that you know to be correct.
 *      * *Should not be necessary (at least I hope so - inefficient, and notoriously difficult to program this properly...)*
 *
 *  * Assume that the Sudoku has only 1 solution, and deduce that a certain partial solution (e.g. a certain value in a [Cell])
 *    can be eliminated.
 *      * Although this is a useful approach for Sudoku's that are known to be correct, we can not use it because this Sudoku solver may
 *        also be used to find that a given [Grid] is unsolvable (incorrect).
 *  * Some derived rules about certain patterns, e.g. *Swordfish*, *X-Wing*, *Jellyfish*, etc.
 *      * As far as I can see, these should never really be necessary...
 *        But let's see... Really fiendish Sudokus exist in the world!
 *
 * So at the time of writing, this Sudoku solver is aimed to rely on only the basic rules. Let's see!
 * TODO: verify the minimum number of fixed cells? See https://en.wikipedia.org/wiki/Mathematics_of_Sudoku#Minimum_number_of_givens
 */
@Suppress("KDocUnresolvedReference")
class SegmentSubsetFinder internal constructor(private val segment: GridSegment) {
    private val knownSubsets: Set<Set<Int>> = HashSet()

    /**
     * Remove all candidates that can be eliminated because of so called hidden candidate subsets (pairs, triples, ...).
     * * See [http://www.sudokuwiki.org/Hidden_Candidates](http://www.sudokuwiki.org/Hidden_Candidates);
     *   this implicitly handles so called naked candidate subsets
     *   ([http://www.sudokuwiki.org/Naked_Candidates](http://www.sudokuwiki.org/Naked_Candidates)) too.
     *
     * * A hidden pair, triple, quadruple, etc. means that a certain combination of 2, 3, 4, etc. candidates
     *   is present only in 2, 3, 4, etc. [Cell]s of a [GridSegment].
     * 
     * E.g. only 2 [Cell]s in a given [Row] (or [Block], [Col]) have 4 and 7 as candidates;
     * these [Cell]s form a hidden pair within the [GridSegment].
     *   * This means that all other alternatives than {4, 7} can be removed from that subset, say, [Cell]s #2 and #5) of that [Row].
     * 
     * But the alternatives that remain in the subset (values 4 and 7 in this example) can also be removed from
     * all other [Cell]s in the [GridSegment]s this subset is part of: the "naked candidates removal".
     */
    @Suppress("UNUSED_PARAMETER")
    fun resolveHiddenSubsets(level: Int) {
        TODO("Not implemented yet")
    }

}
