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
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        gbc.insets = new Insets(0, 20, 0, 45);
        bottomPanel.add(backButton, gbc);

        // Timer Label setup
        timerLabel = createTimerLabel();
        gbc.gridx = 1;
        gbc.weightx = 0; // The timer will not absorb extra space
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 50, 0, 50);
        bottomPanel.add(timerLabel, gbc);

        // Heart Panel setup
        JPanel heartPanel = createHeartPanel();
        gbc.gridx = 2;
        gbc.weightx = 0; // The hearts will not absorb extra space
        gbc.insets = new Insets(0, 0, 0, 0);
        bottomPanel.add(heartPanel, gbc);

        // Add the entire bottomPanel to the main frame
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = GridBagConstraints.RELATIVE; // This will place it at the end
        mainGbc.gridwidth = GridBagConstraints.REMAINDER;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottomPanel, mainGbc);

        // Start the game timer
        gameTimer = new GameTimer();
        gameTimer.start();

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
        this.heartManager = new HeartManager(screenManager, heartPanel, 3);
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
                    if (input == sudokuBoard.getSolution()[row][col]) {
                        textField.setForeground(new Color(40,166,100));
                    } else {
                        textField.setForeground(new Color(168,24,24));
                        heartManager.loseHeart(); // Decrease a heart on wrong input
                    }
                }
            }
        });
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
                    textField.setOpaque(true);
                } else {
                    textField.setText("");
                    textField.setEditable(true);
                    textField.setBackground(Color.white);
                }
            }
        }
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

