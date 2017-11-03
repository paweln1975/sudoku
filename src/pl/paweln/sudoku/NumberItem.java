package pl.paweln.sudoku;

import java.util.Set;
import java.util.TreeSet;

public class NumberItem {
	private int value = 0;
	
	private final int localRow;
	private final int localColumn;

	private final int globalRow;
	private final int globalColumn;
	
	private boolean canBeUsedForElimination = true;
	
	private final Set<Integer> candidates = new TreeSet<>();
	
	protected NumberItem (int squareNumber, int itemNumber) {
		int squareRow;
		int squareColumn;

		this.localRow = itemNumber / 3;
		this.localColumn = itemNumber % 3;

		squareRow = squareNumber / 3;
		squareColumn = squareNumber % 3;
		this.globalRow = squareRow * 3 + this.localRow;
		this.globalColumn = squareColumn * 3 + this.localColumn;

	}
	
	private NumberItem (int pValue, int plocalRow, int plocalColumn, int pGlobalRow, int pGlobalColumn) {
		this.value = pValue;
		this.localRow = plocalRow;
		this.localColumn = plocalColumn;
		this.globalRow = pGlobalRow;
		this.globalColumn = pGlobalColumn;
	}
	
	/**
	 * Make a copy of NumberItem
	 */
	protected static NumberItem newInstance(NumberItem item) {
		NumberItem n = new NumberItem(item.getValue(), item.getLocalRow(), item.getLocalColumn(), 
				item.getGlobalRow(), item.getGlobalColumn());
		n.setEliminationFlag(item.canBeUsedForElimination());
		return n;
	}
	
	public int getLocalRow() {
		return this.localRow;
	}
	
	public int getValue() {
		return value;
	}

	public int getLocalColumn() {
		return localColumn;
	}

	public int getGlobalRow() {
		return globalRow;
	}

	public int getGlobalColumn() {
		return globalColumn;
	}
	
	public void setValue(int value) {
		if (value < -1 || value > 9) {
			throw new IllegalArgumentException("Value out of range");
		}
		this.value = value;
	}
	
	public String toString() {
		return String.valueOf(this.getValue());
	}
	
	public boolean canBeUsedForElimination() {
		return this.canBeUsedForElimination;
	}
	
	public void setEliminationFlag (boolean value) {
		this.canBeUsedForElimination = value;
	}
	
	public void clearCandidates() {
		this.candidates.clear();
	}
	
	public void addCandidate(int value) {
		if (!this.candidates.contains(value)) this.candidates.add(value);
	}

	/**
	 * Removes candidate from NumberItem
	 * @param value value to be removed
	 * @return true if value existed and has been removed otherwise false
	 */
	public boolean removeCandidate(int value) {
		if (this.candidates.contains(value)) {
			this.candidates.remove(value);
			return true;
		}
		return false;
	}
	
	public Set<Integer> getCandidates() {
		return this.candidates;
	}

	public String toStringCandidates() {
		return this.candidates.toString();
	}
}
