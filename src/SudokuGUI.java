import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class SudokuGUI extends JFrame {
    private ScreenManager screenManager;
    private JTextField[][] gridFields;
    private final int gridSize = 9;
    private final int subGridSize = 3;
    private SudokuBoard sudokuBoard;
    private HeartManager heartManager;
    private JLabel timerLabel;
    private GameTimer gameTimer;

    public SudokuGUI(ScreenManager screenManager) {
        this.screenManager = screenManager;
        sudokuBoard = new SudokuBoard();
        configureWindow();
        initializeUI();
        setBoard(sudokuBoard.getBoard()); // Set the board with predefined numbers
    }

    private void configureWindow() {
        setTitle("Sudoku Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(new Color(13, 99, 122)); // Set background color
    }


    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Initialize the board panel
        JPanel boardPanel = new JPanel(new GridLayout(gridSize, gridSize));
        boardPanel.setPreferredSize(new Dimension(500, 500)); // Set the Sudoku board size
        boardPanel.setMinimumSize(new Dimension(500, 500)); // Minimum size of the Sudoku board
        boardPanel.setBackground(new Color(212, 242, 250)); // A subtle blue for the grid background

        // Initialize the grid fields and add them to the board panel
        gridFields = new JTextField[gridSize][gridSize];
        // Create text fields for the grid
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                JTextField textField = new JTextField();
                textField.setOpaque(false);
                textField.setHorizontalAlignment(JTextField.CENTER);
                textField.setFont(new Font("Arial", Font.BOLD, 25));

                // Add a thicker border to visually separate 3x3 squares
                int top = (row % subGridSize == 0) ? 2 : 1;
                int left = (col % subGridSize == 0) ? 2 : 1;
                int bottom = (row == gridSize - 1) ? 2 : 1;
                int right = (col == gridSize - 1) ? 2 : 1;
                textField.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                setupTextField(textField, row, col);
                gridFields[row][col] = textField;
                boardPanel.add(textField);
            }
        }

        // Set board panel constraints and add to the layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        add(boardPanel, gbc);

        // Initialize Sudoku title label
        JLabel titleLabel = new JLabel("Sudoku", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        // Positioning title label
        gbc.gridy = 1; // Placing it below the Sudoku board
        gbc.insets = new Insets(20, 0, 0, 0); // Some padding above the title
        add(titleLabel, gbc);

        initializeBottomPanel();
    }

    private void initializeBottomPanel() {
        // Create a new panel for the bottom components with a GridBagLayout
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setOpaque(false); // if you want to make it transparent
        GridBagConstraints gbc = new GridBagConstraints();

        // Back Button setup
        JButton backButton = createBackButton();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 0, 50);
        bottomPanel.add(backButton, gbc);

        // Timer Label setup
        timerLabel = createTimerLabel();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        bottomPanel.add(timerLabel, gbc);

        // Heart Panel setup
        JPanel heartPanel = createHeartPanel();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 15);
        bottomPanel.add(heartPanel, gbc);

        // Add the entire bottomPanel to the main frame
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = GridBagConstraints.RELATIVE; // This will place it at the end
        mainGbc.gridwidth = GridBagConstraints.REMAINDER;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.weighty = 0;
        add(bottomPanel, mainGbc);

        // Start the game timer
        gameTimer = new GameTimer();
        gameTimer.start();

        bottomPanel.revalidate();
        bottomPanel.repaint();

    }

    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.addActionListener((ActionEvent e) -> screenManager.toMainMenu());
        return backButton;
    }

    private JLabel createTimerLabel() {
        JLabel timerLabel = new JLabel("00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 25));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return timerLabel;
    }

    private JPanel createHeartPanel() {
        JPanel heartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        heartPanel.setOpaque(false); // Make the panel transparent
        this.heartManager = new HeartManager(this,screenManager, heartPanel, 3);
        return heartPanel;
    }

    private void setupTextField(JTextField textField, int row, int col) {
        // Restrict to single numeric digit
        ((PlainDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String newText = textField.getText() + text;
                if ((newText.length() <= 1) && newText.matches("[1-9]?")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Add a key listener to validate input against the solution
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = textField.getText();
                if (!text.isEmpty()) {
                    int input = Integer.parseInt(text);
                    sudokuBoard.getBoard()[row][col] = input; //Update the board

                    if (input == sudokuBoard.getSolution()[row][col]) {
                        textField.setForeground(new Color(40,166,100));
                    }
                    else {
                        textField.setForeground(new Color(168,24,24));
                        heartManager.loseHeart(); // Decrease a heart on wrong input
                    }
                    checkBoardAndShowPopup();
                }
            }
        });
    }

    public boolean isBoardFullAndCorrect() {
        int[][] currentBoard = sudokuBoard.getBoard(); // Get the current board
        int[][] solution = sudokuBoard.getSolution(); // Get the solution for comparison

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                // Check if the cell is empty or does not match the solution
                if (currentBoard[row][col] == 0 || currentBoard[row][col] != solution[row][col]) {
                    return false; // The board is either not full or incorrect
                }
            }
        }
        return true; // The board is full and matches the solution
    }

    public void checkBoardAndShowPopup() {
        if (isBoardFullAndCorrect()) {
            // Stop the timer first
            stopGameTimer();

            // Show the congratulations popup
            JDialog congratulationsDialog = new JDialog(this, "Congratulations!", true);
            congratulationsDialog.setLayout(new FlowLayout());
            JLabel messageLabel = new JLabel("Congratulations! You've completed the puzzle!");
            JButton newGameButton = new JButton("New Game");
            JButton mainMenuButton = new JButton("Main Menu");

            newGameButton.addActionListener(e -> {
                congratulationsDialog.dispose();
                // Reset the board and restart the game here
                resetGame();
            });

            mainMenuButton.addActionListener(e -> {
                congratulationsDialog.dispose();
                screenManager.toMainMenu(); // Assuming this method navigates to the main menu
            });

            congratulationsDialog.add(messageLabel);
            congratulationsDialog.add(newGameButton);
            congratulationsDialog.add(mainMenuButton);
            congratulationsDialog.setSize(300, 150); // Set the size of the popup
            congratulationsDialog.setLocationRelativeTo(this); // Center the dialog
            congratulationsDialog.setVisible(true);
        }
    }


    private void setBoard(int[][] board) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                JTextField textField = gridFields[row][col];
                int value = board[row][col];
                if (value != 0) {
                    textField.setText(String.valueOf(value));
                    textField.setEditable(false);
                    textField.setBackground(Color.lightGray);
                    textField.setFocusable(false);
                    textField.setOpaque(true);
                } else {
                    textField.setText("");
                    textField.setEditable(true);
                    textField.setBackground(new Color(212, 242, 250));
                    textField.setForeground(Color.BLACK);
                    textField.setOpaque(true);
                }
            }
        }
    }

    public void stopGameTimer() {
        gameTimer.stop();
    }

    private void resetGame() {
        sudokuBoard.resetBoard(); // You'll need to implement this method in your SudokuBoard class
        setBoard(sudokuBoard.getBoard());
        gameTimer.start();
    }


    // Inner class for the game timer
    private class GameTimer implements ActionListener {
        private Timer timer;
        private long startTime;

        public GameTimer() {
            // Directly access timerLabel from the enclosing SudokuGUI class
            this.startTime = System.currentTimeMillis();
            timer = new Timer(1000, this);
        }

        public void start() {
            startTime = System.currentTimeMillis(); // Reset the start time every time the timer starts
            timer.start();
        }

        public void stop() {
            timer.stop();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            long elapsedMillis = System.currentTimeMillis() - startTime;
            timerLabel.setText(formatTime(elapsedMillis)); // Directly access timerLabel here
        }

        private String formatTime(long millis) {
            long seconds = (millis / 1000) % 60;
            long minutes = (millis / (1000 * 60)) % 60;

            return String.format("%02d:%02d", minutes, seconds);
        }
    }


}

