package pl.paweln.sudoku;

import pl.paweln.sudoku.algorithms.*;

public class SudokuSolver {
	private Sudoku sudoku;


	SudokuAlgorithm singlePosition;
	SudokuAlgorithm singleCandidate;
	SudokuAlgorithm candidateLine;
	SudokuAlgorithm nakedPairs;

	public SudokuSolver(Sudoku p_sudoku) {
		if (p_sudoku == null) {
			throw new IllegalArgumentException("Sudoku cannot be initialized with null");
		}
		this.sudoku = p_sudoku;
		this.singlePosition = new SinglePosition();
		this.singleCandidate = new SingleCandidate();
		this.candidateLine = new CandidateLine();
		this.nakedPairs = new NakedPairs();
	}


	/**
	 * method tries to solve sudoku
	 * it start with very basic elimination algorithm - Single Position
	 * whenever any change occurs then it start the algorithm again from the beginning
	 * if
	 * @return true if the sudoku has been solved, false otherwise
	 */
	public boolean solve() {

		if (this.sudoku == null) {
			throw new IllegalArgumentException("Sudoku not initialized");
		}


		boolean wasAnyChange = true;
		boolean solved = false;

		this.singlePosition.refreshCandidates(this.sudoku);

		while (wasAnyChange && !solved) {
			wasAnyChange = this.singlePosition.perform(this.sudoku);

			if (!wasAnyChange) {
				wasAnyChange = this.singleCandidate.perform(this.sudoku);
			}

			if (!wasAnyChange) {
				wasAnyChange = this.candidateLine.perform(this.sudoku);
				if (wasAnyChange)
					this.singleCandidate.perform(this.sudoku);
			}

			if (!wasAnyChange) {
				wasAnyChange = this.nakedPairs.perform(this.sudoku);
				if (wasAnyChange)
					this.singleCandidate.perform(this.sudoku);
			}

			solved = this.sudoku.isCompleted();
		}

		return solved;
	}
	
	

}
