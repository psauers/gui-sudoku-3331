package sudoku.dialog;
/**
*	Edna
*	Phillip Sauers
*	CS 3331
*	3/1/18
*	HW 2
**/

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.Random;

import sudoku.model.Board;

/**
 * A dialog template for playing simple Sudoku games.
 * You need to write code for three callback methods:
 * newClicked(int), numberClicked(int) and boardClicked(int,int).
 *
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_SIZE = new Dimension(310, 430);

    private final static String IMAGE_DIR = "/image/";
    
    private JFrame frame = new JFrame();

    /** Sudoku board. */
    private Board board;

    /** Special panel to display a Sudoku board. */
    private BoardPanel boardPanel;

    /** Message bar to display various messages. */
    private JLabel msgBar = new JLabel("");
    
    /** Toggle for button/manual entry. */
    private boolean toggle;
    
    /** Another toggle because I can't figure out buttons */
    private boolean buttonReady;
    private int currentX;
    private int currentY;
    
    /** Create a new dialog. */
    public SudokuDialog() {
    	this(DEFAULT_SIZE, 4);
    }
    
    public SudokuDialog(int size) {
    	this(DEFAULT_SIZE, size);
    }
    
    /** Create a new dialog of the given screen dimension.
     *   
     *  @param dim dimension
     *  @param size size of board
     *  */
    public SudokuDialog(Dimension dim, int size) {
        super("Sudoku");
        setSize(dim);
        this.board = new Board(size);
        this.boardPanel = new BoardPanel(board, this::boardClicked);
        this.toggle = true;
        this.buttonReady = false;
        configureUI();
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        //setResizable(false);
        JOptionPane.showMessageDialog(frame,
        	    "Press 'T' to toggle button/user input.",
        	    "Hint",
        	    JOptionPane.PLAIN_MESSAGE);
    }
    
    public void setToggle(boolean toggle) {
    	this.toggle = toggle;
    }
    
    public boolean getToggle() {
    	return this.toggle;
    }
    
    /**
     * Callback to be invoked when a square of the board is clicked.
     * @param x 0-based row index of the clicked square.
     * @param y 0-based column index of the clicked square.
     */
    private void boardClicked(int x, int y) {
    	showMessage(String.format("Board clicked: x = %d, y = %d",  x, y));
    	if (toggle) {
	    	try{
	    		int number = Integer.parseInt(JOptionPane.showInputDialog("Please enter a number: (0 to delete)"));
		    	if (number == 0) {
		    		int tempNum = board.getGameBoard()[y][x];
		    		if (tempNum != 0) {
			    		deleteNumber(x,y);
		    		}else {
		    			showMessage("You can't delete nothing!");
		    		}
		    	}else{
		    		addNumber(x,y,number);
		    	}
	    	}catch(NumberFormatException nfe) {
	    		showMessage("That wasn't a number...");
	    		System.out.println("Error message: " + nfe.getMessage());
	    	}
    	}else {
    		this.buttonReady = true;
    		this.currentX = x;
    		this.currentY = y;
    	}
    }
    
    public boolean addNumber(int x, int y, int number) {
    	if(board.makeMove(x,y,number)) {
    		auxBoardPanel(board);
    		showMessage("Success! Placed " + number + " at (" + x + "," + y + ").");
    		if (board.getComplete()) {
    			endSequence();
    		}return true;
    	}
    	
    	showMessage("Couldn't place " + number + " at (" + x + "," + y + ").");
    	return false;
    }
    
    private void auxBoardPanel(Board board) {
    	boardPanel.setBoard(board);
    	boardPanel.repaint();
    }
    
    private void endSequence() {
    	showMessage("YES!");
    	String[] options = {"New 4x4","New 9x9","Quit"};
    	int response = JOptionPane.showOptionDialog(null, 
    			("CONGRATULATIONS!\n\n Total moves: " + board.getMoves()),
    			"WINNER!",
    			JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
    	        null, options, options[0]);
    	switch(response) {
    	case 0: new SudokuDialog(4);
    		this.dispose();
    		break;
    	case 1: new SudokuDialog(9);
    		this.dispose();
    		break;
    	case 2: System.exit(0);
    		break;
    	}
    }
    
    /**
     * Callback to be invoked when a number button is clicked.
     * @param number Clicked number (1-9), or 0 for "X".
     */
    private void numberClicked(int number) {
    	if (number == 5) {
    		//toggle for the button stuff
    		setToggle(!getToggle());
    		showMessage("Set toggle to " + getToggle());
    	}else if (this.buttonReady) {
    		int x = this.currentX;
    		int y = this.currentY;
	    	if (number == 0) {
	    		deleteNumber(x,y);
	    	}else {
	    		addNumber(x,y,number);
	    	}this.buttonReady = false;
    	}else {
    		showMessage("You have to select a square first!");
    	}
    }
    
    private void deleteNumber(int x, int y) {
    	board.removeNumber(x, y);
    	showMessage("Number deleted!");
    	auxBoardPanel(board);
    }
    
    /**
     * Callback to be invoked when a new button is clicked.
     * If the current game is over, start a new game of the given size;
     * otherwise, prompt the user for a confirmation and then proceed
     * accordingly.
     * @param size Requested puzzle size, either 4 or 9.
     */
    private void newClicked(int size) {
    	boolean newGame = false;
    	int option;
        //if game isn't over, ask user to confirm.
    	//prompt user for confirmation
		option = JOptionPane.showConfirmDialog(frame, "NEW GAME?!?!?!?!?", "Una pregunta...", JOptionPane.YES_NO_OPTION);
		if (option == 1) {
			//And now a random message generator because why not?
			Random rand = new Random();
			
			int randInt = rand.nextInt(5);
			
			switch(randInt) {
			case 0: showMessage("One day is plenty of time!");
				break;
			case 1: showMessage("Okay! Let's do this... right?!");
				break;
			case 2: showMessage("I wish I was written in Python.");
				break;
			case 3: showMessage("Why did I implement this feature?");
				break;
			case 4: showMessage("The world may never know...");
				break;
			}
			
		}else{
			newGame = true;
		}
		
		if (newGame) {
			showMessage("New size: " + size);
			new SudokuDialog(size);
			this.dispose();
		}
	
    }

    /**
     * Display the given string in the message bar.
     * @param msg Message to be displayed.
     */
    private void showMessage(String msg) {
        msgBar.setText(msg);
    }

    /** Configure the UI. */
    private void configureUI() {
        setIconImage(createImageIcon("sudoku.png").getImage());
        setLayout(new BorderLayout());
        
        JPanel buttons = makeControlPanel();
        // border: top, left, bottom, right
        buttons.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        add(buttons, BorderLayout.NORTH);
        
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        board.setLayout(new GridLayout(1,1));
        board.add(boardPanel);
        add(board, BorderLayout.CENTER);
        
        msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
        add(msgBar, BorderLayout.SOUTH);
    }
      
    /** Create a control panel consisting of new and number buttons. 
     * @return content 
     * */
    private JPanel makeControlPanel() {
    	JPanel newButtons = new JPanel(new FlowLayout());
        JButton new4Button = new JButton("New (4x4)");
        for (JButton button: new JButton[] { new4Button, new JButton("New (9x9)") }) {
        	button.setFocusPainted(false);
            button.addActionListener(e -> {
                newClicked(e.getSource() == new4Button ? 4 : 9);
            });
            newButtons.add(button);
    	}
    	newButtons.setAlignmentX(LEFT_ALIGNMENT);
        
    	// buttons labeled 1, 2, ..., 9, and X.
    	JPanel numberButtons = new JPanel(new FlowLayout());
    	int maxNumber = board.getSize() + 2;
    	for (int i = 1; i <= maxNumber; i++) {
    		JButton button;
            int number = i % maxNumber;
            if (number == board.getSize() + 1) {
            	button = new JButton(number == 5 ? "T" : String.valueOf(number));
            }else if (number == board.getSize() + 2) {
            	button = new JButton(number == 6 ? "S" : String.valueOf(number));
            }else {
            	button = new JButton(number == 0 ? "X" : String.valueOf(number));
            }button.setFocusPainted(false);
            button.setMargin(new Insets(0,2,0,2));
            button.addActionListener(e -> numberClicked(number));
    		numberButtons.add(button);
    	}
    	numberButtons.setAlignmentX(LEFT_ALIGNMENT);
    	JPanel content = new JPanel();
    	content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(newButtons);
        content.add(numberButtons);
        return content;
    }

    /** Create an image icon from the given image file. 
     * @param filename path to file
     * @return null returns image icon
     * */
    private ImageIcon createImageIcon(String filename) {
        URL imageUrl = getClass().getResource(IMAGE_DIR + filename);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        }
        return null;
    }

    public static void main(String[] args) {
        new SudokuDialog();
    }
}
