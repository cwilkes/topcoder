package com.ladro.topcoder.crossword;

public class Board {

	private final char[][] board;
	private final int[] weights;

	public Board(int size, int[] weights) {
		this.weights = weights;
		board = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = '.';
			}
		}
	}

	public String[] getReturn() {
		String[] ret = new String[board.length];
		int i = 0;
		for (char[] row : board) {
			ret[i++] = String.valueOf(row);
		}
		return ret;
	}

	private int getTotalLetters() {
		int totalLetters = 0;
		for (char[] row : board) {
			for (char c : row) {
				if (c != '.') {
					totalLetters++;
				}
			}
		}
		return totalLetters;
	}

	public double getScore() {
		int N = board.length;
		// 1. board filling score = no of letters / no of cells
		int totalLetters = getTotalLetters();
		double boardFillingScore = totalLetters * 1.0 / (N * N);
		int i = 0;
		int j = 0;
		// 2. rows/cols filling score - no of cols with at least 1 char * no
		// of rows with at least 1 char / N*N
		int filledCols = 0, filledRows = 0;
		boolean emptyCol, emptyRow;
		for (i = 0; i < N; i++) {
			emptyCol = emptyRow = true;
			for (j = 0; j < N; j++) {
				if (board[i][j] != '.')
					emptyRow = false;
				if (board[j][i] != '.')
					emptyCol = false;
			}
			if (!emptyCol)
				filledCols++;
			if (!emptyRow)
				filledRows++;
		}
		double rcFillingScore = filledCols * filledRows * 1.0 / (N * N);

		// 3. symmetry score
		double symmetryScore = 0.0, nc = 0, cellScore;
		int nEqual;
		for (i = 0; i < (N + 1) / 2; i++)
			for (j = 0; j <= i; j++) {
				nEqual = (board[i][j] == '.' ? 1 : 0) + (board[i][N - j - 1] == '.' ? 1 : 0) + (board[N - i - 1][j] == '.' ? 1 : 0)
						+ (board[N - i - 1][N - j - 1] == '.' ? 1 : 0) + (board[j][i] == '.' ? 1 : 0) + (board[j][N - i - 1] == '.' ? 1 : 0)
						+ (board[N - j - 1][i] == '.' ? 1 : 0) + (board[N - j - 1][N - i - 1] == '.' ? 1 : 0);
				nEqual = Math.max(nEqual, 8 - nEqual);
				cellScore = 0;
				if (nEqual == 8)
					cellScore = 1;
				if (nEqual == 7)
					cellScore = 0.5;
				if (nEqual == 6)
					cellScore = 0.1;
				symmetryScore += cellScore;
				nc++;
				// System.out.println(i+" "+j+" "+nEqual+": "+board[i][j]+board[i][N-j-1]+board[N-i-1][j]+board[N-i-1][N-j-1]+board[j][i]+board[j][N-i-1]+board[N-j-1][i]+board[N-j-1][N-i-1]+" -> "+cellScore);
			}
		symmetryScore /= nc;

		// 4. words crossings score - no of letters which are parts of 2
		// words, divided by no of letters overall
		// for each letter, check whether it's part of a vertical word, and
		// part of horizontal word
		double crossingsScore = 0;
		for (i = 0; i < N; i++)
			for (j = 0; j < N; j++)
				if (board[i][j] != '.' && (i > 0 && board[i - 1][j] != '.' || i < N - 1 && board[i + 1][j] != '.')
						&& (j > 0 && board[i][j - 1] != '.' || j < N - 1 && board[i][j + 1] != '.'))
					crossingsScore++;
		if (totalLetters > 0)
			crossingsScore /= totalLetters;

		return (boardFillingScore * weights[0] + rcFillingScore * weights[1] + symmetryScore * weights[2] + crossingsScore * weights[3])
				/ (weights[0] + weights[1] + weights[2] + weights[3]);
	}
}
