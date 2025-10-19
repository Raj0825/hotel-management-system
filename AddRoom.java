
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class AddRoom extends JDialog {

    private JTextField tfRoomNumber, tfPrice;
    private JComboBox<String> cbAvailability, cbCleaningStatus, cbBedType;
    private DefaultTableModel tableModel;


    public AddRoom(DefaultTableModel model) {
        this(model, null);
    }


    public AddRoom(Frame owner) {
        this(null, owner);
    }


    private AddRoom(DefaultTableModel model, Frame owner) {
        super(owner, "Add New Room", true);
        this.tableModel = model;

        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Room Number:"));
        tfRoomNumber = new JTextField();
        add(tfRoomNumber);
        add(new JLabel("Price:"));
        tfPrice = new JTextField();
        add(tfPrice);
        add(new JLabel("Availability:"));
        cbAvailability = new JComboBox<>(new String[]{"Available", "Occupied"});
        add(cbAvailability);
        add(new JLabel("Cleaning Status:"));
        cbCleaningStatus = new JComboBox<>(new String[]{"Clean", "Dirty"});
        add(cbCleaningStatus);
        add(new JLabel("Bed Type:"));
        cbBedType = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        add(cbBedType);

        JButton btnAdd = new JButton("Add Room");
        btnAdd.addActionListener(e -> addRoom());
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnAdd);
        add(btnCancel);
    }

    private void addRoom() {
        String roomNumber = tfRoomNumber.getText();
        String priceStr = tfPrice.getText();
        String availability = (String) cbAvailability.getSelectedItem();
        String cleaningStatus = (String) cbCleaningStatus.getSelectedItem();
        String bedType = (String) cbBedType.getSelectedItem();

        if (roomNumber.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room Number and Price cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO rooms(room_number, availability, cleaning_status, price, bed_type) VALUES(?,?,?,?,?)";

        try (Connection conn = DB.connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, availability);
            pstmt.setString(3, cleaningStatus);
            pstmt.setDouble(4, Double.parseDouble(priceStr));
            pstmt.setString(5, bedType);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Room Added Successfully to Database!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding room: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }


        dispose();
    }
}