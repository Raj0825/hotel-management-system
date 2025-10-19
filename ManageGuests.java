
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageGuests extends JFrame {

    private JTable guestTable;
    private DefaultTableModel tableModel;

    public ManageGuests() {
        setTitle("Manage Guests");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Guest ID", "Name", "Phone Number", "Room Number", "Check-in Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        guestTable = new JTable(tableModel);

        loadGuestsFromDatabase();

        add(new JScrollPane(guestTable), BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnCheckIn = new JButton("Check-in New Guest");
        btnCheckIn.addActionListener(e -> {
            new CheckInGuest(this).setVisible(true);
            loadGuestsFromDatabase();
        });

        JButton btnCheckout = new JButton("Check-out Selected Guest");
        btnCheckout.addActionListener(e -> checkoutGuest());

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadGuestsFromDatabase());

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> setVisible(false));

        buttonPanel.add(btnCheckIn);
        buttonPanel.add(btnCheckout);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void checkoutGuest() {
        int selectedRow = guestTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a guest from the table to check out.", "No Guest Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int guestId = (int) tableModel.getValueAt(selectedRow, 0);
        String roomNumber = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to check out this guest?", "Confirm Checkout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DB.connect()) {

                String deleteGuestSql = "DELETE FROM guests WHERE guest_id = ?";
                try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteGuestSql)) {
                    pstmtDelete.setInt(1, guestId);
                    pstmtDelete.executeUpdate();
                }


                String updateRoomSql = "UPDATE rooms SET availability = 'Available', cleaning_status = 'Clean' WHERE room_number = ?";
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateRoomSql)) {
                    pstmtUpdate.setString(1, roomNumber);
                    pstmtUpdate.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Guest checked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);


                loadGuestsFromDatabase();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadGuestsFromDatabase() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM guests";

        try (Connection conn = DB.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("guest_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("room_number"),
                        rs.getString("check_in_date")
                });
            }
        } catch (SQLException | NullPointerException e) {

            JOptionPane.showMessageDialog(this, "Error loading data from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}