
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {



    public static Connection connect() {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/hotel_db";


        String user = "root";


        String password = "root1";

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to MySQL has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public static void initializeDatabase() {
        String createRoomsTable = "CREATE TABLE IF NOT EXISTS rooms ("
                + " room_number VARCHAR(10) PRIMARY KEY,"
                + " availability VARCHAR(20) NOT NULL,"
                + " cleaning_status VARCHAR(20) NOT NULL,"
                + " price DECIMAL(10, 2) NOT NULL,"
                + " bed_type VARCHAR(20) NOT NULL"
                + ");";

        String createGuestsTable = "CREATE TABLE IF NOT EXISTS guests ("
                + " guest_id INT PRIMARY KEY AUTO_INCREMENT,"
                + " name VARCHAR(100) NOT NULL,"
                + " phone VARCHAR(20) NOT NULL,"
                + " room_number VARCHAR(10) NOT NULL,"
                + " check_in_date DATE NOT NULL,"
                + " FOREIGN KEY (room_number) REFERENCES rooms (room_number)"
                + ");";

        String createEmployeesTable = "CREATE TABLE IF NOT EXISTS employees ("
                + " employee_id INT PRIMARY KEY AUTO_INCREMENT,"
                + " name VARCHAR(100) NOT NULL,"
                + " position VARCHAR(50) NOT NULL,"
                + " salary DECIMAL(10, 2) NOT NULL,"
                + " phone VARCHAR(20)"
                + ");";


        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {


            stmt.execute(createRoomsTable);
            stmt.execute(createGuestsTable);
            stmt.execute(createEmployeesTable);
            System.out.println("Database tables created or already exist.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Database connection failed. Check your credentials and ensure the MySQL server is running.");
        }
    }
}