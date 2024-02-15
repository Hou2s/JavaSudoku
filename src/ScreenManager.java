import javax.swing.*;

public class ScreenManager {
    private JFrame currentScreen;

    public void toMainMenu() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = new MainMenu(this);
        currentScreen.setVisible(true);
    }

    public void toGameBoard() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = new SudokuGUI(this);
        currentScreen.setVisible(true);
    }

    // You will add toLevelSelection() method later when you have the LevelSelection class ready.
}
