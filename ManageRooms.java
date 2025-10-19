
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class ManageRooms extends JFrame {

    private JTable roomTable;
    private DefaultTableModel tableModel;

    public ManageRooms() {
        setTitle("Manage Rooms");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Room Number", "Availability", "Cleaning Status", "Price", "Bed Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomTable = new JTable(tableModel);

        loadRoomsFromDatabase();

        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAddRoom = new JButton("Add New Room");
        btnAddRoom.addActionListener(e -> {
            new AddRoom(this).setVisible(true);
            loadRoomsFromDatabase();
        });

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadRoomsFromDatabase());

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> setVisible(false));

        buttonPanel.add(btnAddRoom);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadRoomsFromDatabase() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM rooms";

        try (Connection conn = DB.connect();
             Statement stmt  = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("room_number"),
                        rs.getString("availability"),
                        rs.getString("cleaning_status"),
                        rs.getDouble("price"),
                        rs.getString("bed_type")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {

            JOptionPane.showMessageDialog(this, "Database Connection Failed. Check your DB.java credentials and ensure the MySQL server is running.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}