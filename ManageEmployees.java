
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageEmployees extends JFrame {

    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public ManageEmployees() {
        setTitle("Manage Employees");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Employee ID", "Name", "Position", "Salary", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);

        loadEmployeesFromDatabase();

        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAddEmployee = new JButton("Add New Employee");
        btnAddEmployee.addActionListener(e -> {
            new AddEmployee(this).setVisible(true);
            loadEmployeesFromDatabase(); // Refresh after adding
        });

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadEmployeesFromDatabase());

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> setVisible(false));

        buttonPanel.add(btnAddEmployee);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEmployeesFromDatabase() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM employees";

        try (Connection conn = DB.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getString("phone")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed. Check DB.java.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}