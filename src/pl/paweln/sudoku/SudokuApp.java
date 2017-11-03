package pl.paweln.sudoku;

import pl.paweln.sudoku.loaders.SudokuFileReader;
/**
 *
 */
public class SudokuApp {

	public static void main(String[] args) throws Exception {
		execute("./res/medium2.txt");
	}
	
	
	
	private static void execute(String fileName) throws Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader(fileName));
		System.out.println("\nSudoku loaded from file:\n" + sudoku);
		
		SudokuSolver solver = new SudokuSolver(sudoku);
		boolean solved = solver.solve();

		if (solved) {
			System.out.println("\nSudoku solution:\n" + sudoku);
		} else {
			System.out.println("\nCurrent sudoku:\n" + sudoku);
			System.out.println("\nCurrent sudoku with candidates:\n" + sudoku.toString(true));
		}
	}

}
