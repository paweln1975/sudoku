package pl.paweln.sudoku.test;

import org.junit.Test;
import pl.paweln.sudoku.*;
import pl.paweln.sudoku.algorithms.*;
import pl.paweln.sudoku.loaders.SudokuFileReader;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SudokuTests {
	@Test
	public void testNumberItemPos() {
		Sudoku s = new Sudoku();
		
		// evaluate central element
		Square fourth = s.getSquare(4);
		NumberItem item = fourth.getNumberItem(4);
		
		int localRow = item.getLocalRow();
		int localColumn = item.getLocalColumn();
		int globalRow = item.getGlobalRow();
		int globalColumn = item.getGlobalColumn();
		int squareRow = fourth.getRowNumber();
		int squareCol = fourth.getColNumber();
		
		assertEquals("Square row test", 1, squareRow);
		assertEquals("Square column test", 1, squareCol);
		assertEquals("Local row test", 1, localRow);
		assertEquals("Local column test", 1, localColumn);
		assertEquals("Global row test", 4, globalRow);
		assertEquals("Global column test", 4, globalColumn);
		
		// evaluate first element
		Square first = s.getSquare(0);
		item = first.getNumberItem(0);
				
		localRow = item.getLocalRow();
		localColumn = item.getLocalColumn();
		globalRow = item.getGlobalRow();
		globalColumn = item.getGlobalColumn();
		squareRow = first.getRowNumber();
		squareCol = first.getColNumber();
		
		assertEquals("Square row test", 0, squareRow);
		assertEquals("Square column test", 0, squareCol);
		assertEquals("Local row test", 0, localRow);
		assertEquals("Local column test", 0, localColumn);
		assertEquals("Global row test", 0, globalRow);
		assertEquals("Global column test", 0, globalColumn);
		
		// evaluate first element
		Square last = s.getSquare(8);
		item = last.getNumberItem(8);
		squareRow = last.getRowNumber();
		squareCol = last.getColNumber();
		
		assertEquals("Square row test", 2, squareRow);
		assertEquals("Square column test", 2, squareCol);
						
		localRow = item.getLocalRow();
		localColumn = item.getLocalColumn();
		globalRow = item.getGlobalRow();
		globalColumn = item.getGlobalColumn();
						
		assertEquals("Local row test", 2, localRow);
		assertEquals("Local column test", 2, localColumn);
		assertEquals("Global row test", 8, globalRow);
		assertEquals("Global column test", 8, globalColumn);
	}
	
	@Test
	public void testBigSquarePrint() {
		Sudoku s = new Sudoku();
		s.setValue(0, 0, 5);
		s.setValue(4, 4, 8);
		s.setValue(8, 8, 1);
		s.setValue(1, 6, 2);
		s.setValue(2, 2, 3);
		s.setValue(3, 1, 4);
		s.setValue(5, 5, 7);
		s.setValue(6, 7, 6);
		s.setValue(7, 3, 9);
		s.setValue(8, 4, 8);
		String output = " 5 0 0  0 0 0  0 0 3 \n" +
						" 0 0 0  0 0 0  0 0 0 \n" +
						" 0 0 0  2 0 0  0 0 0 \n\n" +
						" 0 4 0  0 0 0  0 0 0 \n" +
						" 0 0 0  0 8 0  0 0 7 \n" +
						" 0 0 0  0 0 0  0 0 0 \n\n" +
						" 0 0 0  0 0 0  0 0 0 \n" +
						" 0 0 0  9 0 0  0 8 0 \n" +
						" 0 6 0  0 0 0  0 0 1 \n";
		assertEquals("Big square print test", output, s.toString());
		
	}
	
	@Test
	public void testSquareFilledMissingNumbers() {
		Sudoku bs = new Sudoku();
		bs.setValue(0, 0, 1);
		bs.setValue(0, 2, 8);
		bs.setValue(0, 3, 6);
		bs.setValue(0, 6, 2);
		bs.setValue(0, 7, 5);
		
		Square s = bs.getSquare(0);
		List<NumberItem> l = s.getFilledNumberItems();
				
		assertEquals("getFilledNumberItems method test", l.toString() , "[1, 8, 6, 2, 5]");
		
		Set<Integer> set = s.getMissingNumbers();
		assertEquals("getMissingNumberItems method test", set.toString() , "[3, 4, 7, 9]");
	}
	
	@Test
	public void testSquareCorrespondingSquares() {
		Sudoku bs = new Sudoku();
		bs.setValue(0, 0, 1);
		List<Square> list = bs.getCorrespondingSquares(
				bs.getSquare(0), bs.getSquare(0).getNumberItem(0).getValue(), false);
		
		assertEquals("getCorrespondingSquaresWithoutNumber", list.toString(), "[Square:1, Square:2, Square:3, Square:6]");
	
		bs.setValue(1, 3, 1);
		bs.setValue(6, 4, 1);
		list = bs.getCorrespondingSquares(
				bs.getSquare(0), bs.getSquare(0).getNumberItem(0).getValue(), true);
		assertEquals("getCorrespondingSquaresWithNumber", list.toString(), "[Square:1, Square:6]");
	}
	
	@Test
	public void testSquareMakeCopyOfSquare() {
		Sudoku bs = new Sudoku();
		bs.setValue(0, 0, 1);
		bs.setValue(0, 1, 2);
		bs.setValue(0, 2, 3);
		bs.setValue(0, 3, 4);
		
		Square s = bs.getSquare(0);
		Square cs = Square.newInstance(s);
		
		assertEquals("copy of square", s.toStringRowCol(), cs.toStringRowCol());
		
	}
	
	@Test
	public void testOneEliminationIteration() {
		Sudoku sudoku = new Sudoku();
		sudoku.setValue(0, 2, 4);
		sudoku.setValue(0, 3, 5);
		sudoku.setValue(0, 7, 9);
		
		sudoku.setValue(1, 3, 8);
		sudoku.setValue(1, 6, 3);
		
		sudoku.setValue(2, 3, 6);
		sudoku.setValue(2, 7, 1);
		
		sudoku.setValue(3, 3, 1);
		sudoku.setValue(3, 5, 6);
		
		sudoku.setValue(4, 0, 1);
		sudoku.setValue(4, 2, 8);
		sudoku.setValue(4, 7, 2);
		
		sudoku.setValue(5, 1, 3);
		sudoku.setValue(5, 2, 2);
		sudoku.setValue(5, 5, 9);
		
		sudoku.setValue(6, 0, 4);
		sudoku.setValue(6, 1, 5);
		sudoku.setValue(6, 3, 3);
		sudoku.setValue(6, 5, 1);
		sudoku.setValue(6, 8, 8);
		
		sudoku.setValue(7, 0, 9);
		sudoku.setValue(7, 2, 6);
		sudoku.setValue(7, 5, 5);
		sudoku.setValue(7, 7, 4);
		
		sudoku.setValue(8, 4, 2);
		
		// set solution
		Sudoku copy = Sudoku.newInstance(sudoku);
		copy.setValue(3, 4, 2);
		copy.setValue(6, 2, 2);
		copy.setValue(6, 6, 9);
		copy.setValue(8, 3, 9);
		copy.setValue(8, 5, 4);
		
		SinglePosition algorithm = new SinglePosition();
		algorithm.singleSquarePosition(sudoku);

		assertEquals("Basic Elimination Algorithm", copy.toString(), sudoku.toString());
	}
	
	@Test
	public void testRowIterationCanHaveValue() {
		Sudoku sudoku = new Sudoku();
		sudoku.setValue(0, 2, 4);
		sudoku.setValue(0, 3, 5);
		sudoku.setValue(0, 7, 9);
		
		sudoku.setValue(1, 3, 8);
		sudoku.setValue(1, 6, 3);
		
		sudoku.setValue(2, 3, 6);
		sudoku.setValue(2, 7, 1);
		
		sudoku.setValue(3, 3, 1);
		sudoku.setValue(3, 5, 6);
		
		sudoku.setValue(4, 0, 1);
		sudoku.setValue(4, 2, 8);
		sudoku.setValue(4, 7, 2);
		
		sudoku.setValue(5, 1, 3);
		sudoku.setValue(5, 2, 2);
		sudoku.setValue(5, 5, 9);
		
		sudoku.setValue(6, 0, 4);
		sudoku.setValue(6, 1, 5);
		sudoku.setValue(6, 3, 3);
		sudoku.setValue(6, 5, 1);
		sudoku.setValue(6, 8, 8);
		
		sudoku.setValue(7, 0, 9);
		sudoku.setValue(7, 2, 6);
		sudoku.setValue(7, 5, 5);
		sudoku.setValue(7, 7, 4);
		
		sudoku.setValue(8, 4, 2);
		sudoku.setValue(3, 4, 2);
		sudoku.setValue(6, 2, 2);
		sudoku.setValue(6, 6, 9);
		sudoku.setValue(8, 3, 9);
		sudoku.setValue(8, 5, 4);
		
		BaseSudokuAlgorithm algorithm = new BaseSudokuAlgorithm() {
			@Override
			public boolean perform(Sudoku p_sudoku) {
				return false;
			}
		};

		NumberItem n = sudoku.getSquare(6).getNumberItem(4);
		assertEquals("Row Col Elimination CanHaveValueMethod 1", true,
				algorithm.canHaveValue(sudoku, n, 6));
		
		assertEquals("Row Col Elimination CanHaveValueMethod 2", false,
				algorithm.canHaveValue(sudoku, n, 8));
		
		n = sudoku.getSquare(7).getNumberItem(3);
		assertEquals("Row Col Elimination CanHaveValueMethod 3", false,
				algorithm.canHaveValue(sudoku, n, 8));
		
		n = sudoku.getSquare(7).getNumberItem(4);
		assertEquals("Row Col Elimination CanHaveValueMethod 4", true,
				algorithm.canHaveValue(sudoku, n, 8));
		
		
	}
	
	@Test
	public void testOneRowIteration() {
		Sudoku sudoku = new Sudoku();
		sudoku.setValue(0, 2, 4);
		sudoku.setValue(0, 3, 5);
		sudoku.setValue(0, 7, 9);
		
		sudoku.setValue(1, 3, 8);
		sudoku.setValue(1, 6, 3);
		
		sudoku.setValue(2, 3, 6);
		sudoku.setValue(2, 7, 1);
		
		sudoku.setValue(3, 3, 1);
		sudoku.setValue(3, 5, 6);
		
		sudoku.setValue(4, 0, 1);
		sudoku.setValue(4, 2, 8);
		sudoku.setValue(4, 7, 2);
		
		sudoku.setValue(5, 1, 3);
		sudoku.setValue(5, 2, 2);
		sudoku.setValue(5, 5, 9);
		
		sudoku.setValue(6, 0, 4);
		sudoku.setValue(6, 1, 5);
		sudoku.setValue(6, 3, 3);
		sudoku.setValue(6, 5, 1);
		sudoku.setValue(6, 8, 8);
		
		sudoku.setValue(7, 0, 9);
		sudoku.setValue(7, 2, 6);
		sudoku.setValue(7, 5, 5);
		sudoku.setValue(7, 7, 4);
		
		sudoku.setValue(8, 4, 2);
		sudoku.setValue(3, 4, 2);
		sudoku.setValue(6, 2, 2);
		sudoku.setValue(6, 6, 9);
		sudoku.setValue(8, 3, 9);
		sudoku.setValue(8, 5, 4);
		
		// set solution
		Sudoku copy = Sudoku.newInstance(sudoku);
		copy.setValue (1, 5, 2);
		copy.setValue (4, 1, 6);
		copy.setValue (6, 4, 6);
		copy.setValue (7, 4, 8);
		copy.setValue (7, 6, 2);

		SinglePosition algorithm = new SinglePosition();
		algorithm.singleRowPosition(sudoku);
		
		assertEquals("Row Col Elimination Algorithm", copy.toString(), sudoku.toString());
	}
	
	
	@Test
	public void testSquareIsCompleted() {
		Sudoku bs = new Sudoku();
		bs.setValue(0, 0, 8);
		bs.setValue(0, 1, 2);
		bs.setValue(0, 2, 6);
		bs.setValue(0, 3, 4);
		bs.setValue(0, 4, 5);
		bs.setValue(0, 5, 3);
		bs.setValue(0, 6, 7);
		bs.setValue(0, 7, 1);
		
		Square s = bs.getSquare(0);
		assertEquals("Check square completness", false, s.isCompleted());
		
		bs.setValue(0, 8, 9);
		assertEquals("Check square completness", true, s.isCompleted());
	}
	
	@Test 
	public void testSquareIsRowColComplated() {
		Sudoku sudoku = new Sudoku();
		
		sudoku.setValue(0, 0, 1);
		sudoku.setValue(0, 1, 2);
		sudoku.setValue(0, 2, 3);
		sudoku.setValue(1, 0, 4);
		sudoku.setValue(1, 1, 5);
		sudoku.setValue(1, 2, 6);
		sudoku.setValue(2, 0, 7);
		sudoku.setValue(2, 1, 8);
		sudoku.setValue(2, 2, 9);
		

		assertEquals("Check row/col completness", false, sudoku.isRowColCompleted(0));
		
		sudoku.setValue(0, 3, 4);
		sudoku.setValue(0, 6, 5);
		
		sudoku.setValue(3, 0, 2);
		sudoku.setValue(3, 3, 3);
		sudoku.setValue(3, 6, 6);
		sudoku.setValue(6, 0, 7);
		sudoku.setValue(6, 3, 8);
		sudoku.setValue(6, 6, 9);
		
		
//		System.out.println(bs);
		
		assertEquals("Check row/col completness", true, sudoku.isRowColCompleted(0));
	}
	
	@Test
	public void testSimpleSudokuSolverAlgorithm() {
		Sudoku sudoku = new Sudoku();
		sudoku.setValue(0, 2, 4);
		sudoku.setValue(0, 3, 5);
		sudoku.setValue(0, 7, 9);
		
		sudoku.setValue(1, 3, 8);
		sudoku.setValue(1, 6, 3);
		
		sudoku.setValue(2, 3, 6);
		sudoku.setValue(2, 7, 1);
		
		sudoku.setValue(3, 3, 1);
		sudoku.setValue(3, 5, 6);
		
		sudoku.setValue(4, 0, 1);
		sudoku.setValue(4, 2, 8);
		sudoku.setValue(4, 7, 2);
		
		sudoku.setValue(5, 1, 3);
		sudoku.setValue(5, 2, 2);
		sudoku.setValue(5, 5, 9);
		
		sudoku.setValue(6, 0, 4);
		sudoku.setValue(6, 1, 5);
		sudoku.setValue(6, 3, 3);
		sudoku.setValue(6, 5, 1);
		sudoku.setValue(6, 8, 8);
		
		sudoku.setValue(7, 0, 9);
		sudoku.setValue(7, 2, 6);
		sudoku.setValue(7, 5, 5);
		sudoku.setValue(7, 7, 4);
		
		sudoku.setValue(8, 4, 2);
		
		
		Sudoku copy = Sudoku.newInstance(sudoku);
		
		copy.setValue(0, 0, 2);
		copy.setValue(0, 1, 8);
		copy.setValue(0, 4, 1);
		copy.setValue(0, 5, 3);
		copy.setValue(0, 6, 6);
		copy.setValue(0, 8, 7);
		copy.setValue(1, 0, 6);
		copy.setValue(1, 1, 7);
		copy.setValue(1, 2, 1);
		copy.setValue(1, 4, 9);
		copy.setValue(1, 5, 2);
		copy.setValue(1, 7, 5);
		copy.setValue(1, 8, 4);
		copy.setValue(2, 0, 3);
		copy.setValue(2, 1, 9);
		copy.setValue(2, 2, 5);
		copy.setValue(2, 4, 4);
		copy.setValue(2, 5, 7);
		copy.setValue(2, 6, 2);
		copy.setValue(2, 8, 8);
		copy.setValue(3, 0, 7);
		copy.setValue(3, 1, 4);
		copy.setValue(3, 2, 9);
		copy.setValue(3, 4, 2);
		copy.setValue(3, 6, 8);
		copy.setValue(3, 7, 3);
		copy.setValue(3, 8, 5);
		copy.setValue(4, 1, 6);
		copy.setValue(4, 3, 5);
		copy.setValue(4, 4, 3);
		copy.setValue(4, 5, 7);
		copy.setValue(4, 6, 4);
		copy.setValue(4, 8, 9);
		copy.setValue(5, 0, 5);
		copy.setValue(5, 3, 4);
		copy.setValue(5, 4, 8);
		copy.setValue(5, 6, 7);
		copy.setValue(5, 7, 6);
		copy.setValue(5, 8, 1);
		copy.setValue(6, 2, 2);
		copy.setValue(6, 4, 6);
		copy.setValue(6, 6, 9);
		copy.setValue(6, 7, 7);
		copy.setValue(7, 1, 1);
		copy.setValue(7, 3, 7);
		copy.setValue(7, 4, 8);
		copy.setValue(7, 6, 2);
		copy.setValue(7, 8, 3);
		copy.setValue(8, 0, 8);
		copy.setValue(8, 1, 7);
		copy.setValue(8, 2, 3);
		copy.setValue(8, 3, 9);
		copy.setValue(8, 5, 4);
		copy.setValue(8, 6, 1);
		copy.setValue(8, 7, 5);
		copy.setValue(8, 8, 6);
		
		
		SudokuSolver solver = new SudokuSolver(sudoku);
		solver.solve();
		
		assertEquals("SudokuApp solver algorithm - solution 1", copy.toString(), sudoku.toString());
	}
	
	@Test
	public void testFindCandidatesAlgorithm() throws Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/testSingleCandidateAlgorithm.txt"));
		SingleCandidate scAlg = new SingleCandidate();
		scAlg.refreshCandidates(sudoku);
		
		NumberItem item = sudoku.getSquare(0).getNumberItem(7);
		Set<Integer> candidates = item.getCandidates();
		
		List<Integer> result = new LinkedList<>();
		result.add(6);
		
		assertEquals("find 1 Candidates method", result.toString(), candidates.toString());
		
		item = sudoku.getSquare(1).getNumberItem(8);
		candidates = item.getCandidates();
		
		result.add(8);
		result.add(9);
		
		assertEquals("find 2 Candidates method", result.toString(), candidates.toString());
		
	}
	
	@Test 
	public void testSingleCandidateAlgorithm() throws Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/testSingleCandidateAlgorithm.txt"));
		SudokuAlgorithm scAlg = new SingleCandidate();
		
		Sudoku expResult = Sudoku.newInstance(sudoku);
		expResult.setValue(0, 7, 6);

		scAlg.refreshCandidates(sudoku);
		scAlg.perform(sudoku);
		
		assertEquals("test Single Candidate Algorithm", sudoku.toString(), expResult.toString());
	}
	
	@Test
	public void testLoadFromFile() throws Exception {
		Sudoku bs = new Sudoku();
		bs.setValue(0, 2, 4);
		bs.setValue(0, 3, 5);
		bs.setValue(0, 7, 9);
		
		bs.setValue(1, 3, 8);
		bs.setValue(1, 6, 3);
		
		bs.setValue(2, 3, 6);
		bs.setValue(2, 7, 1);
		
		bs.setValue(3, 3, 1);
		bs.setValue(3, 5, 6);
		
		bs.setValue(4, 0, 1);
		bs.setValue(4, 2, 8);
		bs.setValue(4, 7, 2);
		
		bs.setValue(5, 1, 3);
		bs.setValue(5, 2, 2);
		bs.setValue(5, 5, 9);
		
		bs.setValue(6, 0, 4);
		bs.setValue(6, 1, 5);
		bs.setValue(6, 3, 3);
		bs.setValue(6, 5, 1);
		bs.setValue(6, 8, 8);
		
		bs.setValue(7, 0, 9);
		bs.setValue(7, 2, 6);
		bs.setValue(7, 5, 5);
		bs.setValue(7, 7, 4);
		
		bs.setValue(8, 4, 2);
		
		Sudoku loadedSquare = new Sudoku(new SudokuFileReader("./res/verysimple1.txt"));
		
		assertEquals ("Test sudoku loading from file", bs.toString(), loadedSquare.toString());
	}
	


	@Test
	public void testCandidatePrintout() throws Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/broken2.txt"));
		SudokuAlgorithm algorithm = new BaseSudokuAlgorithm() {
			@Override
			public boolean perform(Sudoku p_sudoku) {
				return false;
			}
		};
		algorithm.refreshCandidates(sudoku);


		String output = " 6 5 [1, 7]  [1, 4, 7] [1, 4, 7] 2  9 3 8 \n" +
				" [3, 4, 8] [2, 4, 7, 8] [2, 3, 7]  9 [4, 7, 8] 5  6 1 [4, 7] \n" +
				" [1, 4, 8] 9 [1, 7]  6 [1, 4, 7, 8] 3  2 [4, 5, 7] [4, 5, 7] \n\n" +
				" 9 6 5  8 [4, 7] [4, 7]  1 2 3 \n" +
				" 7 3 4  2 9 1  5 8 6 \n" +
				" 2 1 8  3 5 6  7 [4, 9] [4, 9] \n\n" +
				" [1, 3, 4, 5, 8] [2, 4, 7, 8] 6  [1, 4, 7] [1, 2, 3, 4, 7, 8] [4, 7, 8]  [3, 8] [5, 7, 9] [2, 5, 7, 9] \n" +
				" [3, 8] [2, 7, 8] 9  5 [2, 3, 6, 7, 8] [7, 8]  4 [6, 7] 1 \n" +
				" [1, 3, 4, 5, 8] [2, 4, 7, 8] [1, 2, 3, 7]  [1, 4, 7] [1, 2, 3, 4, 6, 7, 8] 9  [3, 8] [5, 6, 7] [2, 5, 7] \n";
		assertEquals("Sudoku print test with candidates", output, sudoku.toString(true));
	}

	@Test
	public void testGetColNumberItems() throws Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/broken2.txt"));
		List<NumberItem> list = sudoku.getRowNumberItems(0);
		assertEquals("Test getRow/Col Number Items (row)", "[6, 5, 0, 0, 0, 2, 9, 3, 8]", list.toString());

		list = sudoku.getColNumberItems(3);
		assertEquals("Test getRow/Col Number Items (col)", "[0, 9, 6, 8, 2, 3, 0, 5, 0]", list.toString());
	}

	@Test
	public void testCandidatesRemoval() throws  Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/medium2.txt"));
		BaseSudokuAlgorithm algorithm = new BaseSudokuAlgorithm() {
			@Override
			public boolean perform(Sudoku p_sudoku) {
				return false;
			}
		};
		algorithm.refreshCandidates(sudoku);
		NumberItem numberItem =  sudoku.getSquare(0).getNumberItem(0);
		numberItem.setValue(1);
		algorithm.eliminateCandidatesForNumberItem(sudoku, numberItem);

		numberItem =  sudoku.getSquare(4).getNumberItem(0);
		numberItem.setValue(7);
		algorithm.eliminateCandidatesForNumberItem(sudoku, numberItem);

		String output = " 1 [2, 4, 6, 7, 8, 9] [4, 6, 7, 9]  3 [2, 4, 5, 7, 8] [2, 7, 8]  [5, 7] [5, 7, 9] [5, 7, 9] \n"+
				" [2, 3, 5, 7] [2, 3, 7] [3, 7]  1 6 9  4 8 [3, 5, 7] \n"+
 				" [3, 4, 5, 7] [3, 4, 7, 8, 9] [3, 4, 7, 9]  [4, 5] [4, 5, 7, 8] [7, 8]  6 2 [1, 3, 5, 7, 9] \n\n"+
				" [3, 6] 5 2  7 1 4  9 [3] [3, 6, 8] \n"+
 				" [3, 4, 6, 7] [1, 3, 4, 6, 7] 8  [2, 9] [2, 3] 5  [1, 2, 3, 7] [3, 7] [1, 2, 3, 6, 7] \n"+
				" 9 [1, 3, 7] [1, 3, 7]  [2] [2, 3, 8] 6  [1, 2, 3, 5, 7, 8] [3, 5, 7] 4 \n\n"+
				" 8 [2, 3, 4, 6, 7, 9] [3, 4, 6, 7, 9]  [2, 4, 5, 6] [2, 3, 4, 5, 7] [2, 3, 7]  [2, 3, 5, 7] 1 [2, 3, 5, 7, 9] \n"+
 				" [2, 3, 4, 6, 7] [2, 3, 4, 6, 7] [3, 4, 6, 7]  8 9 1  [2, 3, 5, 7] [3, 4, 5, 7] [2, 3, 5, 7] \n"+
 				" [2, 3, 4, 7] [1, 2, 3, 4, 7, 9] 5  [2, 4] [2, 3, 4, 7] [2, 3, 7]  [2, 3, 7, 8] 6 [2, 3, 7, 8, 9] \n";
		assertEquals("test remove candidates after setValue", output, sudoku.toString(true));
