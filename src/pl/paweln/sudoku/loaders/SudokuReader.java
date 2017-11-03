package pl.paweln.sudoku.loaders;

import java.util.List;

public interface SudokuReader {
	List<Integer> getValues() throws Exception;
}
