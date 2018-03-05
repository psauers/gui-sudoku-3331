package sudoku.dialog;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import sudoku.model.Board;

/**
 * A special panel class to display a Sudoku board modeled by the
 * {@link sudoku.model.Board} class. You need to write code for
 * the paint() method.
 *
 * @see sudoku.model.Board
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
    
	public interface ClickListener {
		
		/** Callback to notify clicking of a square. 
		 * 
		 * @param x 0-based column index of the clicked square
		 * @param y 0-based row index of the clicked square
		 */
		void clicked(int x, int y);
	}
	
    /** Background color of the board. */
	private static final Color boardColor = new Color(247, 223, 150);

    /** Board to be displayed. */
    private Board board;

    /** Width and height of a square in pixels. */
    private int squareSize;

    /** Create a new board panel to display the given board. */
    public BoardPanel(Board board, ClickListener listener) {
        this.board = board;
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	int xy = locateSquaree(e.getX(), e.getY());
            	if (xy >= 0) {
            		listener.clicked((xy / 100) + 1, (xy % 100) + 1);
            	}
            }
        });
    }

    /** Set the board to be displayed. */
    public void setBoard(Board board) {
    	this.board = board;
    }
    
    /**
     * Given a screen coordinate, return the indexes of the corresponding square
     * or -1 if there is no square.
     * The indexes are encoded and returned as x*100 + y, 
     * where x and y are 0-based column/row indexes.
     */
    private int locateSquaree(int x, int y) {
    	if (x < 0 || x > board.getSize() * squareSize
    			|| y < 0 || y > board.getSize() * squareSize) {
    		return -1;
    	}
    	int xx = x / squareSize;
    	int yy = y / squareSize;
    	return xx * 100 + yy;
    }

    /** Draw the associated board. */
    @Override
    public void paint(Graphics g) {
        super.paint(g); 

        // determine the square size
        Dimension dim = getSize();
        squareSize = Math.min(dim.width, dim.height) / board.getSize();

        // draw background
        //final Color oldColor = g.getColor(); this is never used?
        g.setColor(boardColor);
        g.fillRect(0, 0, squareSize * board.getSize(), squareSize * board.getSize());

        // WRITE YOUR CODE HERE ...
        // i.e., draw grid and squares.
        
        int[][] board1 = this.board.getGameBoard();
        
        //DRAWS THE ROWS AND COLUMNS
        int width = getSize().width;
        int height = getSize().height;
        
        for (int i = 0; i < board1.length; i++) {
        	g.setColor(Color.gray);
        	if (i % (int) Math.sqrt((double) board1.length) == 0) {
        		g.setColor(Color.black);
        	}
        	//g.drawline(x1, y1, x2, y2);
        	//draws the line from (x1,y1) to (x2,y2)
        	//using the current color (defined at line 96)
        	g.drawLine(0, i * squareSize, height, i * squareSize);	//rows
        	g.drawLine(i * squareSize, 0, i * squareSize, width);	//columns
        }
        int[][] numBoard = board.getGameBoard();
        
        //TODO: Draw the numbers
        int midpoint = squareSize / 2;
        for (int i = 1; i < numBoard.length; i++) {
        	for (int j = 1; j < numBoard[0].length; j++) {
        		if (numBoard[i][j] != 0) {
        			g.drawString(String.valueOf(numBoard[i][j]),((j-1)*squareSize) + midpoint,((i-1)*squareSize) + midpoint);
        		}
        	}
        }
    }

}
