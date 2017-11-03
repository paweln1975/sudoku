package pl.paweln.sudoku;

public class Pair implements Comparable<Pair> {
    private int firstValue;
    private int secondValue;

    public Pair(int firstValue, int secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public int getFirstValue() {
        return firstValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    @Override
    public int compareTo(Pair o) {
        final int oSum = o.firstValue + o.secondValue;
        final int cSum = this.firstValue + this.secondValue;
        return oSum > cSum ?  1 : (
                oSum < cSum ?  -1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair p = (Pair) obj;
            return p.firstValue == this.firstValue && p.secondValue == this.secondValue;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return new Integer(this.firstValue + this.secondValue).hashCode();
    }

    @Override
    public String toString() {
        return "{" + this.firstValue +"," + this.secondValue + "}";
    }
}
