
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckInGuest extends JDialog {

    private JTextField tfName, tfPhone, tfRoomNumber;
    private DefaultTableModel tableModel;

    public CheckInGuest(DefaultTableModel model) { this(model, null); }
    public CheckInGuest(Frame owner) { this(null, owner); }

    private CheckInGuest(DefaultTableModel model, Frame owner) {
        super(owner, "Check-in New Guest", true);
        this.tableModel = model;
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Full Name:"));
        tfName = new JTextField();
        add(tfName);
        add(new JLabel("Phone Number:"));
        tfPhone = new JTextField();
        add(tfPhone);
        add(new JLabel("Assigned Room No:"));
        tfRoomNumber = new JTextField();
        add(tfRoomNumber);
        JButton btnCheckIn = new JButton("Check-in");
        btnCheckIn.addActionListener(e -> checkIn());
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnCheckIn);
        add(btnCancel);
    }

    private void checkIn() {
        String name = tfName.getText();
        String phone = tfPhone.getText();
        String roomNumber = tfRoomNumber.getText();

        if (name.isEmpty() || phone.isEmpty() || roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DB.connect()) {

            String checkRoomSql = "SELECT availability FROM rooms WHERE room_number = ?";
            try (PreparedStatement pstmtCheck = conn.prepareStatement(checkRoomSql)) {
                pstmtCheck.setString(1, roomNumber);
                ResultSet rs = pstmtCheck.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Error: Room " + roomNumber + " does not exist.", "Invalid Room", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String availability = rs.getString("availability");
                if (!availability.equalsIgnoreCase("Available")) {
                    JOptionPane.showMessageDialog(this, "Error: Room " + roomNumber + " is currently occupied or unavailable.", "Room Not Available", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }


            String updateRoomSql = "UPDATE rooms SET availability = 'Occupied' WHERE room_number = ?";
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateRoomSql)) {
                pstmtUpdate.setString(1, roomNumber);
                pstmtUpdate.executeUpdate();
            }


            String insertGuestSql = "INSERT INTO guests(name, phone, room_number, check_in_date) VALUES(?,?,?,?)";
            String checkInDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertGuestSql)) {
                pstmtInsert.setString(1, name);
                pstmtInsert.setString(2, phone);
                pstmtInsert.setString(3, roomNumber);
                pstmtInsert.setString(4, checkInDate);
                pstmtInsert.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Guest Checked-In Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}