package pl.paweln.sudoku.algorithms;

import pl.paweln.sudoku.NumberItem;
import pl.paweln.sudoku.Square;
import pl.paweln.sudoku.Sudoku;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BaseSudokuAlgorithm implements SudokuAlgorithm {

	public abstract boolean perform(Sudoku p_sudoku);

	/**
	 * Method checks if NumberItem can have the given value
	 * Checks if the value already exists in the same square
	 * Checks if the value already exists in the same column
	 * Checks if the value already exists in the same row
	 * 
	 * @param n Number Item
	 * @param value value in range 1..9
	 */
	public boolean canHaveValue(Sudoku p_sudoku, NumberItem n, final int value) {
		boolean possible = true;
		boolean squareFound = false;
		Square s = null;

		// find the square
		for (int i = 0; i < p_sudoku.getSquares().length; i++) {
			s = p_sudoku.getSquare(i);
			for (int j = 0; j < s.getNumberItems().length; j++) {
				if (n.getGlobalRow() == s.getNumberItems()[j].getGlobalRow()
						&& n.getGlobalColumn() == s.getNumberItems()[j].getGlobalColumn()) {
					squareFound = true;
					break;
				}
			}
			if (squareFound) {
				break;
			}
		}

		if (!squareFound) throw new IllegalStateException("NumberItem could not be found in the squares.");

		if (s.containsNumber(value)) {
			possible = false;
		}

		// predicate to test if the NumberItem is equal to given value
		Predicate<NumberItem> equalToValue = (numberItem) -> numberItem.getValue() == value;

		if (possible) {
			// get numbers from column and check if contains the value by using stream
			List<NumberItem> list = p_sudoku.getColNumberItems(n.getGlobalColumn());
			possible = list.stream().noneMatch(equalToValue);
		}

		if (possible) {
			// get numbers from column and check if contains the value by using stream
			List<NumberItem> list = p_sudoku.getRowNumberItems(n.getGlobalRow());
			possible = list.stream().noneMatch(equalToValue);
		}

		return possible;
	}

	/**
	 * Finds the candidates for each NumberItem
	 * 
	 * @param p_sudoku Sudoku square
	 */
	public void refreshCandidates(Sudoku p_sudoku) {
		if (p_sudoku == null) {
			throw new IllegalArgumentException("Sudoku not initialized");
		}

		// main loop over each big square
		Square[] squareTab = p_sudoku.getSquares();
		for (Square s : squareTab) {
			// find missing items (setMissing), create collection of empty
			// spaces (zero items)
			Set<Integer> setMissing = s.getMissingNumbers();
			List<NumberItem> zeroNumbers = s.getEmptyNumberItems();

			// iterate over zero elements and iterate over missing and check if
			// missing number is candidate for that place
			if (setMissing.size() > 0) {
				for (NumberItem numberItem : zeroNumbers) {
					numberItem.clearCandidates();

					for (Integer missingNumber : setMissing) {
						if (this.canHaveValue(p_sudoku, numberItem, missingNumber)) {
							numberItem.addCandidate(missingNumber);
						}
					}
				}
			}
		}
	}


	/**
	 * Method remove candidates after adding a value for object of NumberItem
	 * @param p_sudoku Sudoku
	 * @param p_numberItem given NumberItem
	 */
	public void eliminateCandidatesForNumberItem(Sudoku p_sudoku, NumberItem p_numberItem) {
		final int value = p_numberItem.getValue();

		if (value < 1 || value > 9) {
			throw new IllegalArgumentException("Cannot eliminate cadidates for value:"+value);
		}

		p_numberItem.clearCandidates();

		// remove candidate value for given numberItem
		Consumer<NumberItem> consumerRemove = (numberItem) -> numberItem.removeCandidate(value);
		// test is number item has candidates
		Predicate<NumberItem> candidateExists = (numberItem) -> !numberItem.getCandidates().isEmpty();

		// remove candidates for given row
		List<NumberItem> list = p_sudoku.getRowNumberItems(p_numberItem.getGlobalRow());
		list.stream()
				.filter(candidateExists)
				.forEach(consumerRemove);

		// remove candidates for given col
		list = p_sudoku.getColNumberItems(p_numberItem.getGlobalColumn());
		list.stream()
				.filter(candidateExists)
				.forEach(consumerRemove);

		// test if the square is current square (the same row/col of number item)
		Predicate<Square> currentSquare = (square) -> {
			boolean found = false;
			Predicate<NumberItem> currentNumberItem = (numberItem)
					-> numberItem.getGlobalColumn() == p_numberItem.getGlobalColumn() &&
					numberItem.getGlobalRow() == p_numberItem.getGlobalRow();
			// found a square
			return Arrays.stream(square.getNumberItems())
					.filter(currentNumberItem).count() > 0;
		};

		// remove candidates for the same square
		Arrays.stream(p_sudoku.getSquares())
				.filter(currentSquare)
				.map(Square::getNumberItems)
				.flatMap(tab -> Arrays.stream(tab))
				.filter(candidateExists)
				.forEach(consumerRemove);

	}


}
