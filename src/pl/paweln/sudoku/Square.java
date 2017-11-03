package pl.paweln.sudoku;

import java.util.*;

/**
 * class represent a singe Square. Sudoku is composed of 9 Squares
 */
public class Square {
	private NumberItem[] itemTab;
	private final int row;
	private final int col;
	
	private final int squareNumber;
	
	public Square (int squareNumber) {
		this.itemTab = new NumberItem[9];
		this.squareNumber = squareNumber;
		for (int i = 0; i < itemTab.length; i++) {
			this.itemTab[i] = new NumberItem(squareNumber, i);
		}
		
		this.row = squareNumber / 3;
		this.col = squareNumber % 3;
	}
	
	private Square (int pSquareNumber, int pRow, int pCol) {
		this.row = pRow;
		this.col = pCol;
		this.squareNumber = pSquareNumber;
		this.itemTab = new NumberItem[9];
	}
	
	/**
	 * Make a copy of Square
	 * @param temp sqare to be copied
	 * @return deep copy of a square
	 */
	public static Square newInstance (Square temp) {
		Square s = new Square(temp.squareNumber, temp.row, temp.col);
		for (int i = 0; i < s.itemTab.length; i++) {
			s.itemTab[i] = NumberItem.newInstance(temp.getNumberItem(i));
		}
		return s;
	}
	
	/** 
	 * Method returns only filled NumberItems in the small square that still has some corresponding squares that
	 * do not contain that number - so they can be used for elimination algorithm
	 * @return list of number items
	 */
	public List<NumberItem> getFilledNumberItems() {
		List<NumberItem> list = new LinkedList<>();
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getValue() > 0) {
				list.add(anItemTab);
			}
		}
		return list;
	}
	
	/**
	 * Method return empty NumberItems
	 * @return list of empty number items
	 */
	public List<NumberItem> getEmptyNumberItems() {
		List<NumberItem> list = new LinkedList<>();
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getValue() == 0) {
				list.add(anItemTab);
			}
		}
		return list;
	}
	
	/**
	 * Method returns a set of missing numbers in the square
	 * @return set of missing numbers for the square
	 */
	public Set<Integer> getMissingNumbers() {
		Integer[] tab = new Integer[] {1,2,3,4,5,6,7,8,9};
		Set<Integer> setMissing = new HashSet<>(Arrays.asList(tab));
		
		List<NumberItem> filledNumbers = this.getFilledNumberItems();

		for (NumberItem nItem : filledNumbers) {
			setMissing.remove(nItem.getValue());
		}
		
		return setMissing;
	}
	
	public NumberItem getNumberItem(int i) {
		if (i < 0 || i > 8) {
			throw new IllegalArgumentException("Number item position out of range");
		}
		return this.itemTab[i];
	}
	
	public int getRowNumber() {
		return row;
	}

	public int getColNumber() {
		return col;
	}
	
	public int getRow(int value) {
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getValue() == value) {
				return anItemTab.getLocalRow();
			}
		}
		return -1;
	}
	
	public int getCol(int value) {
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getValue() == value) {
				return anItemTab.getLocalColumn();
			}
		}
		return -1;
	}
	
	public int getPosition(int value) {
		for (int i = 0; i < itemTab.length; i++) {
			if (itemTab[i].getValue() == value) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Return a string with a single row
	 * @param row row number in range 1 .. 3
	 * @return string printout of a row
	 */
	public String rowToString(int row) {
		if (row < 0 || row > 2) {
			throw new IllegalArgumentException("Row number out of range");
		}
		return this.rowToString(row, false);
	}

	/**
	 * Return a string with a single row
	 * @param row row number in range 1 .. 3
	 * @param withCandidates if true and value is unknown (0) the returns the candidate values in brackets []
	 * @return string printout of a row
	 */
	public String rowToString(int row, boolean withCandidates) {
		if (withCandidates) {
			return " " +
					(this.itemTab[3 * row].getValue() > 0 ? this.itemTab[3 * row].getValue() : this.itemTab[3 * row].toStringCandidates())
					+ " " +
					(this.itemTab[3 * row + 1].getValue() > 0 ? this.itemTab[3 * row + 1].getValue() : this.itemTab[3 * row + 1].toStringCandidates())
					+ " " +
					(this.itemTab[3 * row + 2].getValue() > 0 ? this.itemTab[3 * row + 2].getValue() : this.itemTab[3 * row + 2].toStringCandidates())
					+ " ";
		} else {
			return " " + this.itemTab[3 * row].getValue() + " " +this.itemTab[3 * row + 1].getValue()+ " " +this.itemTab[3 * row + 2].getValue() + " ";
		}
	}
	
	/**
	 * returns true if the square contains the value
	 * @param value tested value
	 * @return true if square contains the given value otherwise false
	 */
	public boolean containsNumber (int value) {
		boolean retValue = false;
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getValue() == value) {
				retValue = true;
				break;
			}
		}
		return retValue;
	}
	
	public NumberItem[] getNumberItems() {
		return this.itemTab;
	}
	
	public String toString() {
		return "Square:" + this.squareNumber;
	}
	
	public String toStringRowCol() {
		return this.rowToString(0) + "\n" + this.rowToString(1) + "\n" + this.rowToString(2);
	}
	
	/**
	 * Set -1 value for each item in the row
	 * @param pRow row number
	 */
	public void elimiateRow (int pRow) {
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getLocalRow() == pRow) {
				anItemTab.setValue(-1);
			}
		}
	}
	
	/**
	 * Set -1 value for each item in the column
	 * @param pCol col number
	 */
	public void elimiateCol (int pCol) {
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getLocalColumn() == pCol) {
				anItemTab.setValue(-1);
			}
		}
	}
	
	/**
	 * checks if only one empty position left
	 * @return true if one empty number item left in the square
	 */
	public boolean checkIfOnlyOnePositionLeft() {
		int zeroCounter = 0;
		for (NumberItem anItemTab : itemTab) {
			if (anItemTab.getValue() == 0) {
				zeroCounter++;
			}
		}

		return zeroCounter == 1;
	}

	/**
	 * Sets the value for given position
	 * @param pos position number in the range 0 .. 8
	 * @param value value in the range 1 .. 9
	 */
	public void setValue (int pos, int value) {
		if (pos < 0 || pos > 8) {
			throw new IllegalArgumentException("Position out of range");
		}
		
		if (value < -1 || value > 9) {
			throw new IllegalArgumentException("Value out of range");
		}
		// check if already exists
		if (value >= 1 && value <= 9) {
			for (NumberItem anItemTab : itemTab) {
				if (anItemTab.getValue() == value) {
					throw new IllegalStateException("Attempt to add duplicated value to square:" +
							this.toString() + " value:" + value);
				}
			}
		}
		this.itemTab[pos].setValue(value);
	}
	
	/**
	 * checks wheathe the square is completed that is contains all values from 1..9
	 * @return true if a square is full of numbers otherwise false
	 */
	public boolean isCompleted() {
		Integer[] tab = new Integer[] {1,2,3,4,5,6,7,8,9};
		Set<Integer> set = new HashSet<>(Arrays.asList(tab));

		for (NumberItem anItemTab : this.itemTab) {
			if (anItemTab.getValue() >= 1 && anItemTab.getValue() <= 9) {
				set.remove(anItemTab.getValue());
			}
		}
		
		return (set.size() == 0);
	}
}
