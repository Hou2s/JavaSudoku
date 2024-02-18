import javax.swing.*;
import java.awt.*;

public class HeartManager {
    private int lives;
    private JLabel[] heartLabels;
    private JPanel panel;
    private ScreenManager screenManager;

    public int getLives() {
        return this.lives;
    }


    public HeartManager(ScreenManager screenManager, JPanel parentPanel, int initialLives) {
        this.screenManager = screenManager;
        this.lives = initialLives;
        this.heartLabels = new JLabel[initialLives];
        this.panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Reduced horizontal gap to 5 pixels and vertical gap to 0 pixels
        panel.setOpaque(false); // Make the panel transparent

        for (int i = 0; i < initialLives; i++) {
            ImageIcon heartIcon = new ImageIcon("C:\\Users\\Thelegend27\\IdeaProjects\\Sudoku1\\src\\Assets\\Heart.jpg");
            Image heartImg = heartIcon.getImage();
            Image newimg = heartImg.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // Scale it to 50x50 (change this as needed)
            heartIcon = new ImageIcon(newimg);  // Transform it back
            heartLabels[i] = new JLabel(heartIcon);
            panel.add(heartLabels[i]);
        }

            parentPanel.add(panel);
    }

    public void loseHeart() {
        if (lives > 0) {
            lives--;
            heartLabels[lives].setVisible(false);
            if (lives == 0) {
                JOptionPane.showMessageDialog(null, "Game Over! You've run out of lives.");
                screenManager.toMainMenu();
            }
        }
    }
}
