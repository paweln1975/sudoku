package pl.paweln.sudoku;

import pl.paweln.sudoku.loaders.SudokuReader;

import java.util.*;

/**
 * Class represent the full sudoku table
 * @author pawel
 *
 */
public class Sudoku implements Comparable<Sudoku> {
	private Square[] squaresTab;
	
	public Sudoku() {
		init();
	}

	/**
	 * Creates a sudoku from file
	 * @param p_sudokuReader Sudoku Reader interface
	 * @throws Exception when File is not accessible or format is wrong
	 */
	public Sudoku(SudokuReader p_sudokuReader) throws Exception {
		init();
		
		int counter = 0;
		
		List<Integer> list = p_sudokuReader.getValues();
		for (Integer value : list) {
			int rowNum = counter / 9;
			int colNum = counter - rowNum * 9;

			int rowFactor = rowNum / 3;
			int square = colNum / 3 + rowFactor * 3;

			int normPosRow = rowNum % 3;
			int normPosCol = colNum % 3;

			int pos = normPosCol + normPosRow * 3;

			if (value > 0) {
				this.setValue(square, pos, value);
			}
			counter++;
		}
	}

	private void init() {
		this.squaresTab = new Square[9];
		for (int i = 0; i < squaresTab.length; i++) {
			this.squaresTab[i] = new Square(i);
		}
	}
	
	/**
	 * Creates a deep copy of full sudoku square
	 * @param p_sudoku Sudoku sqare
	 * @return new Sudoku table
	 */
	public static Sudoku newInstance(Sudoku p_sudoku) {
		Sudoku n = new Sudoku();
		for (int i = 0; i < n.squaresTab.length; i++) {
			n.squaresTab[i] = Square.newInstance(p_sudoku.getSquare(i));
		}
		return n;
	}

	/**
	 * Returns a square with 9 numbers in sudoku.
	 * @param i acceptable values are 0 .. 8
	 * @return Square table
	 */
	public Square getSquare(int i) {
		if (i < 0 || i > 8) {
			throw new IllegalArgumentException("Square position out of range");
		}
		return this.squaresTab[i];
	}
	
	public Square[] getSquares() {
		return this.squaresTab;
	}


	/**
	 * Set the value in a small box of sudoku
	 * @param squareNumber Square number in range 0 .. 8
	 * @param itemNumber Position number in range 0 .. 8
	 * @param value value in range 1 .. 9
	 */
	public void setValue(int squareNumber, int itemNumber, int value) {
		if (value < 1 || value > 9) {
			throw new IllegalArgumentException("Value out of range");
		}
		Square s = this.getSquare(squareNumber);
		s.setValue(itemNumber, value);
		
	}

	/**
	 * Returns a sudoku printout
	 * @return String representing a sudoku
	 */
	public String toString() {
		return this.toString(false);
	}

	public String toString(boolean withCandidates) {
		StringBuilder buff = new StringBuilder();
		for (int row = 0; row <= 2; row++)
		{
			for (int i = 0; i <= 2; i++) {
				buff.append(this.getSquare(i).rowToString(row, withCandidates));
			}
			buff.append("\n");
		}
		buff.append("\n");

		for (int row = 0; row <= 2; row++)
		{
			for (int i = 3; i <= 5; i++) {
				buff.append(this.getSquare(i).rowToString(row, withCandidates));
			}
			buff.append("\n");
		}
		buff.append("\n");

		for (int row = 0; row <= 2; row++)
		{
			for (int i = 6; i <= 8; i++) {
				buff.append(this.getSquare(i).rowToString(row, withCandidates));
			}
			buff.append("\n");
		}

		return buff.toString();
	}
	
