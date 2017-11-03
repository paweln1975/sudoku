package pl.paweln.sudoku.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.LinkedList;
import java.util.List;

public class SudokuFileReader implements SudokuReader {
	
	private String fileName;
	private String fileContent;
	
	public SudokuFileReader(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		boolean fileExists = false;
		File f = new File(this.fileName);
		if (f.exists() && !f.isDirectory()) { 
		    fileExists = true;
		}
		
		if (!fileExists) {
			throw new FileNotFoundException("File not found: " + this.fileName);
		}
	}
	@Override
	public List<Integer> getValues() throws IOException {
		List<Integer> list = new LinkedList<>();

		this.fileContent = readFile();
		String[] values = this.fileContent.split(" ");
		for (String value1 : values) {
			if (value1.trim().length() > 0) {
				Integer value = Integer.valueOf(value1);
				if (value < 0 || value > 9)
					throw new InvalidObjectException("Value in the file out of range: " + value.intValue());
				else {
					list.add(value);
				}
			}
		}

		if (list.size() != 81) {
			throw new InvalidObjectException("Invalid number of numbers in the file:" + list.size());
		}

		return list;
	}
	
	private String readFile() throws IOException {
		String everything;
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line.trim());
		        sb.append(" ");
		        line = br.readLine();
		    }
		    everything = sb.toString();
		} 
		
		finally {
		    br.close();
		}
		return everything;
	}
}
