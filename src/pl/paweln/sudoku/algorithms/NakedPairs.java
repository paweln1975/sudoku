package pl.paweln.sudoku.algorithms;

import pl.paweln.sudoku.NumberItem;
import pl.paweln.sudoku.Square;
import pl.paweln.sudoku.Sudoku;
import pl.paweln.sudoku.Pair;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NakedPairs extends BaseSudokuAlgorithm {

    @Override
    public boolean perform(Sudoku p_sudoku) {
        boolean candidatesRemoved = false;
        // iterate over squares
        for (Square square : p_sudoku.getSquares()) {
            Set<Pair> nakedPairs = this.findNakedPairs(square.getNumberItems());
            for (Pair pair: nakedPairs) {
                if (removeCandidates(square.getNumberItems(), pair)) candidatesRemoved = true;
            }
        }

        // iterate over rows
        for (int i = 0; i < 9; i++) {
            NumberItem[] numberItemsTab = p_sudoku.getRowNumberItems(i).toArray(new NumberItem[9]);
            Set<Pair> nakedPairs = this.findNakedPairs(numberItemsTab);
            for (Pair pair: nakedPairs) {
                if (removeCandidates(numberItemsTab, pair)) candidatesRemoved = true;
            }
        }

        // iterate over cols
        for (int i = 0; i < 9; i++) {
            NumberItem[] numberItemsTab = p_sudoku.getColNumberItems(i).toArray(new NumberItem[9]);
            Set<Pair> nakedPairs = this.findNakedPairs(numberItemsTab);
            for (Pair pair: nakedPairs) {
                if (removeCandidates(numberItemsTab, pair)) candidatesRemoved = true;
            }
        }
        return candidatesRemoved;
    }

    public Set<Pair> findNakedPairs(NumberItem[] p_numberItemTab) {
        Predicate<NumberItem> numberItemWithTwoCandidates = (numberItem) -> numberItem.getCandidates().size() == 2;
        // test if there are any pairs
        Set<Pair> pairSet = Arrays.stream(p_numberItemTab)
                .filter(numberItemWithTwoCandidates)
                .map(NakedPairs::convertToPair)
                .collect(Collectors.toSet());

        // test if pairs are two and naked
        for (Iterator<Pair> pIter = pairSet.iterator(); pIter.hasNext();) {
            final Pair p = pIter.next();
            long pCount =  Arrays.stream(p_numberItemTab)
                    .filter(numberItemWithTwoCandidates)
                    .map(NakedPairs::convertToPair)
                    .filter((pair) -> p.getFirstValue() == pair.getFirstValue() && p.getSecondValue() == pair.getSecondValue())
                    .count();
            if (pCount != 2) {
                pIter.remove();
            }

        }

        return pairSet;
    }

    private static Pair convertToPair(NumberItem p_numberItem) {
        if (p_numberItem.getCandidates().size() != 2) {
            throw new IllegalArgumentException("Number item must contain 2 candidates to convert to a Pair");
        }

        Integer[] tab =  p_numberItem.getCandidates().toArray(new Integer[p_numberItem.getCandidates().size()]);
        return new Pair(tab[0], tab[1]);
    }

    private static boolean removeCandidates (NumberItem[] p_numberItem, final Pair p_Pair) {
        boolean candidateRemoved = false;
        Predicate<NumberItem> numberItemWithGivenPair = (numberItem) -> {
                if (numberItem.getCandidates().size() != 2) {
                    return false;
                }

                Integer[] tab = numberItem.getCandidates().toArray(new Integer[numberItem.getCandidates().size()]);
                return tab[0] == p_Pair.getFirstValue() && tab[1] == p_Pair.getSecondValue();
            };

        List<NumberItem> list = Arrays.stream(p_numberItem)
                .filter(numberItemWithGivenPair.negate())
                .collect(Collectors.toList());

        for (NumberItem numberItem : list) {
            if (numberItem.removeCandidate(p_Pair.getFirstValue())) candidateRemoved = true;
            if (numberItem.removeCandidate(p_Pair.getSecondValue())) candidateRemoved = true;
        }
        return candidateRemoved;
    }
}


