package nro.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nro.server.Manager;

public class Panel_Reload extends JFrame {

    private JButton reloadPartButton;
    private JTextField type;

   public Panel_Reload() {
    // Create the input panel using GridBagLayout
    JPanel inputPanel = new JPanel(new GridBagLayout());
    inputPanel.setBackground(Color.WHITE);
    inputPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Increase the border size

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); // Increase the margin between components

    // Font and colors
    Font font = new Font("Arial", Font.PLAIN, 20); // Increase the font size
    Color textColor = Color.BLACK;

    // Loop to add buttons
    for (int i = 0; i <= 26; i++) {
        JButton bt = new JButton(" " + i + " : " + getKey(i));
        bt.setPreferredSize(new Dimension(100, 70)); // Increase the button size
        bt.setFont(font);
        bt.setForeground(Color.WHITE);
        bt.setBackground(new Color(52, 152, 219));
        bt.setFocusPainted(false);

        // Set GridBagConstraints for each button
        gbc.gridx = i % 8; // 8 columns
        gbc.gridy = i / 8; // Move to the next row every 8 buttons
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        inputPanel.add(bt, gbc); // Add button with constraints

        final int buttonNumber = i; // Create a final copy of the loop variable
        bt.addActionListener(e -> {
           Manager.gI().loadDatabase(buttonNumber);
        });
    }

    // Set a larger preferred size for the input panel
    inputPanel.setPreferredSize(new Dimension(1000, 600));

    // Add inputPanel to the main panel or directly to the JFrame
    setLayout(new BorderLayout()); // Set layout of the parent panel or JFrame
    add(inputPanel, BorderLayout.CENTER); // Add inputPanel to the center

    pack(); // Adjusts the frame size to fit the preferred sizes of its components
    setSize(2000, 800); // Manually set a larger size for the JFrame or JPanel
    setLocationRelativeTo(null); // Center the window on the screen
}


    public String getKey(int var) {
        switch (var) {
            case 0:
                return "PART";
            case 1:
                return "PET";
            case 2:
                return "MAP";
            case 3:
                return "SKILL";
            case 4:
                return "HEAD";
            case 5:
                return "INTRINSIC";
            case 6:
                return "TASK";
            case 7:
                return "SIDE TASK";
            case 8:
                return "ITEM | OPTION";
            case 9:
                return "FLAGBAG";
            case 10:
                return "SHOP";
            case 11:
                return "DATA FILE";
            case 12:
                return "MOB";
            case 13:
                return "NPC";
            case 14:
                return "CLAN";
            case 15:
                return "CARD";
            case 16:
                return "POWER";
            case 17:
                return "CAPTION";
            case 18:
                return "Attri TEMP";
            case 19:
                return "Attri Sever";
            case 20:
                return "COUNTEVENT";
            case 21:
                return "EFFECT";
            case 22:
                return "NOTIFY";
            case 23:
                return "CONSIGN";
            case 24:
                return "ACHIVE";
            case 25:
                return "MINIPET";
            case 26:
                return "THUONGDE";
            default:
                return "";
        }
    }

    public void run() {
        new Panel_Reload().setVisible(true);
    }
}
