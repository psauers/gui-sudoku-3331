package sudoku.model;
/**
*	Phillip Sauers
*	Edna
*	CS 3331
*	3/1/18
*	HW 2
**/

public class Board{
	
	private int size;
	private int[][] gameBoard;
	private int openTiles;
	private int moves;
	private boolean[][] xValues;
	private boolean[][] yValues;
	private boolean complete;
	
	public Board(int size){
		this.size = size;
		this.gameBoard = createBoard(size);
		this.openTiles = size * size; 						//number of tiles is size squared (4x4 = 16, etc.)
		this.moves = 0;
		this.xValues = new boolean[size+1][size+1];			//we change to +1 so we can more easily check if a number has been played.
		this.yValues = new boolean[size+1][size+1];
		this.complete = false;
	}
	
	//Default constructor in case of no arguments...
	public Board() {
		this.size = 4;
		this.gameBoard = createBoard(4);
		this.openTiles = 16;
		this.moves = 0;
		this.xValues = new boolean[4][4];
		this.yValues = new boolean[4][4];
		this.complete = false;
	}
	
	//setters
	public void setGameBoard(int[][] gameBoard) {
		this.gameBoard = gameBoard;
	}
	
	public void setOpenTiles(int openTiles) {
		this.openTiles = openTiles;
	}
	
	public void setXValues(boolean[][] xValues) {
		this.xValues = xValues;
	}
	
	public void setYValues(boolean[][] yValues) {
		this.yValues = yValues;
	}
	
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	//getters
	public int[][] getGameBoard() {
		return this.gameBoard;
	}
	
	public int getOpenTiles() {
		return this.openTiles;
	}
	
	public int getMoves() {
		return this.moves;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public boolean[][] getXValues() {
		return this.xValues;
	}
	
	public boolean[][] getYValues() {
		return this.yValues;
	}
	
	public boolean getComplete() {
		return this.complete;
	}
	
	/**
	 *	Updates the openTiles variable, used in determining the current state (win/lose) of the game.
	 *	@return void
	 **/
	public void updateOpenTiles(){
		this.openTiles = getOpenTiles() - 1;
	}
	
	/**
	 *	Updates the moves variable, used as a basic stat.
	 *	@return void
	 **/
	public void updateMoves(){
		this.moves = getMoves() + 1;
	}
	
	/**
	 *	Method checks all user inputted values in order to make sure they can be played.
	 *	it checks: the coordinates and moves (to make sure they're within range, 1 to size);
	 *	should the above check work, it also checks the boolean arrays to ensure a number isn't already played.
	 *	@param size the size of the board
	 *	@return int[][]
	 **/
	public int[][] createBoard(int size){
		int[][] newBoard = new int[size+1][size+1];
		for (int i = 0; i < newBoard.length; i++){
			for (int j = 0; j < newBoard[i].length; j++) {
				if (i == 0) {
					newBoard[i][j] = j;
				} else if (j == 0) {
					newBoard[i][j] = i;
				}
			}
		}newBoard[0][0] = 0;
	return newBoard;
	}
	
	/**
	 *	Method checks all user inputted values in order to make sure they can be played.
	 *	it checks: the coordinates and moves (to make sure they're within range, 1 to size);
	 *	should the above check work, it also checks the boolean arrays to ensure a number isn't already played.
	 *	@param x the x coordinate 
	 *	@param y the y coordinate 
	 *	@param move the move being played 
	 *	@return boolean
	 **/
	public boolean checkMove(int x, int y, int move){
		if (move == -1) {
			return true;
		}else if ((x < 1) || (y < 1) || (x > this.size) || (y > this.size)){
			return false;
		}else{
			if (getXValues()[x][move] || getYValues()[y][move]) {
				return false;
			}
		}return true;
	}
	
	public void removeNumber(int x, int y) {
		updateMoves();
		setOpenTiles(getOpenTiles() + 1);
		int[][] temp = getGameBoard();
		boolean[][] tempX = getXValues();
		boolean[][] tempY = getYValues();
		tempX[x][temp[y][x]] = tempY[y][temp[y][x]] = false;
		temp[y][x] = 0;
		setXValues(tempX);
		setYValues(tempY);
		setGameBoard(temp);
	}
	
	/**
	 *	This method attempts to make a move by taking the coordinates for the x and y,
	 *	along with the move, and attempting to check it before placing it there.
	 *	If it can be placed there, the move is made onto the board and input into
	 *	the boolean arrays; openTiles and moves are updated; and the game checks
	 *	whether a win sequence should be initiated here.
	 *	If move is invalid, invalidMove is called.
	 *	@param x the x coordinate 
	 *	@param y the y coordinate 
	 *	@param move the move being played 
	 *	@return void
	 **/
	public boolean makeMove(int x, int y, int move) {
		updateMoves();
		if (move == -1) {
			//cheat code - initiates win sequence if -1 is entered.
			setOpenTiles(0);
			setComplete(true);
			return true;
		}if (checkMove(x,y,move)){
			//checkMove was true, so we allow the move to be made.
			int[][] temp = getGameBoard();
			boolean[][] tempX = getXValues();
			boolean[][] tempY = getYValues();
			if (temp[y][x] == 0) {
				updateOpenTiles();
			}temp[y][x] = move;
			tempX[x][move] = tempY[y][move] = true;
			setXValues(tempX);
			setYValues(tempY);
			setGameBoard(temp);
			if (getOpenTiles() == 0) {
				setComplete(true);
			}return true;
		} else {
			//you can't make this move!
			return false;
		}
	}
		
	/**
	 *	This method prints the current board, along with printing
	 *	the current game progress (by displaying openTiles and moves).
	 *	@return void
	 **/	
	public void printBoard() {
		if (getOpenTiles() != 0) {
			System.out.println("\n\nCurrent open tiles: " + getOpenTiles() + "  |  Total moves: " + getMoves() + "  |  Current board:");
		}for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {
				if (gameBoard[i][j] != 0) {
					System.out.print(gameBoard[i][j]);
				} else {
					System.out.print(" ");
				}System.out.print(" | ");
			}System.out.print("\n--+");
			for (int k = 0; k < getSize(); k++) {
				System.out.print("---+");
			}System.out.println();
		}
	}
}	