package experimental.resources;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SolarRecommendationPage extends JFrame {

    private final double totalWatts;
    private final JComboBox<String> batteryBrandBox;
    private final JComboBox<String> panelBrandBox;
    private final JComboBox<String> inverterBrandBox;
    private final JTextField sunlightField = new JTextField("5");
    private final JTextArea resultArea = new JTextArea();

    private final Map<String, SolarCost> costMap = new HashMap<>();
    private final Map<String, String> batteryDetails = new HashMap<>();
    private final Map<String, String> panelDetails = new HashMap<>();
    private final Map<String, String> inverterDetails = new HashMap<>();

    private JButton nextButton; // Proceed button

    public SolarRecommendationPage(double totalWatts) {
        this.totalWatts = totalWatts;

        setTitle("Step 2 – Solar System Recommendation");
        setSize(950, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        loadData();

        // HEADER
        JLabel title = new JLabel("⚡ Solar System Recommendation", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // LEFT – Inputs
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel lbl1 = new JLabel("🔋 Battery Brand:");
        JLabel lbl2 = new JLabel("☀️ Solar Panel Brand:");
        JLabel lbl3 = new JLabel("🌤 Avg Sunlight Hours/Day:");
        JLabel lbl4 = new JLabel("⚙️ Inverter Brand:");

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        lbl1.setFont(labelFont);
        lbl2.setFont(labelFont);
        lbl3.setFont(labelFont);
        lbl4.setFont(labelFont);

        sunlightField.setPreferredSize(new Dimension(60, 28));
        sunlightField.setHorizontalAlignment(JTextField.CENTER);
        sunlightField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        batteryBrandBox = new JComboBox<>(batteryDetails.keySet().toArray(new String[0]));
        panelBrandBox = new JComboBox<>(panelDetails.keySet().toArray(new String[0]));
        inverterBrandBox = new JComboBox<>(inverterDetails.keySet().toArray(new String[0]));

        JButton calcButton = new JButton("Calculate Recommendation");
        calcButton.setBackground(new Color(70, 130, 180));
        calcButton.setForeground(Color.WHITE);
        calcButton.setFocusPainted(false);
        calcButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        calcButton.addActionListener(e -> calculateRecommendation());

        leftPanel.add(lbl1);
        leftPanel.add(batteryBrandBox);
        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(lbl2);
        leftPanel.add(panelBrandBox);
        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(lbl4);
        leftPanel.add(inverterBrandBox);
        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(lbl3);
        leftPanel.add(sunlightField);
        leftPanel.add(Box.createVerticalStrut(20));

        leftPanel.add(calcButton);

        // RIGHT – Results
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        mainPanel.add(leftPanel);
        mainPanel.add(scrollPane);
        add(mainPanel, BorderLayout.CENTER);

        // FOOTER – Back and Proceed Buttons
        JButton backButton = new JButton("← Back");
        backButton.setBackground(new Color(255, 99, 71));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> goBackToStep1());

        nextButton = new JButton("Proceed to Map →");
        nextButton.setBackground(new Color(46, 139, 87));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nextButton.setFocusPainted(false);
        nextButton.setEnabled(false); // disabled until calculation
        nextButton.addActionListener(e -> goToMapPage());

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(240, 248, 255));
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));
        footer.add(backButton, BorderLayout.WEST);
        footer.add(nextButton, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadData() {
        batteryDetails.put("Motolite SolarMaster", "Deep-cycle battery ideal for off-grid solar setups.");
        batteryDetails.put("Dyness 51.2 V 200Ah LiFePO₄", "High-efficiency 10kWh LiFePO₄ battery for residential setups.");
        batteryDetails.put("Pylontech Phantom-S", "51.2V 200Ah lithium battery, long lifespan and strong inverter compatibility.");
        batteryDetails.put("Huawei LUNA2000", "Premium modular battery system (5–15kWh).");
        batteryDetails.put("LVFU 51.2V 200Ah", "10kWh wall-mount battery ideal for hybrid solar systems.");
        batteryDetails.put("allTopElec", "Reliable solar energy storage with long cycle life and BMS protection.");
        batteryDetails.put("Panasonic Deep Cycle", "Long lifespan, low-maintenance sealed lead-acid design.");
        batteryDetails.put("Yuasa Solar Battery", "Durable and reliable battery known for excellent discharge recovery.");
        batteryDetails.put("AMARON FLO", "Zero-maintenance battery with silver alloy for long lifespan.");

        panelDetails.put("Solaric Titan 400W", "High-efficiency mono panel from Solaric PH.");
        panelDetails.put("PhilSolar Pro 410W", "Top-tier 410W panel available locally.");
        panelDetails.put("JA Solar 550W", "High-output half-cut cell panel ideal for on-grid/off-grid.");
        panelDetails.put("Renogy 300W", "Trusted global brand, durable 24V monocrystalline panel.");
        panelDetails.put("Jinko 200W", "Reliable and compact residential solar panel.");
        panelDetails.put("Helios 380W", "Budget-friendly panel with strong warranty support.");
        panelDetails.put("Canadian Solar TOPBiHiKu6", "Premium high-efficiency bifacial N-type panel.");
        panelDetails.put("Dyness 450 W Monocrystalline Panel", "Mid-range efficiency; compatible with LiFePO₄ systems.");
        panelDetails.put("Hantech 160 W Monocrystalline Solar Panel", "Compact mono panel for portable/off-grid use.");

        inverterDetails.put("Growatt 5kW Hybrid Inverter", "Smart hybrid inverter with WiFi monitoring and MPPT control.");
        inverterDetails.put("Huawei SUN2000", "Premium inverter with AI-powered optimization and backup support.");
        inverterDetails.put("Solis Mini 4G", "Reliable budget inverter for small home installations.");
        inverterDetails.put("SMA Sunny Boy", "German-made inverter, high reliability for grid-tied setups.");
        inverterDetails.put("ABB UNO-DM", "Compact inverter with smart grid-ready features.");

        costMap.put("Motolite SolarMaster", new SolarCost(12000, 0, 0));
        costMap.put("Dyness 51.2 V 200Ah LiFePO₄", new SolarCost(120000, 0, 0));
        costMap.put("Pylontech Phantom-S", new SolarCost(135000, 0, 0));
        costMap.put("Huawei LUNA2000", new SolarCost(180000, 0, 0));
        costMap.put("LVFU 51.2V 200Ah", new SolarCost(110000, 0, 0));
        costMap.put("allTopElec", new SolarCost(95000, 0, 0));
        costMap.put("Panasonic Deep Cycle", new SolarCost(25000, 0, 0));
        costMap.put("Yuasa Solar Battery", new SolarCost(30000, 0, 0));
        costMap.put("AMARON FLO", new SolarCost(20000, 0, 0));

        costMap.put("Solaric Titan 400W", new SolarCost(0, 5500, 0));
        costMap.put("PhilSolar Pro 410W", new SolarCost(0, 5700, 0));
        costMap.put("JA Solar 550W", new SolarCost(0, 7500, 0));
        costMap.put("Renogy 300W", new SolarCost(0, 5200, 0));
        costMap.put("Jinko 200W", new SolarCost(0, 3500, 0));
        costMap.put("Helios 380W", new SolarCost(0, 4900, 0));
        costMap.put("Canadian Solar TOPBiHiKu6", new SolarCost(0, 8000, 0));
        costMap.put("Dyness 450 W Monocrystalline Panel", new SolarCost(0, 6000, 0));
        costMap.put("Hantech 160 W Monocrystalline Solar Panel", new SolarCost(0, 3000, 0));

        costMap.put("Growatt 5kW Hybrid Inverter", new SolarCost(0, 0, 45000));
        costMap.put("Huawei SUN2000", new SolarCost(0, 0, 60000));
        costMap.put("Solis Mini 4G", new SolarCost(0, 0, 30000));
        costMap.put("SMA Sunny Boy", new SolarCost(0, 0, 55000));
        costMap.put("ABB UNO-DM", new SolarCost(0, 0, 40000));
    }

    private double panelWatt(String brand) {
        return switch (brand) {
            case "Solaric Titan 400W" -> 400;
            case "PhilSolar Pro 410W" -> 410;
            case "JA Solar 550W" -> 550;
            case "Renogy 300W" -> 300;
            case "Jinko 200W" -> 200;
            case "Helios 380W" -> 380;
            case "Hantech 160 W Monocrystalline Solar Panel" -> 160;
            case "Canadian Solar TOPBiHiKu6" -> 565;
            case "Dyness 450 W Monocrystalline Panel" -> 450;
            default -> 400;
        };
    }

    private void calculateRecommendation() {
        final String batteryBrand = (String) batteryBrandBox.getSelectedItem();
        final String panelBrand = (String) panelBrandBox.getSelectedItem();
        final String inverterBrand = (String) inverterBrandBox.getSelectedItem();

        SolarCost batteryPrice = costMap.get(batteryBrand);
        SolarCost panelPrice = costMap.get(panelBrand);
        SolarCost inverterPrice = costMap.get(inverterBrand);

        if (batteryPrice == null || panelPrice == null || inverterPrice == null) {
            JOptionPane.showMessageDialog(this, "Invalid brand selection. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double sunlight;
        try {
            sunlight = Double.parseDouble(sunlightField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid sunlight hour value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final double sunlightHours = sunlight;
        final double dailyWh = totalWatts;
        final double requiredPanels = Math.max(1, Math.ceil(dailyWh / (panelWatt(panelBrand) * sunlightHours)));

        double batteryCost = batteryPrice.getBatteryCost();
        double panelCost = panelPrice.getPanelCost();
        double inverterCost = inverterPrice.getInverterCost();

        double totalPanelCost = requiredPanels * panelCost;
        double totalMaterialCost = batteryCost + totalPanelCost + inverterCost;
        double laborCost = totalMaterialCost * 0.15;
        double totalSystemCost = totalMaterialCost + laborCost;

        resultArea.setText(String.format("""
        📊 Solar Recommendation
        ----------------------------------------
        Total Appliance Load: %.0f Wh/day
        Avg Sunlight: %.1f hr/day

        🪫 Battery: %s
        %s
        Cost: ₱%,.2f

        ☀️ Solar Panel: %s
        %s
        Panels Needed: %.0f units
        Panel Cost (each): ₱%,.2f
        Total Panel Cost: ₱%,.2f

        ⚙️ Inverter: %s
        %s
        Cost: ₱%,.2f

        💰 Labor (15%%): ₱%,.2f
        ----------------------------------------
        ✅ Total Estimated System Cost: ₱%,.2f
        ----------------------------------------
        """,
                dailyWh, sunlightHours,
                batteryBrand, batteryDetails.get(batteryBrand), batteryCost,
                panelBrand, panelDetails.get(panelBrand), requiredPanels, panelCost, totalPanelCost,
                inverterBrand, inverterDetails.get(inverterBrand), inverterCost,
                laborCost, totalSystemCost
        ));
        nextButton.setEnabled(true);
    }

    // Navigate to SolarMapPage.java
    private void goToMapPage() {
        SwingUtilities.invokeLater(() -> {
            new SolarMapPage().setVisible(true);
        });
        this.dispose();
    }


    // Go back to Step 1 (example: SolarInputPage)
    private void goBackToStep1() {
        SwingUtilities.invokeLater(() -> {
            new ApplianceSelectionPage().setVisible(true);
        });
        this.dispose();
    }
}
