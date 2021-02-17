package com.assignment.content_sorting.util;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedReaderWrapper implements Comparable<BufferedReaderWrapper>{
	private BufferedReader bufferedReader;
    private String line;
    public BufferedReaderWrapper(BufferedReader bufferedReader){
        this.bufferedReader = bufferedReader;
        readNextLine();
    }

	private void readNextLine() {
		try {
			this.line=bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String next() {
		String answer=line;
		readNextLine();
		return answer;
	}
	
	public boolean isEmpty() {
		return this.line==null;
	}
	
	public String getComparable() {
		return this.line;
	}
	
	public void close() throws IOException {
		this.bufferedReader.close();
	}

	@Override
	public int compareTo(BufferedReaderWrapper o) {
		if(this.getComparable()==null)
			return 1;
		if(o.getComparable()==null)
			return -1;
		return this.getComparable().compareTo(o.getComparable());
	}
}
