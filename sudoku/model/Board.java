package sudoku.dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Board{
	private int moves;   //Playable Moves
	private int rowSize; //RowQuad
	private int colSize; //ColQuad
	private int [][] puzzle;
	private int [][] temp;
	private int size;
	private boolean[][] writable; //used if puzzle is pre-loaded so user can't overwrite pre-loaded numbers
	//Constructors
	
	public Board(){
		init(4);
	}
	
	public Board(int size){
		init(size);	
	}
	//Getters
	public int getSize(){
		return size;
	} 
	
	public int getMoves(){
		return moves;
	}
	
	public int[][] getGameBoard(){
		return puzzle;
	}
	public boolean getComplete(){
		return (moves == 0);
	}
	
	//setters
	public void zeroMoves(){
		 this.moves = 0;
	}
	public void setGameBoard(int[][] gameBoard) {
		this.puzzle = gameBoard;
	}
	public void setWriteBoard(boolean[][] gameBoard) {
		this.writable = gameBoard;
	}
	//Array Index Range
	public int range(int max){
		return (int)(Math.random()* max);
	}

	//determine size and layout
	private void init(int size){
		this.size 	= size;
		moves 		= (size*size)+1;
		puzzle 		= new int[size][size];
		temp 		= new int[size][size];
		writable 	= new boolean[size][size];
		for(int x = 0; x < puzzle.length; x++){
			Arrays.fill(writable[x],true);
			Arrays.fill(puzzle[x],0);
		}
		if(puzzle.length%2 == 0){
			rowSize = colSize = 2;
			}
		else if(puzzle.length%3 == 0){
			rowSize = colSize = 3;
			}
		}//end init

	//Check if player has made a valid move
	public boolean validMove(int number, int inputI, int inputJ, boolean forSolving){
		int rowQuad = 0;
		int colQuad = 0;
		//Figures out which quadrant it is in
		for(int i = rowSize; i <= puzzle.length; i += rowSize){
			if(inputI < i){
				rowQuad = (i - rowSize);
				break;
			}
		}
		//After it figures out which quadrant it is in, checks column
		for(int j = colSize; j <= puzzle.length; j += colSize){
			if(inputJ < j){
				colQuad = (j - colSize);
				break;
			}
		}
		//checks the row quadrant 
		for(int i = rowQuad; i < rowSize; i++){
			for(int j = colQuad; j < colSize; j++){
				if( (i== inputI && j == inputJ)){
					if(writable[i][j]==false){
						return false;
					}
				}
					else if(puzzle[i][j] == number){
						return false;
					}
			}
		}
		//ROW
		for(int i = inputI, j = 0; j < puzzle.length; j++){
			if( (i == inputI && j == inputJ)){
				if(writable[i][j]==false){
					return false;
				}
			}
				else if(puzzle[i][j] == number){
					return false;
				}
			}
		//COL
		for(int i = 0, j = inputJ; i < puzzle.length; i++){
			if( (i== inputI && j == inputJ)){
				if(writable[i][j]==false){
					return false;
				}
			}
				else if(puzzle[i][j] == number){
					return false;
				}
		}
		
		if(forSolving){
			puzzle[inputI][inputJ]= number;
			moves --;
		}
		else{
			
		}
		return true;
	}//End validMove
	
	//Remove Move
	public boolean removeNumber(int inputI, int inputJ){
		if(!writable[inputI][inputJ]){
			return false;
		}
		puzzle[inputI][inputJ] = 0;
		moves ++;
		return true;
	}//end RemoveMove
	
	//use for strategy pattern
	public void randomNumberGenerator(){

	}
	//checks if puzzle is solvable
	public boolean solvable() {
		for (int i = 0; i < puzzle.length; i++) {
				temp[i] = Arrays.copyOf(puzzle[i], puzzle[i].length);
		}
		return solve(false);
	}
	
	//solves puzzle
	public boolean solve(boolean forSolving) {
		if(forSolving){
			temp = getGameBoard();
		}
	    for (int i = 0; i < temp.length; i++) {
	        for (int j = 0; j < temp[i].length; j++) {
	            if (temp[i][j] == 0) {
	                for (int k = 1; k <= size; k++) {
	                    temp[i][j] = k;
	                    if (validMove(k, i, j, forSolving) && solve(forSolving)) {
	                        return true;
	                    }
	                    temp[i][j] = 0;
	                }
	                return false;
	            }
	        }
	    }
	    if(forSolving){
	    setGameBoard(temp);
	    zeroMoves();
	    }
	    return true;
	}
	
	/*
	 * FOR PHILLIP, MAKE SURE TO CREATE AND SET THE BOOLEAN ARRAYS
	 * this randomly creates a premade puzzle for user to solve.
	 * you can use cheon's jar file that he sent in the first hw, to 
	 * use puzzles as reference to make sure that they are solvable. 
	 * Set number puzzles for size 9 and boolean arrays.
	 * size 4 is already created.
	 * for size 4 all you need to do is set the boolean arrays.
	 */
	public void preFilledPuzzle(){
		
		int random = range(size);
	
		if(size == 4){	
			switch(random) {
			case 0: 
				int [][]temp =
					{
						{0,0,0,1},
						{0,2,4,0},
						{2,1,3,0},
						{4,0,1,0}
					};
				boolean [][]tempwritable = 
						{
							{true,true,true,false},
							{true,false,false,true},
							{false,false,false,true},
							{false,true,false,true}
						};
				setGameBoard(temp);
				setWriteBoard(tempwritable);
				break;
			case 1:
				int [][]temp2 =
					{
						{0,2,1,0},
						{4,0,0,3},
						{1,0,0,2},
						{0,4,3,0}
					};
				setGameBoard(temp2);
				break;
			case 2: 
				int [][]temp3 =
					{
						{0,0,1,0},
						{3,1,4,0},
						{1,3,2,0},
						{0,0,0,1}
					};
				setGameBoard(temp3);
				break;
			case 3:
				int [][]temp4 =
					{
						{1,2,0,0},
						{0,0,2,1},
						{0,4,0,3},
						{3,0,4,0}
					};
				setGameBoard(temp4);
				break;
			case 4:
				int [][]temp5 =
					{
						{4,0,0,0},
						{0,2,3,0},
						{0,1,4,3},
						{3,0,2,0}
					};
				setGameBoard(temp5);
				break;
			default:
				break;
			}
		
		}
		else if(size == 9){
		
			switch(random) {
			case 0: 
				int [][]temp =
						{
							{0,4,9,5,0,0,7,2,0},
							{3,0,0,4,0,2,1,9,0},
							{0,0,2,0,9,3,4,0,0},
							{0,6,7,3,0,0,0,0,2},
							{0,0,0,6,7,1,0,0,0},
							{0,9,0,0,2,4,6,0,7},
							{7,0,1,9,3,0,2,8,0},
							{9,0,0,0,0,7,0,3,0},
							{0,3,6,0,0,0,9,7,1},
						};
				setGameBoard(temp);
				break;
			case 1: 
				int [][]temp2 =
						{
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0},
						};
				setGameBoard(temp2);
				break;
			case 2:
				int [][]temp3 =
					{
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0}
					};
				setGameBoard(temp3);
				break;
			case 3: 
				int [][]temp4 =
					{
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0}
					};
				setGameBoard(temp4);
				break;
			case 4: 
				int [][]temp5 =
					{
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0}
					};
				setGameBoard(temp5);
				break;
			default:
				break;
			}
		}
	}
}//endclass
