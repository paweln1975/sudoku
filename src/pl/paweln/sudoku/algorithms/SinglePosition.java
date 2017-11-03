package pl.paweln.sudoku.algorithms;

import pl.paweln.sudoku.NumberItem;
import pl.paweln.sudoku.Square;
import pl.paweln.sudoku.Sudoku;

import java.util.*;

public class SinglePosition extends BaseSudokuAlgorithm {

    @Override
    public boolean perform(Sudoku p_sudoku) {
        if (p_sudoku == null) {
            throw new IllegalArgumentException("Sudoku not initialized");
        }
        return
                this.singleSquarePosition(p_sudoku) || this.singleRowPosition(p_sudoku);

    }

    /**
     * This method performs the algoritm with elimination for squares. For each square get the missing numbers.
     * For each missing number takes the corresponding square which are used for eliminations rows/cols with the
     * analysed item. If only one place left to the current number it is added to the square.
     * @param p_sudoku Sudoku
     * @return true if a new value has been found / false if algorithm failed
     */
    public boolean singleSquarePosition(Sudoku p_sudoku) {
        if (p_sudoku == null) {
            throw new IllegalArgumentException("Sudoku not initialized");
        }

        boolean valueFound = false;
        Square[] squareTab = p_sudoku.getSquares();
        for (Square s : squareTab) {
            // get filled numbers, iterate over them
            List<NumberItem> filledNumbers = s.getFilledNumberItems();

            if (filledNumbers.size() > 0) {
                for (NumberItem numberItem : filledNumbers) {
                    if (numberItem.canBeUsedForElimination()) {
                        // find corresponding squares for each number that does not contain the number
                        // iterate over corresponding squares
                        List<Square> correspondingSquaresWithoutNumber = p_sudoku.getCorrespondingSquares(s,
                                numberItem.getValue(), false);
                        // if the list is empty it means that no more elimination could be done with this number
                        if (correspondingSquaresWithoutNumber.size() > 0) {
                            // iterate over squares that do not contain the number
                            // for each find corresponding squares that contain that number
                            for (Square squareAnalysed : correspondingSquaresWithoutNumber) {
                                List<Square> correspondingSquaresWithNumber = p_sudoku
                                        .getCorrespondingSquares(squareAnalysed, numberItem.getValue(), true);

                                if (correspondingSquaresWithNumber.size() > 0) {
                                    // make a clone of Square s and use the correspondingSquaresWithNumber to
                                    // eliminate the places where this number cannot exist, put the -1 value for each row/col
                                    // where value from the corresponding square make elimination finally only one place with 0 should left
                                    // and this is the possible place for numberItem

                                    Square squareAnalysedCopy = Square.newInstance(squareAnalysed);

                                    for (Square squareNumber : correspondingSquaresWithNumber) {
                                        if (squareAnalysedCopy.getColNumber() == squareNumber.getColNumber()) {
                                            int elimCol = squareNumber.getCol(numberItem.getValue());
                                            if (elimCol > -1) {
                                                squareAnalysedCopy.elimiateCol(elimCol);
                                            }
                                        } else if (squareAnalysedCopy.getRowNumber() == squareNumber.getRowNumber()) {
                                            int elimRow = squareNumber.getRow(numberItem.getValue());
                                            if (elimRow > -1) {
                                                squareAnalysedCopy.elimiateRow(elimRow);
                                            }
                                        }

                                    }

                                    boolean success = squareAnalysedCopy.checkIfOnlyOnePositionLeft();
                                    if (success) {
                                        int pos = squareAnalysedCopy.getPosition(0);
                                        squareAnalysed.setValue(pos, numberItem.getValue());

                                        super.eliminateCandidatesForNumberItem(p_sudoku, squareAnalysed.getNumberItem(pos));

                                        valueFound = true;
                                    }
                                }
                            }
                        } else {
                            numberItem.setEliminationFlag(false);
                        }
                    }
                }

            }

        }

        return valueFound;
    }

    /**
     * This method performs elimination for each rows. Check if missing number could be placed only once in empty box
     * by comparing with values in the square, row and column.
     * @param p_sudoku Suboku object
     * @return true if a new value has been found / false if algorithm failed
     */
    public boolean singleRowPosition(Sudoku p_sudoku) {
        if (p_sudoku == null) {
            throw new IllegalArgumentException("Sudoku not initialized");
        }

        boolean valueFound = false;
        // main loop over each row
        // TODO optimization possible - skip the completed rows
        for (int i = 0; i < 9; i++) {
            List<NumberItem> rowNumberItems = p_sudoku.getRowNumberItems(i);

            Integer[] tab = new Integer[] {1,2,3,4,5,6,7,8,9};
            Set<Integer> setMissing = new HashSet<>(Arrays.asList(tab));

            List<NumberItem> zeroNumbers = new LinkedList<>();

            // find empty items and missing numbers in the current row
            for (NumberItem nItem : rowNumberItems) {
                if (nItem.getValue() == 0) {
                    zeroNumbers.add(nItem);
                } else {
                    setMissing.remove(nItem.getValue());
                }
            }

            // still some elements are missing so iterate over them and checks in each empty NumberItem
            // checks if the missing value could be there by using the method canHaveValue (checking occurence of the
            // value in a square, row and col
            if (setMissing.size() > 0) {
                // find number of possible places for each missing number among this zeroNumbers
                for (Integer valueInt : setMissing) {
                    int countPossible = 0;
                    for (NumberItem numberItem : zeroNumbers) {
                        if (super.canHaveValue(p_sudoku, numberItem, valueInt)) {
                            countPossible++;
                        }
                    }
                    if (countPossible == 1) {
                        for (NumberItem numberItem : zeroNumbers) {
                            if (super.canHaveValue(p_sudoku, numberItem, valueInt)) {
                                numberItem.setValue(valueInt);
                                super.eliminateCandidatesForNumberItem(p_sudoku, numberItem);

                                valueFound = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return valueFound;
    }
}
