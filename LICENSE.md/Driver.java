/**
 * @author Seth Rivett
 * @related Reversi
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
	final static int BOARDSIZE = 64;

	public static void main(String[] args) {

		String JSONInput = new String("");
		
		// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html for below line
				BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));
				char inChar;
				inChar = ' ';
				String str = new String("");
				boolean firstBraceFound;
				firstBraceFound = false;
				while(firstBraceFound == false) {// find start brace and end brace
					try {
						inChar = (char) br.read();
						if (inChar == '{') {
							str += "{";
							firstBraceFound = true;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				int braces;
				braces = 1;
				boolean lastBraceFound = false;
				while(lastBraceFound == false) {// find start brace and end brace
					try {
						inChar = (char) br.read();
						str += inChar;
						if (inChar == '{') {
							braces++;
						}
						else if (inChar == '}') {
							braces--;
							if (braces == 0) {
								lastBraceFound = true;
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}			
		JSONInput = str;

		int[] board = new int[BOARDSIZE]; // make the board
		
		int parseIndex = 0;
		char currChar = ' ';
		for (int i = 0; i < BOARDSIZE; i++) {
			while ((parseIndex + 1< JSONInput.length()) &&
				 !((JSONInput.charAt(parseIndex) == '0'   ||
				   (JSONInput.charAt(parseIndex) == '1')  ||
				   (JSONInput.charAt(parseIndex) == '2')) )){
				parseIndex++;
			}
			currChar = JSONInput.charAt(parseIndex);
			switch (currChar) {
			case '0': board[i] = 0; break;
			case '1': board[i] = 1; break;
			case '2': board[i] = 2; break;
			}
			if (parseIndex + 1 < JSONInput.length()) { 
				parseIndex++;
			}
		}
				
		// get the player----------------------------------
		
		int player;
		player = 0;
		
		currChar = ' ';
		
		while ((parseIndex + 1< JSONInput.length()) &&
			 !((JSONInput.charAt(parseIndex) == '1'  ||
			   (JSONInput.charAt(parseIndex) == '2')) )){
			parseIndex++;
		}
		
		currChar = JSONInput.charAt(parseIndex);
		switch (currChar) {
		case '1': player = 1; break;
		case '2': player = 2; break;
		}
		if (parseIndex + 1 < JSONInput.length()) { 
			parseIndex++;
		}
		
		final String VALIDSTRING = new String("12345678");
		
		// get the column----------------------		
		int column;
		column = 0;
		
		currChar = ' ';
		
		while ((parseIndex + 1< JSONInput.length()) &&
				 !(VALIDSTRING.contains("" + JSONInput.charAt(parseIndex)))){
			parseIndex++;
		}
		currChar = JSONInput.charAt(parseIndex);
		column = getBound(currChar);
		if (parseIndex + 1 < JSONInput.length()) { 
			parseIndex++;
		}
		
		// get the row----------------------	
		int row;
		row = 0;
		currChar = ' ';
		
		while ((parseIndex + 1< JSONInput.length()) &&
				 !(VALIDSTRING.contains("" + JSONInput.charAt(parseIndex)))){
			parseIndex++;
		}
		
		currChar = JSONInput.charAt(parseIndex);
		row = getBound(currChar);
		
		if (parseIndex + 1 < JSONInput.length()) { 
			parseIndex++;
		}		
		
		board = makeMove(board, player, column, row);
		printBoard(board);
		
	}
	private static int getBound(char inChar) {
		int x;
		x = 0;
		switch (inChar) {
		case '1': x = 1; break;
		case '2': x = 2; break;
		case '3': x = 3; break;
		case '4': x = 4; break;
		case '5': x = 5; break;
		case '6': x = 6; break;
		case '7': x = 7; break;
		case '8': x = 8; break;
		}
		return x;
	}

	private static void printBoard(int[] board) {
		String printStr = new String("");
		printStr = printStr + "{ \"board\": [\n";
		int lineTally;
		final int ROWSIZE = 8;
		lineTally = 0;
		for (int i = 0; i < board.length; i++) {
			
			if (lineTally == 64) {
			}
			else {
				printStr += Integer.toString(board[i])  + ",";
			}
			if ((lineTally + 1) % ROWSIZE == 0) {
				printStr += "\n";
			}
			lineTally++;
		}
		printStr = printStr + "]}";
		System.out.print(printStr);
	}
	
	public static int[] makeMove(int[] board, int player, int column, int row) {
		int[] outBoard = board.clone();
		
		int position;
		position = getPosition(column, row);
		
		/* flip in all eight directions */
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (! ((i == 0) && (j == 0))) { //skip stopped direction (0,0)
					outBoard = flipDirection(outBoard, player, column, row, i, j);
				}
			}
		}
		if ((!(checkSum(outBoard) == checkSum(board))) && (position >= 0) && (position < 64 ) 
				&& (board[position] == 0)) {
			//write the move to the board
			outBoard[position] = player;
		}
		return outBoard;
	} 
	
	
	// used for a comparison of integer arrays
	private static int checkSum(int[] board) {
		int x;
		x = 0;
		for (int i: board) {
			x += i;
		}
		return x;
	}
	
	private static int getPosition (int column, int row) {
		return ((column - 1) + 8 * (row - 1));
	}
	
	private static int[] flipDirection (int[] board, int player, int column, int row, int columnDirection, int rowDirection) {
				int rowIndex, columnIndex;
				columnIndex = column + columnDirection;
				rowIndex = row + rowDirection;
				int[] flipBoard = board.clone();
				
				int x = 0;
				while  ((columnIndex >= 1) && (columnIndex <= 8) && 
						(rowIndex    >= 1) && (rowIndex    <= 8)){
								
					x = getPosition(columnIndex, rowIndex);
					if (board[x] == player) {
						return flipBoard;	
					}
					else if (board[x] == (3 - player)) {
						flipBoard[x] = player;
					}
					else if (board[x] == 0) {
						break; // early exit
					}
					columnIndex += columnDirection;
					rowIndex += rowDirection;
				}
				
			return board;
	}
}

