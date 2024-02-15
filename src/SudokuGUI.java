import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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



    public SudokuGUI(ScreenManager screenManager) {
        this.screenManager = screenManager;
        sudokuBoard = new SudokuBoard(); // Create a new Sudoku board
        setTitle("Sudoku Game");
        setSize(600, 700);
        getContentPane().setBackground(new Color(13, 99, 122)); // A shade of blue for the background
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeUI(); // Initialize the Sudoku board UI
        setBoard(sudokuBoard.getBoard()); // Set the board with predefined numbers
    }

    //
    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel boardPanel = new JPanel(new GridLayout(gridSize, gridSize));
        boardPanel.setPreferredSize(new Dimension(500, 500)); // Set the Sudoku board size
        boardPanel.setMinimumSize(new Dimension(500, 500)); // Minimum size of the Sudoku board
        boardPanel.setBackground(new Color(212, 242, 250)); // A subtle blue for the grid background

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(boardPanel, gbc);

        // Sudoku title label
        JLabel titleLabel = new JLabel("Sudoku", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        // Positioning the title label
        gbc.gridy = 1; // Placing it below the Sudoku board
        gbc.insets = new Insets(10, 0, 0, 0); // Some padding above the title
        add(titleLabel, gbc);

        addBackButton(); // Add the Back button
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.addActionListener((ActionEvent e) -> screenManager.toMainMenu());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0); // Some padding above the back button
        add(backButton, gbc);
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
                    }
                }
            }
        });
    }


}