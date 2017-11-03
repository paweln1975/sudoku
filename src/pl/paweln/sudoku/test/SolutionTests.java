package pl.paweln.sudoku.test;

import org.junit.Test;
import pl.paweln.sudoku.Sudoku;
import pl.paweln.sudoku.SudokuSolver;
import pl.paweln.sudoku.loaders.SudokuFileReader;

import static org.junit.Assert.assertEquals;

public class SolutionTests {
    @Test
    public void testSolutionSimple1() throws Exception {
        testSolution("simple1.txt", "simple1solution.txt");
    }

    @Test
    public void testSolutionMedium2() throws Exception {
        testSolution("medium2.txt", "medium2solution.txt");
    }

    private static void testSolution (String inputFileName, String solutionFileName) throws Exception {
        Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/" + inputFileName));
        Sudoku solution = new Sudoku(new SudokuFileReader("./res/" + solutionFileName));

        SudokuSolver solver = new SudokuSolver(sudoku);
        solver.solve();

        assertEquals ("Test sudoku solution " + inputFileName, sudoku.toString(), solution.toString());
    }
}
