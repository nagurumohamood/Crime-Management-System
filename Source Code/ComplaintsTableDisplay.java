package src;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ComplaintsTableDisplay extends JFrame {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crime_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Darling@15";

    public ComplaintsTableDisplay() {
        setTitle("Complaints");
        setSize(600, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to maximize

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel();

        // Create table and set the model
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query
            String query = "SELECT * FROM case_details";
            ResultSet resultSet = statement.executeQuery(query);

            // Get column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                tableModel.addColumn(metaData.getColumnName(column));
            }

            // Populate data into the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int column = 1; column <= columnCount; column++) {
                    rowData[column - 1] = resultSet.getObject(column);
                }
                tableModel.addRow(rowData);
            }

            // Add table to the frame
            add(scrollPane);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
}
