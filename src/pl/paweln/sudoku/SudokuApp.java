package pl.paweln.sudoku;

import pl.paweln.sudoku.loaders.SudokuFileReader;

/**
 * Main class to start Sudoku application
 * executes the sudoku solving algorithm
 * This is temporary executable for the program before writing a GUI
 */
public class SudokuApp {

	public static void main(String[] args) throws Exception {
		execute("medium2.txt");
	}
	
	
	
	private static void execute(String p_fileName) throws Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/" + p_fileName));
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