	/**
	 * Returns list of square in the same row or column that contain a number or do not contain a number
	 * @param s related square
	 * @param n value of number
	 * @param withNumber flag determines if corresponding square must contain a number
	 * @return List object with correponding Squares
	 */
	public List<Square> getCorrespondingSquares(Square s, int n, boolean withNumber) {
		List<Square> list = new LinkedList<>();
		for (Square square : this.squaresTab) {
			boolean anotherRowSquare = (s.getColNumber() == square.getColNumber()
					&& s.getRowNumber() != square.getRowNumber());
			boolean anotherColSquere = (s.getColNumber() != square.getColNumber()
					&& s.getRowNumber() == square.getRowNumber());
			boolean containsNumber = square.containsNumber(n);
			if (anotherRowSquare || anotherColSquere) {
				if (withNumber && containsNumber) {
					list.add(square);
				}
				if (!withNumber && !containsNumber) {
					list.add(square);
				}
			}
		}
		return list;
	}

	@Override
	public int compareTo(Sudoku s) {
		return this.toString().compareTo(s.toString());
	}

	/**
	 * return a list with NumberItems in the given row
	 * @param row row number
	 * @return a list
	 */
	public List<NumberItem> getRowNumberItems(int row) {
		int squareRow = row / 3;

		List<NumberItem> list = Arrays.stream(this.squaresTab)
				.filter( (square) -> square.getRowNumber() == squareRow )
				.map(Square::getNumberItems)
				.flatMap ( numberItems -> Arrays.stream(numberItems))
				.filter( numberItem -> numberItem.getGlobalRow() == row)
				.collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

//		List<NumberItem> list = new LinkedList<>();
//		for (int i = 0; i < this.squaresTab.length; i++) {
//			Square s = this.squaresTab[i];
//			if (s.getRowNumber() == squareRow) {
//				for (int j = 0; j < 9; j++) {
//					NumberItem n = s.getNumberItem(j);
//					if (n.getGlobalRow() == row) {
//						list.add(n);
//					}
//				}
//			}
//		}
		return list;
	}
	
	public List<NumberItem> getColNumberItems(int col) {
		int squareCol = col / 3;

        List<NumberItem> list = Arrays.stream(this.squaresTab)
                .filter( (square) -> square.getColNumber() == squareCol )
                .map(Square::getNumberItems)
                .flatMap ( numberItems -> Arrays.stream(numberItems))
                .filter( numberItem -> numberItem.getGlobalColumn() == col)
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

//		List<NumberItem> list = new LinkedList<>();
//		for (int i = 0; i < this.squaresTab.length; i++) {
//			Square s = this.squaresTab[i];
//			if (s.getColNumber() == squareCol) {
//				for (int j = 0; j < 9; j++) {
//					NumberItem n = s.getNumberItem(j);
//					if (n.getGlobalColumn() == col) {
//						list.add(n);
//					}
//				}
//			}
//		}
		return list;
	}

	/**
	 * Check is sudoku is solved
	 * @return true if sudoku is done otherwise false
	 */
	public boolean isCompleted() {
		// check if all squares are completed
		boolean completed = true;
		for (int i = 0; i < getSquares().length; i++) {
			if (!getSquare(i).isCompleted()) {
				completed = false;
				break;
			}
		}

		if (completed) {
			for (int i = 0; i < 9; i++) {
				if (!this.isRowColCompleted(i)) {
					completed = false;
					break;
				}
			}
		}
		return completed;
	}

	public boolean isRowColCompleted(int numRowCol) {
		List<NumberItem> list = this.getRowNumberItems(numRowCol);
		boolean retValue = this.checkListCompletness(list);

		if (retValue) {
			list = this.getColNumberItems(numRowCol);
			retValue = this.checkListCompletness(list);
		}

		return retValue;
	}



	private boolean checkListCompletness(List<NumberItem> list) {
		Integer[] tab = new Integer[] {1,2,3,4,5,6,7,8,9};
		Set<Integer> set = new HashSet<>(Arrays.asList(tab));

		for (NumberItem n : list) {
			if (n.getValue() >= 1 && n.getValue() <= 9) {
				set.remove(n.getValue());
			}
		}
		return (set.size() == 0);
	}

}
