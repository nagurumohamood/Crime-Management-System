package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class HomePage extends JFrame implements ActionListener {
    private JButton btnAdminSignIn;
    private JButton btnFileCase;
    private JButton btnExit;

    public HomePage() {
        setTitle("Crime Management System");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JLabel with the background image
        ImageIcon backgroundIcon = new ImageIcon("images/home.jpg");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblHeader = new JLabel("Crime Management System");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(lblHeader);

        backgroundLabel.add(headerPanel, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Resizing the logo
        ImageIcon logoIcon = new ImageIcon("images/admin.jpg");
        Image logoImage = logoIcon.getImage();
        Image resizedLogoImage = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedLogoIcon = new ImageIcon(resizedLogoImage);

        btnAdminSignIn = new JButton("Admin Sign In", resizedLogoIcon);
        btnAdminSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Admin Login Frame
                new AdminLoginFrame().setVisible(true);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnAdminSignIn, gbc);

        // Add "File Case" button
        ImageIcon fileCaseIcon = new ImageIcon("images/case.png");
        Image fileCaseImage = fileCaseIcon.getImage();
        Image resizedFileCaseImage = fileCaseImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedFileCaseIcon = new ImageIcon(resizedFileCaseImage);

        btnFileCase = new JButton("File Case", resizedFileCaseIcon);
        btnFileCase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code for file case action
                CaseFileForm a = new CaseFileForm();
                a.setVisible(true);
            }
        });
        gbc.gridx = 1;
        buttonPanel.add(btnFileCase, gbc);

        backgroundLabel.add(buttonPanel, BorderLayout.CENTER);

        // Create exit button panel
        JPanel exitPanel = new JPanel();
        exitPanel.setBackground(Color.DARK_GRAY);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnExit = new JButton(" Exit ");
        btnExit.addActionListener(this);
        btnExit.setBackground(Color.RED);
        btnExit.setForeground(Color.BLACK);
        exitPanel.add(btnExit);

        backgroundLabel.add(exitPanel, BorderLayout.SOUTH);

        setContentPane(backgroundLabel); // Set the background label as the content pane

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExit) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HomePage();
            }
        });
    }
}

