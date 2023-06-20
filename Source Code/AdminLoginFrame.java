
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

class AdminLoginFrame extends JFrame {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Darling@15";
    private JButton btnAdminSignUp;

    private JTextField adminTextField;
    private JPasswordField passwordTextField;

    public AdminLoginFrame() {
        setTitle("Admin Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        // Add admin label and text field
        JLabel adminLabel = new JLabel("Username :");
        adminTextField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(adminLabel, constraints);
        constraints.gridx = 1;
        panel.add(adminTextField, constraints);

        // Add password label and text field
        JLabel passwordLabel = new JLabel("Password:");
        passwordTextField = new JPasswordField(20);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);
        constraints.gridx = 1;
        panel.add(passwordTextField, constraints);

        // Add login button
        JButton loginButton = new JButton("Admin Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String admin = adminTextField.getText();
                String password = new String(passwordTextField.getPassword());

                if (validateAdminCredentials(admin, password)) {
                    AdminMainFrame adminMainFrame = new AdminMainFrame();
                    adminMainFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AdminLoginFrame.this, "Invalid admin credentials", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, constraints);
        add(panel);
    }

    private boolean validateAdminCredentials(String admin, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, admin);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            return rs.next(); // If there is a match, rs.next() will be true
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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
        return false; // In case of any error, return false
    }
}