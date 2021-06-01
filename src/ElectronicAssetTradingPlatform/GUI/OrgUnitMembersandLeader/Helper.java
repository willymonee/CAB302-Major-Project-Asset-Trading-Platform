package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import javax.swing.*;
import java.awt.*;

public class Helper {
    public static JPanel createPanel(Color c) {
        // Create a JPanel object and store it in a local var
        JPanel panel = new JPanel();
        // set the background colour to that passed in c
        panel.setBackground(c);
        // Return the JPanel object
        return panel;
    }

    public static JLabel createLabel(String text, int fontSize) {
        // Create a JLabel object
        JLabel label = new JLabel(text);
        // set the font
        label.setFont(new Font ( "Dialog", Font.BOLD, fontSize));
        // Return the JPanel object
        return label;
    }


}
