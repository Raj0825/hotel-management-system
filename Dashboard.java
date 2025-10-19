
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Hotel Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        String imagePath = "icons" + File.separator + "hotel_main.png";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            ImageIcon backgroundIcon = new ImageIcon(imagePath);
            Image scaledImage = backgroundIcon.getImage().getScaledInstance(1550, 1220, Image.SCALE_SMOOTH);
            JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
            add(backgroundLabel);

            JLabel titleLabel = new JLabel("WELCOME TO THE HOTEL PARADISE");
            titleLabel.setBounds(150, 50, 1000, 50);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
            backgroundLabel.add(titleLabel);

        } else {

            System.out.println("Image not found at path: " + imageFile.getAbsolutePath());
            getContentPane().setBackground(Color.DARK_GRAY);
            JLabel errorLabel = new JLabel("Image not found. Check the 'icons' folder.", SwingConstants.CENTER);
            errorLabel.setForeground(Color.WHITE);
            errorLabel.setFont(new Font("Arial", Font.BOLD, 24));
            add(errorLabel);
        }




        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.DARK_GRAY);
        setJMenuBar(menuBar);

        JMenu managementMenu = new JMenu("Hotel Management");
        managementMenu.setForeground(Color.WHITE);
        menuBar.add(managementMenu);

        JMenuItem roomsItem = new JMenuItem("Manage Rooms");
        roomsItem.addActionListener(e -> new ManageRooms().setVisible(true));
        managementMenu.add(roomsItem);

        JMenuItem guestsItem = new JMenuItem("Manage Guests");
        guestsItem.addActionListener(e -> new ManageGuests().setVisible(true));
        managementMenu.add(guestsItem);

        JMenu adminMenu = new JMenu("Admin");
        adminMenu.setForeground(Color.WHITE);
        menuBar.add(adminMenu);

        JMenuItem employeesItem = new JMenuItem("Manage Employees");
        employeesItem.addActionListener(e -> new ManageEmployees().setVisible(true));
        adminMenu.add(employeesItem);

        setVisible(true);
    }
}