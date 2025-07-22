package nro.utils;

/**
 *
 * @author onepl
 */
import nro.jdbc.DBService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import nro.models.PartManager;
import nro.server.Manager;

public class Panel_Reward extends JFrame {

    private JComboBox<String> accountComboBox;
    private JComboBox<String> itemComboBox;
    private JTextField quantityField;
    private JTextField optionsField;
    private JTextField searchIdField;
    private JTextField searchItemIdField;
    private JButton sendButton;
    private JButton searchAccountButton;
    private JButton searchItemButton;
    private JButton reloadPartButton;

    private Map<String, Integer> accountMap;  // Map to store account names and their IDs
    private Map<Integer, String> itemMap;     // Map to store item IDs and their names   
    
    public Panel_Reward() {
        setTitle("Ngọc Rồng Bá Vương - Quà Tặng");
        setSize(600, 400); // Increased size to accommodate panels better
        setLocationRelativeTo(null);

        accountMap = new HashMap<>();
        itemMap = new HashMap<>();
        loadAccountData();  // Load account data into the JComboBox
        loadItemData();     // Load item data into the JComboBox

        JPanel mainPanel = new JPanel(new BorderLayout()); // Use BorderLayout for mainPanel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel containing input components
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5)); // GridLayout with 8 rows and 2 columns
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Font and colors
        Font font = new Font("Arial", Font.PLAIN, 16);
        Color textColor = Color.BLACK;

        JLabel accountLabel = new JLabel("Tài Khoản:");
        accountLabel.setFont(font);
        accountLabel.setForeground(textColor);
        inputPanel.add(accountLabel);

        accountComboBox = new JComboBox<>(accountMap.keySet().toArray(new String[0]));
        accountComboBox.setFont(font);
        inputPanel.add(accountComboBox);

        JLabel itemLabel = new JLabel("Vật Phẩm:");
        itemLabel.setFont(font);
        itemLabel.setForeground(textColor);
        inputPanel.add(itemLabel);

        itemComboBox = new JComboBox<>(itemMap.values().toArray(new String[0]));
        itemComboBox.setFont(font);
        inputPanel.add(itemComboBox);

        JLabel quantityLabel = new JLabel("Số Lượng:");
        quantityLabel.setFont(font);
        quantityLabel.setForeground(textColor);
        inputPanel.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setFont(font);
        inputPanel.add(quantityField);

        JLabel optionsLabel = new JLabel("Chỉ số (id:param,id:param):");
        optionsLabel.setFont(font);
        optionsLabel.setForeground(textColor);
        inputPanel.add(optionsLabel);

        optionsField = new JTextField();
        optionsField.setFont(font);
        inputPanel.add(optionsField);

        // Panel containing search buttons using GridBagLayout
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);

        searchIdField = new JTextField();
        searchIdField.setFont(font);
        inputPanel.add(searchIdField);

        searchAccountButton = new JButton("Tìm ID Tài Khoản");
        searchAccountButton.setFont(font);
        searchAccountButton.setForeground(Color.WHITE);
        searchAccountButton.setBackground(new Color(52, 152, 219));
        searchAccountButton.setFocusPainted(false);
        inputPanel.add(searchAccountButton);
        
        
        searchItemIdField = new JTextField();
        searchItemIdField.setFont(font);
        inputPanel.add(searchItemIdField);

        searchItemButton = new JButton("Tìm ID Vật Phẩm");
        searchItemButton.setFont(font);
        searchItemButton.setForeground(Color.WHITE);
        searchItemButton.setBackground(new Color(52, 152, 219));
        searchItemButton.setFocusPainted(false);
        inputPanel.add(searchItemButton);

        reloadPartButton = new JButton("Load Part");
        reloadPartButton.setFont(font);
        reloadPartButton.setForeground(Color.WHITE);
        reloadPartButton.setBackground(new Color(52, 152, 219));
        reloadPartButton.setFocusPainted(false);
        inputPanel.add(reloadPartButton);
        
        mainPanel.add(inputPanel, BorderLayout.CENTER); // Add inputPanel to CENTER position of mainPanel
        mainPanel.add(inputPanel, BorderLayout.NORTH); // Add searchPanel to NORTH position of mainPanel

        // Panel containing button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        sendButton = new JButton("Gửi");
        sendButton.setFont(font);
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(new Color(52, 152, 219));
        sendButton.setFocusPainted(false);
        buttonPanel.add(sendButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Add buttonPanel to SOUTH position of mainPanel

        add(mainPanel);

        searchAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchAccountById();
            }
        });

        searchItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchItemById();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendItemReward();
            }
        });
        
        reloadPartButton.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                 PartManager.getInstance().load();
            }
        });

        pack();
    }

    private void searchAccountById() {
        try {
            int searchId = Integer.parseInt(searchIdField.getText());
            boolean found = false;
            for (Map.Entry<String, Integer> entry : accountMap.entrySet()) {
                if (entry.getValue() == searchId) {
                    accountComboBox.setSelectedItem(entry.getKey());
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Account ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid account ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchItemById() {
        try {
            int searchItemId = Integer.parseInt(searchItemIdField.getText());
            itemComboBox.removeAllItems();
            boolean found = false;
            for (Map.Entry<Integer, String> entry : itemMap.entrySet()) {
                if (entry.getKey() == searchItemId) {
                    itemComboBox.addItem(entry.getValue());
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Item ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid item ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAccountData() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
            ps = con.prepareStatement("SELECT id, username FROM account WHERE id >= 0");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                accountMap.put(username, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading account data.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadItemData() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
            ps = con.prepareStatement("SELECT id, name FROM item_template WHERE id >= 0");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                itemMap.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading item data.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendItemReward() {
        String accountName = (String) accountComboBox.getSelectedItem();
        int accountId = accountMap.get(accountName);
        String itemName = (String) itemComboBox.getSelectedItem();
        int itemId = getKeyFromValue(itemMap, itemName); // Lấy ID của item từ tên item
        int quantity = Integer.parseInt(quantityField.getText());
        String options = optionsField.getText();

        // Tạo chuỗi phần thưởng dữ liệu mới
        String newItemReward = "{" + itemId + ":" + quantity;
        if (!options.isEmpty()) {
            newItemReward += "|";
            String[] optionList = options.split(" ");
            for (String option : optionList) {
                newItemReward += "[" + option + "],";
            }
            newItemReward = newItemReward.substring(0, newItemReward.length() - 1) + "}";
        } else {
            newItemReward += "}";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
            // Đọc dữ liệu reward hiện tại
            ps = con.prepareStatement("SELECT reward FROM account WHERE id = ?");
            ps.setInt(1, accountId);
            rs = ps.executeQuery();

            String currentReward = "";
            if (rs.next()) {
                currentReward = rs.getString("reward");
            }

            // Cập nhật dữ liệu reward
            if (currentReward != null && !currentReward.isEmpty()) {
                currentReward += ";" + newItemReward;
            } else {
                currentReward = newItemReward;
            }

            // Ghi lại dữ liệu reward đã cập nhật
            ps = con.prepareStatement("UPDATE account SET reward = ? WHERE id = ?");
            ps.setString(1, currentReward);
            ps.setInt(2, accountId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Item reward sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending item reward.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức hỗ trợ tìm key từ value trong Map
    private <K, V> K getKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void run() {
        new Panel_Reward().setVisible(true);
    }
    
    
}
