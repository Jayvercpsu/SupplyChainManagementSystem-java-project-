import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class SupplyChainManagementSystem extends JFrame {

    // Supplier Management
    private HashMap<Integer, String> supplierDetails = new HashMap<>();

    // Order Processing
    private Queue<String> orderQueue = new LinkedList<>();

    // Inventory Tracking
    private ArrayList<String> inventory = new ArrayList<>();

    // Logistics Coordination
    private LinkedList<String> logistics = new LinkedList<>();

    // GUI Components
    private JTextArea displayArea;
    private JTextField supplierNameField, supplierDetailField, orderField, inventoryField, logisticsField;

    public SupplyChainManagementSystem() {
        setTitle("Supply Chain Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Set background color
        getContentPane().setBackground(new Color(240, 240, 240));

        // Display Area - Larger
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1), "Output Log", TitledBorder.LEFT, TitledBorder.TOP));
        add(scrollPane, BorderLayout.SOUTH);

        // Panel for Input Fields and Buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Action buttons at the top
        JPanel actionPanel = new JPanel();
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton showSuppliersButton = new JButton("Show Suppliers");
        JButton showOrdersButton = new JButton("Show Orders");
        JButton showInventoryButton = new JButton("Show Inventory");
        JButton showLogisticsButton = new JButton("Show Logistics");

        showSuppliersButton.setBackground(new Color(30, 144, 255));
        showOrdersButton.setBackground(new Color(30, 144, 255));
        showInventoryButton.setBackground(new Color(30, 144, 255));
        showLogisticsButton.setBackground(new Color(30, 144, 255));

        showSuppliersButton.setForeground(Color.WHITE);
        showOrdersButton.setForeground(Color.WHITE);
        showInventoryButton.setForeground(Color.WHITE);
        showLogisticsButton.setForeground(Color.WHITE);

        showSuppliersButton.addActionListener(e -> displaySuppliers());
        showOrdersButton.addActionListener(e -> displayOrders());
        showInventoryButton.addActionListener(e -> displayInventory());
        showLogisticsButton.addActionListener(e -> displayLogistics());

        actionPanel.add(showSuppliersButton);
        actionPanel.add(showOrdersButton);
        actionPanel.add(showInventoryButton);
        actionPanel.add(showLogisticsButton);

        mainPanel.add(actionPanel, BorderLayout.NORTH);

        // Input Section - Smaller input fields and buttons, centered
        JPanel inputPanel = new JPanel(new GridLayout(6, 3, 10, 10));
        inputPanel.setPreferredSize(new Dimension(400, 250));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Section"));

        // Supplier input
        inputPanel.add(new JLabel("Supplier Name:"));
        supplierNameField = new JTextField();
        supplierNameField.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(supplierNameField);
        JButton addSupplierButton = new JButton("Add Supplier");
        addSupplierButton.setPreferredSize(new Dimension(150, 25));
        addSupplierButton.addActionListener(e -> addSupplier());
        inputPanel.add(addSupplierButton);

        inputPanel.add(new JLabel("Supplier Details:"));
        supplierDetailField = new JTextField();
        supplierDetailField.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(supplierDetailField);
        inputPanel.add(new JLabel()); // empty placeholder

        // Order input
        inputPanel.add(new JLabel("Order Details:"));
        orderField = new JTextField();
        orderField.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(orderField);
        JButton addOrderButton = new JButton("Add Order");
        addOrderButton.setPreferredSize(new Dimension(150, 25));
        addOrderButton.addActionListener(e -> placeOrder());
        inputPanel.add(addOrderButton);

        // Inventory input
        inputPanel.add(new JLabel("Inventory Item:"));
        inventoryField = new JTextField();
        inventoryField.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(inventoryField);
        JButton addInventoryButton = new JButton("Add Inventory");
        addInventoryButton.setPreferredSize(new Dimension(150, 25));
        addInventoryButton.addActionListener(e -> addInventoryItem());
        inputPanel.add(addInventoryButton);

        // Logistics input
        inputPanel.add(new JLabel("Logistics Schedule:"));
        logisticsField = new JTextField();
        logisticsField.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(logisticsField);
        JButton addLogisticsButton = new JButton("Add Logistics Schedule");
        addLogisticsButton.setPreferredSize(new Dimension(150, 25));
        addLogisticsButton.addActionListener(e -> addLogisticsDetail());
        inputPanel.add(addLogisticsButton);

        // Center the input panel
        JPanel centeredPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centeredPanel.add(inputPanel);

        mainPanel.add(centeredPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // Database Connection Helper
    public static class DBHelper {

        private static final String DB_URL = "jdbc:mysql://localhost:3306/supply_chain_db";
        private static final String USER = "root"; // default username for XAMPP
        private static final String PASS = ""; // default password for XAMPP

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        }
    }

    // Supplier Management
    private void addSupplier() {
        String supplierName = supplierNameField.getText();
        String supplierDetails = supplierDetailField.getText();
        if (supplierName.isEmpty()) {
            displayArea.append("Supplier Name cannot be empty.\n");
            return;
        }

        // Insert supplier into database
        try (Connection connection = DBHelper.getConnection()) {
            String query = "INSERT INTO suppliers (name, details) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, supplierName);
                statement.setString(2, supplierDetails);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    displayArea.append("Added supplier: " + supplierName + "\n");
                }
            }
        } catch (SQLException e) {
            displayArea.append("Error adding supplier: " + e.getMessage() + "\n");
        }

        supplierNameField.setText("");
        supplierDetailField.setText("");
    }

    private void displaySuppliers() {
        // Retrieve suppliers from database
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM suppliers";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    displayArea.append("No suppliers to display.\n");
                } else {
                    displayArea.append("Suppliers:\n");
                    do {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String details = resultSet.getString("details");
                        displayArea.append("ID: " + id + ", Name: " + name + ", Details: " + details + "\n");
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            displayArea.append("Error displaying suppliers: " + e.getMessage() + "\n");
        }
    }

    // Order Processing
    private void placeOrder() {
        String order = orderField.getText();
        if (!order.isEmpty()) {
            // Insert order into database
            try (Connection connection = DBHelper.getConnection()) {
                String query = "INSERT INTO orders (order_details) VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, order);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        displayArea.append("Order placed: " + order + "\n");
                    }
                }
            } catch (SQLException e) {
                displayArea.append("Error placing order: " + e.getMessage() + "\n");
            }

            orderField.setText("");
        } else {
            displayArea.append("Please enter order details.\n");
        }
    }

    // Display Orders
    private void displayOrders() {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM orders";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    displayArea.append("No orders to display.\n");
                } else {
                    displayArea.append("Orders:\n");
                    do {
                        int id = resultSet.getInt("id");
                        String orderDetails = resultSet.getString("order_details");
                        displayArea.append("ID: " + id + ", Order: " + orderDetails + "\n");
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            displayArea.append("Error displaying orders: " + e.getMessage() + "\n");
        }
    }

    // Inventory Tracking
    private void addInventoryItem() {
        String item = inventoryField.getText();
        if (!item.isEmpty()) {
            // Add inventory item to database
            try (Connection connection = DBHelper.getConnection()) {
                String query = "INSERT INTO inventory (item_name) VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, item);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        displayArea.append("Added inventory item: " + item + "\n");
                    }
                }
            } catch (SQLException e) {
                displayArea.append("Error adding inventory item: " + e.getMessage() + "\n");
            }

            inventoryField.setText("");
        } else {
            displayArea.append("Please enter an inventory item.\n");
        }
    }

    // Display Inventory
    private void displayInventory() {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM inventory";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    displayArea.append("No inventory items to display.\n");
                } else {
                    displayArea.append("Inventory:\n");
                    do {
                        int id = resultSet.getInt("id");
                        String itemName = resultSet.getString("item_name");
                        displayArea.append("ID: " + id + ", Item: " + itemName + "\n");
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            displayArea.append("Error displaying inventory: " + e.getMessage() + "\n");
        }
    }

    // Logistics Coordination
    private void addLogisticsDetail() {
        String detail = logisticsField.getText();
        if (!detail.isEmpty()) {
            // Add logistics detail to database
            try (Connection connection = DBHelper.getConnection()) {
                String query = "INSERT INTO logistics (schedule_details) VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, detail);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        displayArea.append("Added logistics detail: " + detail + "\n");
                    }
                }
            } catch (SQLException e) {
                displayArea.append("Error adding logistics detail: " + e.getMessage() + "\n");
            }

            logisticsField.setText("");
        } else {
            displayArea.append("Please enter logistics details.\n");
        }
    }

    // Display Logistics
    private void displayLogistics() {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM logistics";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    displayArea.append("No logistics to display.\n");
                } else {
                    displayArea.append("Logistics Schedule:\n");
                    do {
                        int id = resultSet.getInt("id");
                        String scheduleDetails = resultSet.getString("schedule_details");
                        displayArea.append("ID: " + id + ", Schedule: " + scheduleDetails + "\n");
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            displayArea.append("Error displaying logistics: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SupplyChainManagementSystem().setVisible(true);
        });
    }
}
