/**
 * @author Seth Rivett
 * @related Reversi
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reversi {
	final static int BOARDSIZE = 64;
	
	public static void main(String[] args) {
		// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html for below line
		BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));
		int[] board = getIntSequence(br, BOARDSIZE, "012");
		int player = (getIntSequence(br, 1, "12"))[0];
		int column = (getIntSequence(br, 1, "12345678"))[0];
		int row = (getIntSequence(br, 1, "12345678"))[0];
						
		board = makeMove(board, player, column, row);
		System.out.println(boardStr(board));
	}

	private static String boardStr(int[] board) {
		String printStr = new String("{ \"board\": [\n");
		for (int i = 0; i < BOARDSIZE; i++) {
			printStr += Integer.toString(board[i])  + ",";
			if ((i + 1) % (Math.sqrt(BOARDSIZE)) == 0) {
				printStr += "\n";
			}
		}
		return (printStr = printStr.substring(0, printStr.length() - 2) + "]}");
	}
	
	private static int[] getIntSequence(BufferedReader reader, int size, String validString) {
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
	
	private static int[] makeMove(int[] board, int player, int column, int row) {
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
		if ((!(boardStr(outBoard) == boardStr(board))) && (position >= 0) && (position < 64 ) 
				&& (board[position] == 0)) {
			outBoard[position] = player; //write the move to the board
		}
		return outBoard;
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
