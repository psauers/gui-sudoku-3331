package sudoku.dialog;


import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.net.URL;

import javax.swing.*;

import java.util.Random;

/**
 * A dialog template for playing simple Sudoku games.
 * You need to write code for three callback methods:
 * newClicked(int), numberClicked(int) and boardClicked(int,int).
 *
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {
int currentNumber =0;
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
        configureUI();
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        //setResizable(false);
    }
    
    /**
     * Callback to be invoked when a square of the board is clicked.
     * @param x 0-based row index of the clicked square.
     * @param y 0-based column index of the clicked square.
     */
    //Phillip I added currentNumber to this method in order to save it into memory for the boardclick
    private void boardClicked(int x, int y) {
    	showMessage(String.format("Board clicked: x = %d, y = %d",  x, y));
    	try{
    		int number = currentNumber;
	    	if (number == 0) {
	    		board.removeNumber(x,y);
	    		auxBoardPanel(board);
	    	}if(board.validMove(number,x,y,true)) {
	    		auxBoardPanel(board);
	    		showMessage("Success! Placed " + number + " at (" + x + "," + y + ").");
	    		currentNumber = 0;
	    		if (board.getComplete()) {
	    			endSequence();
	    		}
	    	}else {
	    		showMessage("You can't place " + number + " at (" + x + "," + y + ").");
	    	}
    	}catch(NumberFormatException nfe) {
    		showMessage("That wasn't a number...");
    		System.out.println("Error message: " + nfe.getMessage());
    	}
    }
    
    private void auxBoardPanel(Board board) {
    	boardPanel.setBoard(board);
    	boardPanel.repaint();
    }
    
    private void endSequence() {
    	showMessage("YES!");
    	String[] options = {"New 4x4","New 9x9"};
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
		default:
			break;
			}
    }
    
    /**
     * Callback to be invoked when a number button is clicked.
     * @param number Clicked number (1-9), or 0 for "X".
     */
    private void numberClicked(int number) {
    	currentNumber = number;
    	if (number != 0) {
    		showMessage("Number: " + number + ", size: " + board.getSize());
    		while (number > board.getSize()) {
    			//the number isn't within the range and should return an error.
    			number = Integer.parseInt(JOptionPane.showInputDialog("Number not witin range! Please enter another number:"));
    		}
        	showMessage("Number clicked: " + number);
        }
    	//if it didn't go into loop, number is zero.
        //allow the user to remove another 
    	else {
    		showMessage("Which number do you want to delete?");
    	}
        
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
     * Callback to be invoked when a new button is clicked.
     * If the current game is over, start a new game of the given size;
     * otherwise, prompt the user for a confirmation and then proceed
     * accordingly.
     * @param Hint Option.
     */
    
    //this method calls methods from the toolbar or menu bar
    private void helpClicked(int op) {
    	System.out.println(op);
    	switch(op) {
			case 0: board.preFilledPuzzle();
				auxBoardPanel(board);
			break;
			case 1: if(board.solvable()){
						showMessage("Yes");
					}
					else{
							showMessage("No");
						}
			break;
			//STRATEGY PATTERN
			//if board is not completely solvable, solve as much as you can
			case 2: if(board.solve(true)){
						showMessage("Yes");
					}
			else{
				board.randomNumberGenerator();
			}
				
				auxBoardPanel(board);
				endSequence();
			break;
			default:
			break;
		}
			//this.dispose();
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
    		//New Game Buttons
        	JPanel newButtons = new JPanel(new FlowLayout());
            for (JButton button: new JButton[] { new JButton("New (4x4)"), new JButton("New (9x9)")}) {
            	button.setFocusPainted(false);
            	button.addActionListener(e -> {
                    newClicked(e.getSource() == "New (4x4)" ? 4 : 9);
                });
                newButtons.add(button);
        	}
        	newButtons.setAlignmentX(CENTER_ALIGNMENT);

    	// buttons labeled 1, 2, ..., 9, and X.
    	JButton[] buttons = new JButton [board.getSize()+1];
    	JPanel numberButtons = new JPanel(new FlowLayout());
    	for (int i = 0; i < buttons.length; i++) {
    		int number = i;
    		buttons[i] = new JButton(number == 0 ? "X" : String.valueOf(number));
    	    buttons[i].setFocusPainted(false);
    	    buttons[i].setMargin(new Insets(0,2,0,2));
    		buttons[i].addActionListener(e -> numberClicked(number));
    		numberButtons.add(buttons[i]);
    	}
    	numberButtons.setAlignmentX(CENTER_ALIGNMENT);
    	
		//creates menu bar
    	JMenuBar 	menuBar =	new JMenuBar();
    	JMenu 		menu 	= 	new JMenu("HELP!");
    	int 		[] key 	=	{KeyEvent.VK_P,KeyEvent.VK_S,KeyEvent.VK_V}; 
    	
    	//creates toolbar
    	JToolBar helpBar = 	new JToolBar();
    	String [] tip =		{"Random Puzzle Start"," Solvable?","I can solve the puzzle for you"};
    	String icons [] =	{"random.png", "solvable.png","solve.png"};
    	
    	//creates the buttons that go on the tool bar
    	JButton[] helpButtons = {new JButton("Pre-filled"), new JButton("Solvable"),new JButton("Solve")};
    	String [] text = {"Pre-filled", "Solvable","Solve"};
        for (int i = 0; i < helpButtons.length; i++) {
        	int number = i;
        	helpButtons[i].addActionListener(e -> helpClicked(number));
        	helpButtons[i].setIcon(createImageIcon(icons[i]));
        	helpButtons[i].setToolTipText(tip[i]);
            //BAR
        	helpBar.add(helpButtons[i]);
            helpBar.addSeparator();
            //MENU//
            JMenuItem option = new JMenuItem(text[i], key[i] );       
            KeyStroke ctrlKeyStroke = KeyStroke.getKeyStroke("control"+" "+ Integer.toString(i));
            // Accelerator
            option.setAccelerator(ctrlKeyStroke);
            //Menu Icon
            option.setIcon(createImageIcon(icons[i]));
            option.addActionListener(e -> helpClicked(number));
            menu.add(option);

    	}
        menuBar.add(menu);
        menuBar.setAlignmentX(LEFT_ALIGNMENT);
        helpBar.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel content = new JPanel();
    	content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
    	content.add(menuBar);
        content.add(newButtons);
        content.add(numberButtons);
        content.add(helpBar);
        
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
