package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCriminalPage extends JFrame implements ActionListener {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Darling@15";

    private JTextField tfFullName;
    private JComboBox<String> cbGender;
    private JTextField tfAge;
    private JTextField tfNationality;
    private JTextField tfCaseNumber;
    private JComboBox<String> cbBailStatus;
    private JComboBox<String> cbJailTerm;
    private JTextField tfOffense;
    private JButton btnAddCriminal;
    private JButton btnBack;

    public AddCriminalPage() {
        setTitle("Add Criminal");

        setLayout(new GridBagLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] genders = { "Male", "Female", "Other" };
        cbGender = new JComboBox<>(genders);

        cbBailStatus = new JComboBox<>(new String[] { "Granted", "Denied", "Pending" });

        cbJailTerm = new JComboBox<>(
                new String[] { "1-5 years", "5-10 years", "10-20 years", "20-50 years", "50-100 years" });

        btnAddCriminal = new JButton("Add Criminal");
        btnAddCriminal.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Full Name:"), gbc);
        tfFullName = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tfFullName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(cbGender, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Age:"), gbc);
        tfAge = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(tfAge, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Nationality:"), gbc);
        tfNationality = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(tfNationality, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Case/File Number:"), gbc);
        tfCaseNumber = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(tfCaseNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Bail Status:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(cbBailStatus, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Jail Term:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(cbJailTerm, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Type of Offense:"), gbc);
        tfOffense = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(tfOffense, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 0, 0);
        add(btnAddCriminal, gbc);
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 0, 0, 0);
        add(btnBack, gbc);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddCriminal) {
            String fullName = tfFullName.getText();
            String gender = (String) cbGender.getSelectedItem();
            int age;
            try {
                age = Integer.parseInt(tfAge.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nationality = tfNationality.getText();
            String caseNumber = tfCaseNumber.getText();
            String bailStatus = (String) cbBailStatus.getSelectedItem();
            String jailTerm = (String) cbJailTerm.getSelectedItem();
            String offense = tfOffense.getText();

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "INSERT INTO criminals (full_name, gender, age, nationality, case_number, bail_status, jail_term, offense) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, fullName);
                    statement.setString(2, gender);
                    statement.setInt(3, age);
                    statement.setString(4, nationality);
                    statement.setString(5, caseNumber);
                    statement.setString(6, bailStatus);
                    statement.setString(7, jailTerm);
                    statement.setString(8, offense);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Criminal added successfully");
                        tfFullName.setText("");
                        tfAge.setText("");
                        tfNationality.setText("");
                        tfCaseNumber.setText("");
                        tfOffense.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add criminal", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add criminal", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnBack) {
            dispose(); // Close the AddCriminalPage frame
        }
    }

}
