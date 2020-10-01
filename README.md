# This project
### Learning project
* This project started as a learning project for me personally, to sharpen my skills in Java and related stuff like unit testing & mocking (JUnit, Mockito), Maven, design patterns, etc.
* Later when learning Kotlin I converted it to Kotlin, aiming to learn and practice techniques specific for Kotlin, like the Kotlin native support for Delegation, which has become one of my favourites, but also coroutines, mockk, and all other nice stuff that makes Kotlin "the better Java" imho!

Because of it's learning goals I do not aim to provide the ultimate Sudoku solver. In fact, it probably will never be able to solve the hardest Sudoku's out there, simply because implementing all known solution algorithms, techniques and tricks out there takes more time to this aspect alone than I am willing to - there are other interesting things I'd like to do, especially with asynchronous parallel computation by using coroutines!

#### No 4 eyes principle
It's just my project. Not reviewed so far, and made by myself alone. So maybe I've overlooked some obvious ways to do thins, etc.; comments are welcome!

#### Current state
Currently, the solver is able to solve probably all Sudokus that you will see in newspapers, even those marked with 5 stars, and it was fun to program these algorithms / solving techniques.
But no, it still can't solve the really hard ones that you find on specialized Sudoku sites or in apps with really fiendish puzzles.

#### What will be next?
* Instead of focussing on solving algorithms, the next thing will be applying asynchronous solution executiong by using coroutines, I'm really excited by the possibilities Kotlin offers here.
  * Until now I have the feeling that even when applying asynchronous processing, there will be little or no need for explicit synchronization or guarding.
  Due to the nature of a Sudoku puzzle, you never will have to increment a value (well, maybe except some statistics in future, which probably can be AtomicIntegers etc.). All mutable lists, maps and sets already use thread safe implementations like ConcurrentHashMap etc., and changes on these will always go into the same direction (e.g. candidate values are removed only, never added; and if a value is set, it will never be set again). Of course, when applying  parallel processing, threads may see stale state, but as I grasp now, this will never be an invalid state.
  So I do not expect to need explicit synchronization or guarding.
  We will see!
* Maybe I will build a web frontend to it. Would really like that! Maybe using something like Angular or so. But no detailed plans for it yet, and zero experience in frontend developing!

### Design patterns
Quite some commmon design patterns have been applied in this project, not just to see if it works, but rather the other way around: the things I wanted to program just asked for it

Design patterns in this project include:
 * **Delegation** - very useful pattern and heavily used in the project! It's made so easy in Kotlin, if you compare it by the cumbersome and maintenance-sensitive way you have to do it in Java! Applause to the Kolin people!
 * **Factory** or some derivate of it
 * **Observable** - also out of the box with Kotlin, as a specific case of delegation.
 * **Visitor pattern** - not very commonly used, but this pattern appeared very well suited for formatting Sudoku grid and its building blocks (rows, columns, blocks, cells) it consists of, it nicely separates the formatting from the model.
 * **Decorator pattern** - to decorate (literally!) a formatted grid with row and column coordinates
 * **Event based** - helps to decouple the solving part from the puzzle itself.
 * **Lazy initialization**, also natively supported by Kotlin (in fact as a specific case of delegation). Uhmmm... maybe we should call it a technique rather than a design pattern.
    * In some cases lazy initialization is necessary because the data is simply not available yet when the object is constructed.
    E.g., grid and its substituents (row, column, block, cell) are highly coupled by nature, so GridElement, which is a supertype of row, column, block and cell, cannot reach grid properties during it's construction, and lazy initialization is simply necessary
    * In other cases, initialization is relatively costly, e.g. constructing maps and sets of properties for use by solving algorithms. Simple puzzles do not require these, so lazy initialization saves memory and computing power in these cases.

### Frameworks
For the basics of the Sudoku solver project, I decided not to use Spring Boot as the program did not require it. The same is true for JEE techniques like depencency injection. In some cases, like supplying a Sudoku formatter, I chose to use a factory like pattern & some static methods instead. If I ever feel I'd add CDI or Spring Boot, these can be replaced easily by @Inject, @Bean or @Component.

So instead of relying on Spring Boot, e.g. for asynchronous event handling, I will have to build it myself, which will help me to master parallelism & asynchronous programming with Kotlin coroutes 😊

### Libraries
I use some common libraries like Apache Commons StringUtils, and for logging slf4j (with log4j-2 as implementation). And for testing of asynchronous stuff: Awaitility.

Less common is the use of [combinatoricslib3](https://github.com/dpaukov/combinatoricslib3), for generating combinations.
* I prefer its use of Streams above [Guave's solution](https://guava.dev/releases/23.5-jre/api/docs/com/google/common/collect/Sets.html#combinations-java.util.Set-int-) which uses Sets (forcing to keep all combinations in memory at the same time).
* Another combination util is [Apache Commons combinationsIterator](https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/util/CombinatoricsUtils.html#combinationsIterator(int,%20int)), which features an iterator (so probably no need to keep all combinations in memory); however, this library accpts `int`s only, while [dpaukov's combinatoricslib3](https://github.com/dpaukov/combinatoricslib3) accepts any object type, which better suit my needs.

## External resources
### Sudoku difficulty rating
 * https://github.com/SudokuMonster/SukakuExplainer/wiki/Difficulty-ratings-in-Sudoku-Explainer-v1.2.1
 * Also worth reading: [An amateur's outlook on P=NP and Sudoku - Can each 9*9 Minimum Sudoku with exactly one solution be solved by logic?](http://english.log-it-ex.com/)
   * Contains reference to the hardest Suduku ever: <img src=http://www.sudokusnake.com/images/GoldenNugget.PNG width="100" height="100" title="Hardest known Sudoku: Golden Nugget" alt="Hardest known Sudoku: Golden Nugget">

###Testcases etc.
 * [Invalid testcases - Sudopedia](http://sudopedia.enjoysudoku.com/Invalid_Test_Cases.html)
 * [Valid testcases - Sudopedia](http://sudopedia.enjoysudoku.com/Valid_Test_Cases.html)
 * [Page with links to 100s of testcases ](https://warwick.ac.uk/fac/sci/moac/people/students/peter_cock/python/sudoku/)
 * [Benchmark data sets](https://github.com/t-dillon/tdoku/blob/master/benchmarks/README.md)
   > Links to various testcase sources, either known invalid or valid, with different difficulty levels from very easy to really really difficult ([Sudoku Explainer](https://github.com/SudokuMonster/SukakuExplainer/wiki/Difficulty-ratings-in-Sudoku-Explainer-v1.2.1) difficulty rating above 11.0) 

### Solving methods, challenges etc.
 * https://www.sudokuwiki.org/Sudoku.htm
   > Puzzles, solving methods, etc.
 * [Tdoku: A fast Sudoku Solver and Generator](https://github.com/t-dillon/tdoku)
   > Super fast Sudoku solver + generator + benchmarking
 * [Solving techniques on Sudoku Snake](http://www.sudokusnake.com/techniques.php)
