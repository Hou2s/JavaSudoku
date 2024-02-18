import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {
    private ScreenManager screenManager;

    public MainMenu(ScreenManager screenManager) {
        this.screenManager = screenManager;
        setTitle("Sudoku Main Menu");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(13, 99, 122)); // A shade of blue for the background
        setLocationRelativeTo(null);
        initializeMenu();
    }

    private void initializeMenu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0); // Some padding between buttons

        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 30));
        playButton.addActionListener((ActionEvent e) -> screenManager.toGameBoard());

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        add(playButton, gbc);
        add(exitButton, gbc); // Add the exit button below the play button
    }
}
