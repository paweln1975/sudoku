package pl.paweln.sudoku.algorithms;

import pl.paweln.sudoku.NumberItem;
import pl.paweln.sudoku.Square;
import pl.paweln.sudoku.Sudoku;

import java.util.List;
import java.util.Set;

public class SingleCandidate extends BaseSudokuAlgorithm {

	@Override
	public boolean perform(Sudoku p_sudoku) {
		if (p_sudoku == null) {
			throw new IllegalArgumentException("Sudoku not initialized");
		}

		return
			this.setValuesForSingleCandidates(p_sudoku) ||
				this.setValuesForCandidatesExistingOnce(p_sudoku);

	}

	/**
	 * For each empty number the method check if the last candidate left in the square
	 * @param p_sudoku Sudoku
	 * @return true if method has found this candidation, false otherwise
	 */
	private boolean setValuesForSingleCandidates(Sudoku p_sudoku) {
		boolean valueFound = false;

		if (p_sudoku == null) {
			throw new IllegalArgumentException("Sudoku not initialized");
		}
		// main loop over each big square
		Square[] squareTab = p_sudoku.getSquares();
		for (Square s : squareTab) {
			// loop over empty items
			List<NumberItem> empty = s.getEmptyNumberItems();
			for (NumberItem numberItem : empty) {

				// get candidates and check if only one exists
				Set<Integer> candidates = numberItem.getCandidates();
				if (candidates.size() == 1) {

					for (Integer value : candidates) {
						numberItem.setValue(value);
						super.eliminateCandidatesForNumberItem(p_sudoku, numberItem);
						valueFound = true;
					}

					numberItem.clearCandidates();
				}
			}
		}
		return valueFound;
	}

	/**
	 * The method checks if there is only one candidate exists among other cancidates
	 * @param p_sudoku Sudoku
	 * @return true if method has found this candidation, false otherwise
	 */
	private boolean setValuesForCandidatesExistingOnce(Sudoku p_sudoku) {
		boolean valueFound = false;

		if (p_sudoku == null) {
			throw new IllegalArgumentException("Sudoku not initialized");
		}

		// main loop over each big square
		Square[] squareTab = p_sudoku.getSquares();
		for (Square s : squareTab) {
			// loop over empty items

			Set<Integer> setMissing = s.getMissingNumbers();
			List<NumberItem> empty = s.getEmptyNumberItems();


			// iterate over missing numbers
			for (Integer missingInt : setMissing) {
				int count = 0;
				// iterate over empty numbers
				for (NumberItem numberItem : empty) {
					// get candidates and check if only one exists
					Set<Integer> candidates = numberItem.getCandidates();
					if (candidates.contains(missingInt)) {
						count++;
					}
				}
				if (count == 1) {
					for (NumberItem numberItem : empty) {
						Set<Integer> candidates = numberItem.getCandidates();
						if (candidates.contains(missingInt)) {
							numberItem.setValue(missingInt);
							super.eliminateCandidatesForNumberItem(p_sudoku, numberItem);
							valueFound = true;
							break;
						}
					}
				}
			}
		}
		return valueFound;
	}
}
