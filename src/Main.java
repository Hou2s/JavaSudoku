import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenManager screenManager = new ScreenManager();
            screenManager.toMainMenu();
        });
    }
}
