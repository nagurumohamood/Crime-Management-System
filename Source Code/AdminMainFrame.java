import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

class AdminMainFrame extends JFrame {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Darling@15";
    private JButton btnEditRecord;

    public AdminMainFrame() {
        setTitle("Admin Main");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to maximize
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 15, 15, 15);

        // Add admin button
        // ImageIcon addAdminIcon = new ImageIcon("admin.png");
        ImageIcon logoIcon = new ImageIcon("images/cr.png");
        Image logoImage = logoIcon.getImage();
        Image resizedLogoImage = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedLogoIcon = new ImageIcon(resizedLogoImage);
        JButton addAdminButton = new JButton("Create Administrator", resizedLogoIcon);
        // addAdminButton.setIcon(logoIcon);
        addAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminInputDialog dialog = new AdminInputDialog(AdminMainFrame.this);
                String[] credentials = dialog.showDialog();
                if (credentials != null) {
                    String username = credentials[0];
                    String password = credentials[1];
                    if (adminExists(username)) {
                        JOptionPane.showMessageDialog(AdminMainFrame.this, "Admin already exists", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (insertAdmin(username, password)) {
                            JOptionPane.showMessageDialog(AdminMainFrame.this, "Admin added successfully");
                        } else {
                            JOptionPane.showMessageDialog(AdminMainFrame.this, "Failed to add admin", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminMainFrame.this, "Invalid username or password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(addAdminButton, constraints);

        // Edit criminal record button
        ImageIcon addEditIcon = new ImageIcon("images/avatar5.png");
        JButton addEditcButton = new JButton("Edit Criminal Record");
        addEditcButton.setIcon(addEditIcon);
        addEditcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditCriminalRecordPage editPage = new EditCriminalRecordPage();
                editPage.setVisible(true);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(addEditcButton, constraints);

        // Add criminal record button
        ImageIcon addcrimIcon = new ImageIcon("images/new.jpg");
        JButton addcrimButton = new JButton("Add Criminal Record");
        addcrimButton.setIcon(addcrimIcon);
        addcrimButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCriminalPage addPage = new AddCriminalPage();
                addPage.setVisible(true);
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(addcrimButton, constraints);

        // Display records button
        ImageIcon adddisIcon = new ImageIcon("images/group.jpg");
        JButton adddisButton = new JButton("Display Crime Records",resizedLogoIcon);
        adddisButton.setIcon(resizedLogoIcon);
        adddisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CriminalsTableDisplay displayPage = new CriminalsTableDisplay();
                displayPage.setVisible(true);
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(adddisButton, constraints);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon addserIcon = new ImageIcon("images/new.jpg");
        JButton addserButton = new JButton("Search Record");
        addserButton.setIcon(addserIcon);
        addserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CriminalSearchPage addPage = new CriminalSearchPage();
                addPage.setVisible(true);
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(addserButton, constraints);

        ImageIcon addscomIcon = new ImageIcon("images/new.jpg");
        JButton displayComplaintsButton = new JButton("Display Complaints", addscomIcon);
        displayComplaintsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComplaintsTableDisplay a = new ComplaintsTableDisplay();
                a.setVisible(true);
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(displayComplaintsButton, constraints);
        add(panel);
    }

    private boolean adminExists(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String sql = "SELECT COUNT(*) FROM admins WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
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
        return false;
    }

    private boolean insertAdmin(String username, String password) {
        if (adminExists(username)) {
            return false;
        }

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(AdminMainFrame.this, "Please fill in all the fields", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
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
        return false;
    }
}
class AdminInputDialog extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField cpasswordField;
    private JButton okButton;
    private JButton cancelButton;
    private String[] credentials;

    public AdminInputDialog(JFrame parent) {
        super(parent, "Add Admin ", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Create Username:");
        usernameField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);
        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Create Password:");
        passwordField = new JPasswordField(20);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        JLabel cpasswordLabel = new JLabel("Confirm Password:");
        cpasswordField = new JPasswordField(20);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(cpasswordLabel, constraints);
        constraints.gridx = 1;
        panel.add(cpasswordField, constraints);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(okButton, constraints);
        constraints.gridx = 1;
        panel.add(cancelButton, constraints);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String cpassword = new String(cpasswordField.getPassword());

                if (!password.equals(cpassword)) {
                    JOptionPane.showMessageDialog(AdminInputDialog.this,
                            "Passwords do not match. Please try again.", "Password Mismatch",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                credentials = new String[] { username, password };
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                credentials = null;
                dispose();
            }
        });

        add(panel);
    }

    public String[] showDialog() {
        setVisible(true);
        return credentials;
    }
}
