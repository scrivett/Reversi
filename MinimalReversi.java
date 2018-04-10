/**
 * @author Seth Rivett
 * @related Reversi
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MinimalReversi {
	final static int BOARDSIZE = 64;

	public static void main(String[] args) {
		// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html for below line
		BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));
		
		int[] board = getIntSequence(br, BOARDSIZE, "012");
		System.out.println("Board");
		int player = (getIntSequence(br, 1, "12"))[0];
		System.out.println("player");
		int column = (getIntSequence(br, 1, "12345678"))[0];
		System.out.println("column");
		int row = (getIntSequence(br, 1, "12345678"))[0];
		System.out.println("row");
						
		board = makeMove(board, player, column, row);
		printBoard(board);
		
	}

	private static void printBoard(int[] board) {
		String printStr = new String("");
		printStr = printStr + "{ \"board\": [\n";
		int lineTally;
		final int ROWSIZE = 8;
		lineTally = 0;
		for (int i = 0; i < board.length; i++) {
			
			if (lineTally == (BOARDSIZE - 1)) {
				printStr += Integer.toString(board[i]);
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
	
	public static int[] getIntSequence(BufferedReader reader, int size, String validString) {
		char inChar = ' ';

		int[] intSequence = new int[size];
		int index = 0;
		while(index != size) {
			try {
				inChar = (char) reader.read();
				if ((validString.contains("" + inChar))) {
					intSequence[index] = inChar - '0';
					index++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return intSequence;
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
