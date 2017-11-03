package pl.paweln.sudoku.algorithms;

import pl.paweln.sudoku.Sudoku;

/**
 * All algorithms comes from https://www.sudokuoftheday.com/techniques/
 * @author pawel
 *
 */
public interface SudokuAlgorithm {
	public boolean perform(Sudoku s);

	public void refreshCandidates(Sudoku s);
}