//		System.out.println(sudoku.toString(true));
	}

	@Test
	public void testNakedPairs() throws Exception {
		Sudoku sudoku = new Sudoku(new SudokuFileReader("./res/medium2beforeNakedPair.txt"));
		NakedPairs nakedPairs = new NakedPairs();
		nakedPairs.refreshCandidates(sudoku);
		Set<Pair> pairSet = nakedPairs.findNakedPairs(sudoku.getSquare(3).getNumberItems());
		assertEquals("Test finding naked pars", "[{1,4}, {3,7}]", pairSet.toString());

		nakedPairs.perform(sudoku);

		String output =
				" [1, 2, 4, 5, 7] [1, 2, 4, 6, 7, 8] [1, 4, 6]  3 [2, 4, 7] [2, 7, 8]  [5, 7] 9 [1, 5, 7] \n"+
 				" [2, 3, 5, 7] [2, 3, 7] [3, 7]  1 6 9  4 8 [3, 5, 7] \n"+
 				" [1, 3, 4, 7] [1, 3, 4, 7, 8, 9] [1, 4, 9]  5 [4, 7] [7, 8]  6 2 [1, 3, 7] \n\n"+
				" 6 5 2  7 1 4  9 3 8 \n"+
				" [1, 4] [1, 4] 8  9 3 5  2 7 6 \n"+
				" 9 [3, 7] [3, 7]  2 8 6  1 5 4 \n\n"+
				" 8 [2, 3, 4, 7, 9] [4, 9]  6 5 [2, 3, 7]  [3, 7] 1 [2, 3, 7, 9] \n"+
 				" [2, 3, 7] [2, 3, 6, 7] [6]  8 9 1  [3, 5, 7] 4 [2, 3, 5, 7] \n"+
 				" [1, 2, 3, 7] [1, 2, 3, 7, 9] 5  4 [2, 7] [2, 3, 7]  8 6 [2, 3, 7, 9] \n";
		assertEquals("test Naked Pairs algorithm", output, sudoku.toString(true));
	}
}
