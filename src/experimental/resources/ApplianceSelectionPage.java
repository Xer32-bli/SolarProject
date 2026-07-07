package experimental.resources;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApplianceSelectionPage extends JFrame {

    private final Map<String, ApplianceInput> applianceInputs = new LinkedHashMap<>();

    public ApplianceSelectionPage() {
        setTitle("Step 1 – Select Appliances");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("🏠 Select Your Household Appliances");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(25, 25, 112));

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        addAppliance(mainPanel, "Television");
        addAppliance(mainPanel, "Refrigerator");
        addAppliance(mainPanel, "Electric Fan");
        addAppliance(mainPanel, "Lights");
        addAppliance(mainPanel, "Laptop / Computer");
        addAppliance(mainPanel, "Air Conditioner");
        addAppliance(mainPanel, "Rice Cooker");
        addAppliance(mainPanel, "Washing Machine");

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton nextButton = new JButton("Next ➜");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextButton.setBackground(new Color(60, 179, 113));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setPreferredSize(new Dimension(150, 40));

        nextButton.addActionListener(e -> {
            double totalWatts = calculateTotalWatts();

            this.dispose();

            // proceed to next page
            SwingUtilities.invokeLater(() -> {
                System.out.println("Proceeding to Step 2... Total Power: " + totalWatts + "W");

                new SolarRecommendationPage(totalWatts).setVisible(true);
            });
        });

        mainPanel.add(nextButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);
    }

    private void addAppliance(JPanel panel, String name) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(new Color(240, 248, 255));

        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField quantityField = new JTextField("", 4);
        JTextField hoursField = new JTextField("", 4);

        row.add(checkBox);
        row.add(new JLabel("Qty:"));
        row.add(quantityField);
        row.add(new JLabel("Hours/day:"));
        row.add(hoursField);

        applianceInputs.put(name, new ApplianceInput(checkBox, quantityField, hoursField));
        panel.add(row);
    }

    private double calculateTotalWatts() {
        double total = 0.0;

        for (Map.Entry<String, ApplianceInput> entry : applianceInputs.entrySet()) {
            String name = entry.getKey();
            ApplianceInput input = entry.getValue();

            if (input.checkBox.isSelected()) {
                int qty = parseIntSafe(input.quantityField.getText());
                int hours = parseIntSafe(input.hoursField.getText());
                double watt = getApplianceWattage(name);

                total += watt * qty * hours;
            }
        }

        return total;
    }

    private double getApplianceWattage(String name) {
        return switch (name) {
            case "Television" -> 100;
            case "Refrigerator" -> 200;
            case "Electric Fan" -> 75;
            case "Lights" -> 10;
            case "Laptop / Computer" -> 150;
            case "Air Conditioner" -> 1000;
            case "Rice Cooker" -> 700;
            case "Washing Machine" -> 500;
            default -> 50;
        };
    }

    private int parseIntSafe(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static class ApplianceInput {
        JCheckBox checkBox;
        JTextField quantityField;
        JTextField hoursField;

        ApplianceInput(JCheckBox c, JTextField q, JTextField h) {
            this.checkBox = c;
            this.quantityField = q;
            this.hoursField = h;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApplianceSelectionPage().setVisible(true));
    }
}
