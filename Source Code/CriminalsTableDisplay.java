package src;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CriminalsTableDisplay extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Darling@15";

    public CriminalsTableDisplay() {
        setTitle("Criminals Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to maximize

        setSize(600, 400);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM criminals";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);

            getContentPane().add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve data from the database", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int column = 1; column <= columnCount; column++) {
            columnNames[column - 1] = metaData.getColumnName(column);
        }

        // Get data rows
        Object[][] data = new Object[100][columnCount]; // Assuming a maximum of 100 rows
        int rowCount = 0;
        while (resultSet.next()) {
            for (int column = 1; column <= columnCount; column++) {
                data[rowCount][column - 1] = resultSet.getObject(column);
            }
            rowCount++;
        }

        return new DefaultTableModel(data, columnNames);
    }
}
