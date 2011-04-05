package com.ladro.topcoder.crossword;

public class BeautifulCrossword {

	public String[] generateCrossword(int N, String[] words, int[] weights) {
		new Words(words, N);
		Board board = new Board(N, weights);
		System.out.println("Score: " + board.getScore());
		return board.getReturn();
	}
}
