
public class Main {
    public static void main(String[] args) {

        DB.initializeDatabase();

        javax.swing.SwingUtilities.invokeLater(() -> {
            new Dashboard();
        });
    }
}