package experimental.resources;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;



public class SolarDashboard extends JFrame {

    private JComboBox<String> batteryBrandBox;
    private JComboBox<String> panelBrandBox;
    private final JTextField sunlightField = new JTextField();
    private final JTextArea resultArea = new JTextArea();
    private BufferedImage mapImage;
    private JLabel mapLabel;

    private final Map<String, SolarBattery> batteryMap = new HashMap<>();
    private final Map<String, SolarPanel> panelMap = new HashMap<>();
    private final Map<String, SolarCost> costMap = new HashMap<>(); // ✅ new cost map

    public SolarDashboard() {
        setTitle("Solar Energy Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        loadBrandData();
        loadCostData();

        // HEADER
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 215, 100),
                        getWidth(), getHeight(), new Color(70, 160, 250));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel title = new JLabel("Solar Energy Dashboard");
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));
        title.setForeground(new Color(30, 30, 30));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(245, 250, 255),
                        getWidth(), getHeight(), new Color(220, 235, 255));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createGlassCard("🔋 Battery & Site Inputs", createInputPanel()));
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(createGlassCard("📊 Results", createResultsPanel()));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(createGlassCard("🗺 Map — Philippines", createMapPanel()));
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(createGlassCard("💡 Tips", createTipsPanel()));
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // FOOTER
        JLabel footer = new JLabel("© 2025 Rei pogi corporation - renewable energy for the Rei's better future", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(Color.DARK_GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        add(footer, BorderLayout.SOUTH);

        batteryBrandBox = new JComboBox<>(batteryMap.keySet().toArray(new String[0]));
        panelBrandBox = new JComboBox<>(panelMap.keySet().toArray(new String[0]));
    }

    private void loadBrandData() {

        // Battery
        batteryMap.put("Motolite SolarMaster", new SolarBattery(
                "Motolite SolarMaster", "Deep-cycle battery ideal for off-grid solar setups. Locally made and widely available in PH."
        ));
        batteryMap.put("AllTopElec", new SolarBattery (
                "AllTopElec", "offering reliable solar energy storage with long cycle life, built-in BMS protection, and compatibility with most inverters—ideal for home or commercial solar systems."
        ));
        batteryMap.put("Panasonic Deep Cycle", new SolarBattery(
                "Panasonic Deep Cycle", "Long lifespan, low-maintenance battery with sealed lead-acid design."
        ));
        batteryMap.put("Yuasa Solar Battery", new SolarBattery(
                "Yuasa Solar Battery", "Durable and reliable battery known for excellent discharge recovery."
        ));
        batteryMap.put("Pylontech Phantom-S", new SolarBattery(
                "Pylontech Phantom-S", "Advanced 51.2V 200Ah LiFePO₄ battery delivering high efficiency, long lifespan, and strong inverter compatibility—ideal for reliable residential and commercial solar energy storage."
        ));
        batteryMap.put("AMARON FLO", new SolarBattery(
                "AMARON FLO", "With Silver inside, the zero maintenance AMARON battery lasts long, really long."
        ));
        batteryMap.put("Dyness 51.2 V 200Ah LiFePO₄", new SolarBattery(
                "Dyness 51.2 V 200Ah LiFePO₄", "Single-unit 10 kWh battery; good for residential setups (lights, fans, fridge, electronics). High efficiency, 6,000+ cycle life."
        ));
        batteryMap.put("LVFU 51.2 V 200Ah LiFePO₄", new SolarBattery(
                "LVFU 51.2 V 200Ah LiFePO₄", "10 kWh wall-mount battery; solid option for off-grid or hybrid systems. Often paired with Growatt or Deye inverters."
        ));
        batteryMap.put("Huawei LUNA2000", new SolarBattery(
                "Huawei LUNA2000","Premium modular system (5 kWh / 10 kWh / 15 kWh setups). Excellent for hybrid and grid-tied installations."
        ));

        // Panel
        panelMap.put("Solaric Titan 400W", new SolarPanel(
                "Solaric Titan 400W", "High-efficiency monocrystalline panel distributed locally by Solaric Philippines."
        ));
        panelMap.put("Helios 380W", new SolarPanel(
                "Helios 380W", "Locally available budget-friendly solar panel with good warranty support."
        ));
        panelMap.put("PhilSolar Pro 410W", new SolarPanel(
                "PhilSolar Pro 410W", "Top-tier panel brand available in Luzon and Visayas markets."
        ));
        panelMap.put("Xiaomi Mijia 100W Monocrystalline Panel", new SolarPanel(
                "Xiaomi Mijia 100W Monocrystalline Panel","100 W monocrystalline; premium brand (Xiaomi). Good for small systems or for supplementing bigger arrays."
        ));
        panelMap.put("Jinko 200 W Monocrystalline Solar Panel", new SolarPanel(
                "Jinko 200 W Monocrystalline Solar Panel", "Reliable 200 W panel with IP67 waterproof rating; ideal for residential and small off-grid systems."
        ));
        panelMap.put("Canadian Solar TOPBiHiKu6",  new SolarPanel(
                "Canadian Solar TOPBiHiKu6", "Premium high-efficiency bifacial panel with N-type cells; durable for large or hybrid installations."
        ));
        panelMap.put("Dyness 450 W Monocrystalline Panel", new SolarPanel(
                "Dyness 450 W Monocrystalline Panel", "Mid-range efficiency panel; works well with LiFePO₄ battery systems for home solar setups."
        ));
        panelMap.put("Hantech 160 W Monocrystalline Solar Panel", new SolarPanel(
                "Hantech 160 W Monocrystalline Solar Panel", "Compact and affordable mono panel; good for portable or small off-grid applications."
        ));
        panelMap.put("Renogy 300 W Monocrystalline Solar Panel", new SolarPanel(
                "Renogy 300 W Monocrystalline Solar Panel", "High-quality 24 V PERC panel; trusted international brand for residential and RV systems."
        ));
        panelMap.put("JA Solar 550 W PERC Monocrystalline Panel", new SolarPanel(
                "JA Solar 550 W PERC Monocrystalline Panel", "Efficient half-cut cell design, great for both off-grid and on-grid systems."
        ));

    }

    // Battery / Panel Cost
    private void loadCostData() {
        costMap.put("Motolite SolarMaster", new SolarCost(14500, 0,0));
        costMap.put("AllTopElec", new SolarCost(90000, 0,0));
        costMap.put("Panasonic Deep Cycle", new SolarCost(16500, 0,0));
        costMap.put("Yuasa Solar Battery", new SolarCost(15500, 0,0));
        costMap.put("Pylontech Phantom-S", new SolarCost(50000, 0,0));
        costMap.put("AMARON FLO", new SolarCost(7200, 0,0));
        costMap.put("Dyness 51.2 V 200Ah LiFePO₄", new SolarCost(120000, 0,0));
        costMap.put("LVFU 51.2 V 200Ah LiFePO₄", new SolarCost(115000, 0,0));
        costMap.put("Huawei LUNA2000", new SolarCost(180000, 0,0));

        costMap.put("Solaric Titan 400W", new SolarCost(0, 9500,0));
        costMap.put("Helios 380W", new SolarCost(0, 7200,0));
        costMap.put("PhilSolar Pro 410W", new SolarCost(0, 9800,0));
        costMap.put("Xiaomi Mijia 100NNBSPW Monocrystalline Panel", new SolarCost(0, 13000,0));
        costMap.put("Jinko 200 W Monocrystalline Solar Panel", new SolarCost(0, 14500,0));
        costMap.put("Canadian Solar TOPBiHiKu6",  new SolarCost(0, 12000,0));
        costMap.put("Dyness 450 W Monocrystalline Panel", new SolarCost(0, 8000,0));
        costMap.put("Hantech 160 W Monocrystalline Solar Panel", new SolarCost(0, 1200,0));
        costMap.put("Renogy 300 W Monocrystalline Solar Panel", new SolarCost(0, 10000,0));
        costMap.put("JA Solar 550 W PERC Monocrystalline Panel", new SolarCost(0, 11000,0));
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Battery Brand:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(batteryBrandBox = new JComboBox<>(batteryMap.keySet().toArray(new String[0])), gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Solar Panel Brand:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(panelBrandBox = new JComboBox<>(panelMap.keySet().toArray(new String[0])), gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Sunlight Hours/Day:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(sunlightField, gbc);

        JButton calc = createButton("Calculate");
        JButton clear = createButton("Clear");
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btns.setOpaque(false);
        btns.add(calc);
        btns.add(clear);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btns, gbc);

        calc.addActionListener(e -> calculateFromInputs());
        clear.addActionListener(e -> clearInputs());

        return panel;
    }

    private JPanel createResultsPanel() {
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setPreferredSize(new Dimension(400, 200));
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel createMapPanel() {
        mapImage = loadMapImage("philippines_map.jpg");
        mapLabel = new JLabel();
        mapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (mapImage != null) {
            Image scaled = mapImage.getScaledInstance(459, 612, Image.SCALE_SMOOTH);
            mapLabel.setIcon(new ImageIcon(scaled));
        } else {
            mapLabel.setText("<html><center><b>Map not found</b><br>Place <i>philippines_map.jpg</i> beside .class files</center></html>");
        }

        mapLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleMapClick(e.getX(), e.getY());
            }
        });

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(mapLabel, BorderLayout.CENTER);
        return p;
    }

    private JPanel createTipsPanel() {
        JTextArea ta = new JTextArea("""
                • Click a location on the map to estimate optimal tilt and orientation.
                • Choose brands available locally for easy maintenance.
                • Typical tilt = site latitude, facing South (in PH).
                • Avoid shading from nearby structures.
                """);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setOpaque(false);
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(ta, BorderLayout.CENTER);
        return p;
    }

    private JPanel createGlassCard(String title, JComponent inner) {
        JPanel panel = new JPanel(new BorderLayout(8, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        lbl.setForeground(new Color(20, 70, 140));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(inner, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 140, 250));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(40, 120, 230)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(60, 140, 250)); }
        });
        return btn;
    }

    private BufferedImage loadMapImage(String fileName) {
        try {
            var url = getClass().getResource("/" + fileName);
            if (url != null) return ImageIO.read(url);
        } catch (Exception ignored) {}
        try {
            File f = new File(fileName);
            if (f.exists()) return ImageIO.read(f);
        } catch (IOException ignored) {}
        return null;
    }


    private void calculateFromInputs() {
        String batteryBrand = (String) batteryBrandBox.getSelectedItem();
        String panelBrand = (String) panelBrandBox.getSelectedItem();
        SolarBattery battery = batteryMap.get(batteryBrand);
        SolarPanel panel = panelMap.get(panelBrand);

        SolarCost bCost = costMap.get(batteryBrand);
        SolarCost pCost = costMap.get(panelBrand);

        try {
            double sunlight = Double.parseDouble(sunlightField.getText());
            double totalCost = bCost.getBatteryCost() + pCost.getPanelCost();
            double labor = totalCost * 0.15;
            double grand = totalCost + labor;

            resultArea.setText(String.format("""
                    Selected Battery: %s
                    %s

                    Selected Panel: %s
                    %s

                    Sunlight Hours: %.1f hr/day
                    --------------------------
                    💰 Estimated Installation Cost:
                    Battery: ₱%,.2f
                    Panel: ₱%,.2f
                    Labor (15%%): ₱%,.2f
                    --------------------------
                    Total: ₱%,.2f
                    --------------------------
                    Recommendation: Use 2–3 panels for optimal charging.
                    """,
                    battery.getBrand(), battery.getDetails(),
                    panel.getBrand(), panel.getDetails(),
                    sunlight,
                    bCost.getBatteryCost(), pCost.getPanelCost(), labor, grand
            ));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for sunlight hours.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleMapClick(int x, int y) {
        String region;
        double tilt;

        if (y < 220) {
            region = "Luzon";
            tilt = 15;
        } else if (y < 420) {
            region = "Visayas";
            tilt = 10;
        } else {
            region = "Mindanao";
            tilt = 8;
        }

        JOptionPane.showMessageDialog(this, String.format("""
                You clicked on: %s
                Recommended Tilt: %.1f°
                Orientation: Face South
                """, region, tilt), "Solar Panel Tilt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearInputs() {
        sunlightField.setText("");
        resultArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ignored) {}

            // Start at Step 1: Appliance selection page
            ApplianceSelectionPage step1 = new ApplianceSelectionPage();
            step1.setVisible(true);
        });
    }
}
