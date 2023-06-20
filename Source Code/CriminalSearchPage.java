package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CriminalSearchPage extends JFrame implements ActionListener {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Darling@15";

    private JTextField tfSearch;
    private JButton btnSearch;
    private JTextArea taResult;

    public CriminalSearchPage() {
        setTitle("Criminal Search");
        setLayout(new GridBagLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to maximize

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        tfSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(this);
        taResult = new JTextArea(10, 30);
        taResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taResult);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Search by Name:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tfSearch, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(btnSearch, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 0, 0);
        add(scrollPane, gbc);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            String name = tfSearch.getText().trim();

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "SELECT * FROM criminals WHERE full_name LIKE ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, "%" + name + "%");

                    ResultSet resultSet = statement.executeQuery();
                    taResult.setText("");
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String fullName = resultSet.getString("full_name");
                        String gender = resultSet.getString("gender");
                        int age = resultSet.getInt("age");
                        String nationality = resultSet.getString("nationality");
                        String caseNumber = resultSet.getString("case_number");
                        String bailStatus = resultSet.getString("bail_status");
                        String jailTerm = resultSet.getString("jail_term");
                        String offense = resultSet.getString("offense");

                        String result = "ID: " + id +
                                "\nFull Name: " + fullName +
                                "\nGender: " + gender +
                                "\nAge: " + age +
                                "\nNationality: " + nationality +
                                "\nCase Number: " + caseNumber +
                                "\nBail Status: " + bailStatus +
                                "\nJail Term: " + jailTerm +
                                "\nOffense: " + offense +
                                "\n-------------------------\n";

                        taResult.append(result);
                    }
                    resultSet.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to search for criminals", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CriminalSearchPage());
    }
}
