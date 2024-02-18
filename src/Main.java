import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SoundManager soundManager = new SoundManager("C:\\Users\\Thelegend27\\IdeaProjects\\Sudoku1\\src\\Assets\\Harmony.wav");
            soundManager.loop();
            ScreenManager screenManager = new ScreenManager();
            screenManager.toMainMenu();
        });
    }
}
