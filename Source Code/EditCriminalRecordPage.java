package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditCriminalRecordPage extends JFrame implements ActionListener {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Darling@15";

    private JTextField tfCaseNumber;
    private JTextField tfFullName;
    private JComboBox<String> cbGender;
    private JTextField tfAge;
    private JTextField tfNationality;
    private JComboBox<String> cbBailStatus;
    private JComboBox<String> cbJailTerm;
    private JTextField tfOffense;
    private JButton btnEditCriminal;

    public EditCriminalRecordPage() {
        setTitle("Edit Criminal");
        setSize(400, 300);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to maximize


        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] genders = {"Male", "Female", "Other"};
        cbGender = new JComboBox<>(genders);

        cbBailStatus = new JComboBox<>(new String[]{"Granted", "Denied", "Pending"});

        cbJailTerm = new JComboBox<>(new String[]{"1-5 years", "5-10 years", "10-20 years", "20-50 years", "50-100 years"});

        btnEditCriminal = new JButton("Edit Criminal");
        btnEditCriminal.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(new JLabel("Case/File Number:"), gbc);
        tfCaseNumber = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(tfCaseNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(new JLabel("Full Name:"), gbc);
        tfFullName = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(tfFullName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(cbGender, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(new JLabel("Age:"), gbc);
        tfAge = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPane.add(tfAge, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPane.add(new JLabel("Nationality:"), gbc);
        tfNationality = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPane.add(tfNationality, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPane.add(new JLabel("Bail Status:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPane.add(cbBailStatus, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPane.add(new JLabel("Jail Term:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPane.add(cbJailTerm, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPane.add(new JLabel("Type of Offense:"), gbc);
        tfOffense = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        contentPane.add(tfOffense, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(btnEditCriminal, gbc);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditCriminal) {
            String caseNumber = tfCaseNumber.getText();
            String fullName = tfFullName.getText();
            String gender = (String) cbGender.getSelectedItem();
            String ageText = tfAge.getText();
            String nationality = tfNationality.getText();
            String bailStatus = (String) cbBailStatus.getSelectedItem();
            String jailTerm = (String) cbJailTerm.getSelectedItem();
            String offense = tfOffense.getText();

            // Check if all fields are filled
            if (caseNumber.isEmpty() || fullName.isEmpty() || ageText.isEmpty() || nationality.isEmpty() || offense.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "UPDATE criminals SET full_name = ?, gender = ?, age = ?, nationality = ?, bail_status = ?, jail_term = ?, offense = ? WHERE case_number = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, fullName);
                    statement.setString(2, gender);
                    statement.setInt(3, age);
                    statement.setString(4, nationality);
                    statement.setString(5, bailStatus);
                    statement.setString(6, jailTerm);
                    statement.setString(7, offense);
                    statement.setString(8, caseNumber);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Criminal updated successfully");
                        tfCaseNumber.setText("");
                        tfFullName.setText("");
                        tfAge.setText("");
                        tfNationality.setText("");
                        tfOffense.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update criminal", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update criminal", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
