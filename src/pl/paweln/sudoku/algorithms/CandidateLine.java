package pl.paweln.sudoku.algorithms;

import java.util.List;
import java.util.Set;

import pl.paweln.sudoku.Sudoku;
import pl.paweln.sudoku.NumberItem;
import pl.paweln.sudoku.Square;

public class CandidateLine extends BaseSudokuAlgorithm {

	@Override
	public boolean perform(Sudoku p_sudoku) {
        boolean candidateRemoved = false;

		if (p_sudoku == null) {
			throw new IllegalArgumentException("Sudoku not initialized");
		}

		// iterate over square and detects if among each the same candidates values form a line
		// if yes then eliminate from the same row or column of corresponding squares these candidate values
		
		// main loop over each big square
		Square[] squareTab = p_sudoku.getSquares();
        for (Square square : squareTab) {


            // get empty numbers, iterate over them
            Set<Integer> setMissing = square.getMissingNumbers();
            for (Integer missingNumber : setMissing) {

                int count = 0;
                int row = -1;
                int col = -1;
                int rowChange = 0;
                int colChange = 0;

                // get empty spaces - find there potential candidates
                List<NumberItem> emptyNumberItems = square.getEmptyNumberItems();

                for (NumberItem numberItem : emptyNumberItems) {
                    if (numberItem.getCandidates().contains(missingNumber)) {
                        count++;
                        if (count == 1) {
                            row = numberItem.getGlobalRow();
                            col = numberItem.getGlobalColumn();
                        } else {
                            if (row != numberItem.getGlobalRow()) {
                                rowChange++;
                            }
                            if (col != numberItem.getGlobalColumn()) {
                                colChange++;
                            }
                        }
                    }
                }

                boolean rowCandidateLine = (count > 1 && rowChange == 0);
                boolean colCandidateLine = (count > 1 && colChange == 0);

                List<Square> list = p_sudoku.getCorrespondingSquares(square, missingNumber, false);

                for (Square correspondigSquare : list) {
                    List<NumberItem> emptyCorrNumberItems = correspondigSquare.getEmptyNumberItems();
                    for (NumberItem numberItem : emptyCorrNumberItems) {
                        if (rowCandidateLine
                                && numberItem.getGlobalRow() == row
                                && numberItem.getCandidates().contains(missingNumber)
                                ) {
                            numberItem.getCandidates().remove(missingNumber);
                            candidateRemoved = true;
                        }

                        if (colCandidateLine
                                && numberItem.getGlobalColumn() == col
                                && numberItem.getCandidates().contains(missingNumber)
                                ) {
                            numberItem.getCandidates().remove(missingNumber);
                            candidateRemoved = true;
                        }
                    }
                }
            }

        }
		return candidateRemoved;
	}

}
