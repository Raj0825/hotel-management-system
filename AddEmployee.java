
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddEmployee extends JDialog {

    private JTextField tfName, tfPosition, tfSalary, tfPhone;

    public AddEmployee(Frame owner) {
        super(owner, "Add New Employee", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Full Name:"));
        tfName = new JTextField();
        add(tfName);
        add(new JLabel("Position:"));
        tfPosition = new JTextField();
        add(tfPosition);
        add(new JLabel("Salary:"));
        tfSalary = new JTextField();
        add(tfSalary);
        add(new JLabel("Phone Number:"));
        tfPhone = new JTextField();
        add(tfPhone);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> addEmployee());
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnAdd);
        add(btnCancel);
    }

    private void addEmployee() {
        String name = tfName.getText();
        String position = tfPosition.getText();
        String salary = tfSalary.getText();
        String phone = tfPhone.getText();

        if (name.isEmpty() || position.isEmpty() || salary.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Position, and Salary are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO employees(name, position, salary, phone) VALUES(?,?,?,?)";

        try (Connection conn = DB.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, Double.parseDouble(salary));
            pstmt.setString(4, phone);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Employee Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }

        dispose();
    }
}