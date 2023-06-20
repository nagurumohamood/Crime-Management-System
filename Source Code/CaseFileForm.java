package src;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;

class CaseFileForm extends JFrame {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Darling@15";

    private JTextField firIdTextField;
    private JTextField policeStationTextField;
    private JTextField subjectTextField;
    private JComboBox<String> caseTypeComboBox;
    private JTextField nameTextField;
    private JTextArea addressTextArea;
    private JSpinner dateSpinner;
    private JTextField phoneNumberTextField;
    private JTextArea detailsTextArea;

    public CaseFileForm() {
        setTitle("Case File Form");
        setSize(400, 500);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to maximize
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        // Add FIR ID label and text field
        JLabel firIdLabel = new JLabel("FIR ID:");
        firIdTextField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        panel.add(firIdLabel, constraints);
        constraints.gridx = 1;
        panel.add(firIdTextField, constraints);

        // Add police station label and text field
        JLabel policeStationLabel = new JLabel("Police Station:");
        policeStationTextField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(policeStationLabel, constraints);
        constraints.gridx = 1;
        panel.add(policeStationTextField, constraints);

        // Add subject label and text field
        JLabel subjectLabel = new JLabel("Subject:");
        subjectTextField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(subjectLabel, constraints);
        constraints.gridx = 1;
        panel.add(subjectTextField, constraints);

        // Add case type label and combo box
        JLabel caseTypeLabel = new JLabel("Case Type:");
        String[] caseTypes = { "Violence", "CyberCrime", "Robbery", "Bribery", "Drug Trafficking", "Money Laundering",
        "Assault", "Kidnapping", "Others" }; 
        caseTypeComboBox = new JComboBox<>(caseTypes);
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(caseTypeLabel, constraints);
        constraints.gridx = 1;
        panel.add(caseTypeComboBox, constraints);

        // Add name label and text field
        JLabel nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(nameLabel, constraints);
        constraints.gridx = 1;
        panel.add(nameTextField, constraints);

        // Add address label and text area
        JLabel addressLabel = new JLabel("Address:");
        addressTextArea = new JTextArea(4, 20);
        addressTextArea.setLineWrap(true);
        JScrollPane addressScrollPane = new JScrollPane(addressTextArea);
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(addressLabel, constraints);
        constraints.gridx = 1;
        panel.add(addressScrollPane, constraints);

        // Add date label and date picker
        JLabel dateLabel = new JLabel("Date:");
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(dateLabel, constraints);
        constraints.gridx = 1;
        panel.add(dateSpinner, constraints);

        // Add phone number label and text field
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberTextField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 7;
        panel.add(phoneNumberLabel, constraints);
        constraints.gridx = 1;
        panel.add(phoneNumberTextField, constraints);

        // Add details label and text area
        JLabel detailsLabel = new JLabel("Details:");
        detailsTextArea = new JTextArea(4, 20);
        detailsTextArea.setLineWrap(true);
        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);
        constraints.gridx = 0;
        constraints.gridy = 8;
        panel.add(detailsLabel, constraints);
        constraints.gridx = 1;
        panel.add(detailsScrollPane, constraints);

        // Add submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any field is blank
                if (nameTextField.getText().isEmpty() ||
                        ((String) caseTypeComboBox.getSelectedItem()).isEmpty() ||
                        firIdTextField.getText().isEmpty()||
                        policeStationTextField.getText().isEmpty()||
                        subjectTextField.getText().isEmpty()||
                        nameTextField.getText().isEmpty()||
                        addressTextArea.getText().isEmpty() ||
                        detailsTextArea.getText().isEmpty() ||
                        phoneNumberTextField.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(CaseFileForm.this, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop further processing
                }

                // Validate mobile number
                String mobileNumber = phoneNumberTextField.getText();
                if (mobileNumber.length() != 10) {
                    JOptionPane.showMessageDialog(CaseFileForm.this,
                            "Invalid mobile number. Please enter a 10-digit number.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop further processing
                }
                java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
                java.sql.Date date = new java.sql.Date(selectedDate.getTime());
                // Store the case details in the database
                storecasedetails(firIdTextField.getText(),policeStationTextField.getText(),subjectTextField.getText(),(String)caseTypeComboBox.getSelectedItem(),nameTextField.getText(),
                        addressTextArea.getText(),date,phoneNumberTextField.getText(),
                        detailsTextArea.getText());

                JOptionPane.showMessageDialog(CaseFileForm.this, "Case Filed Successfully");
                dispose(); // Close the case file form
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, constraints);
        add(panel);
        setVisible(true);
    }

    public void storecasedetails(String string, String string2, String string3, Object object, String string4,
            String string5, Date date2, String string6, String string7) {
        // Get the input values from the text fields
        String firId = string;
        String policeStation = string2;
        String subject = string3;
        String caseType =  (String) object;
        String name = string4;
        String address = string5;
        // java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
        java.sql.Date date =  date2;
        String phoneNumber = string6;
        String details = string7;

        // Database connection variables
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Register JDBC driver and establish a connection to the database
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Prepare the SQL statement to insert the case details
            String sql = "INSERT INTO case_details (fir_id, police_station, subject, case_type, name, address, date, phone_number, details) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

            // Set the parameter values in the SQL statement
            stmt.setString(1, firId);
            stmt.setString(2, policeStation);
            stmt.setString(3, subject);
            stmt.setString(4, caseType);
            stmt.setString(5, name);
            stmt.setString(6, address);
            stmt.setDate(7, date);
            stmt.setString(8, phoneNumber);
            stmt.setString(9, details);

            // Execute the SQL statement
            stmt.executeUpdate();

            // Display a success message or perform any other necessary actions

        } catch (SQLException | ClassNotFoundException e) {
            // Handle any errors that occur during the database operation
            e.printStackTrace();

            // Display an error message or perform any other necessary error handling
        } finally {
            // Close the statement and database connection resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
