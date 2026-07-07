package experimental.resources;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SolarMapPage extends JFrame {

    private JLabel coordLabel;
    private JLabel tiltLabel;
    private JLabel efficiencyLabel;
    private Image mapImage;

    public SolarMapPage() {
        setTitle("Step 4 – Solar Map & Tilt Simulation");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // 🔹 Fullscreen for large map
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        // Header
        JLabel header = new JLabel("🗺️ Click Anywhere on the Philippines Map to Simulate Tilt", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        header.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Load Map Image
        try {
            mapImage = new ImageIcon(getClass().getResource("/experimental/resources/ph_map.jpg")).getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Map image not found. Please check the path.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Center Panel with Scalable Map
        MapPanel mapPanel = new MapPanel();
        mapPanel.setBackground(Color.WHITE);
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                simulateClick(e.getX(), e.getY(), mapPanel.getWidth(), mapPanel.getHeight());
            }
        });
        add(mapPanel, BorderLayout.CENTER);

        // Bottom Info + Finish
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(new EmptyBorder(15, 50, 10, 50));
        infoPanel.setBackground(new Color(245, 250, 255));

        coordLabel = new JLabel("📍 Coordinates: (click anywhere)");
        tiltLabel = new JLabel("🌄 Optimal Tilt Angle: —°");
        efficiencyLabel = new JLabel("☀️ Efficiency Adjustment: —%");
        JLabel note = new JLabel("💡 Tip: Near the equator = lower tilt angle, northern/southern regions = higher tilt.");

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 17);
        coordLabel.setFont(infoFont);
        tiltLabel.setFont(infoFont);
        efficiencyLabel.setFont(infoFont);
        note.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        infoPanel.add(coordLabel);
        infoPanel.add(tiltLabel);
        infoPanel.add(efficiencyLabel);
        infoPanel.add(note);

        JButton finishButton = new JButton("✅ Finish Simulation");
        finishButton.setBackground(new Color(46, 139, 87));
        finishButton.setForeground(Color.WHITE);
        finishButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        finishButton.setFocusPainted(false);
        finishButton.setPreferredSize(new Dimension(220, 45));
        finishButton.addActionListener(e -> finishSimulation());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        bottomPanel.add(finishButton, BorderLayout.EAST);
        bottomPanel.setBackground(new Color(245, 250, 255));

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Simulation Logic
    private void simulateClick(int x, int y, int width, int height) {
        double latitude = 21 - ((double) y / height) * 10;    // 21°N → 11°N
        double longitude = 116 + ((double) x / width) * 10;   // 116°E → 126°E

        double tiltAngle = Math.abs(latitude - 10); // Base tilt heuristic
        double efficiency = Math.max(80, 100 - Math.abs(tiltAngle - 15) * 1.2); // Min 80%

        coordLabel.setText(String.format("📍 Coordinates: %.2f°N, %.2f°E", latitude, longitude));
        tiltLabel.setText(String.format("🌄 Optimal Tilt Angle: %.1f°", tiltAngle));
        efficiencyLabel.setText(String.format("☀️ Efficiency Adjustment: %.1f%%", efficiency));
    }

    // Custom Scalable Map Panel
    private class MapPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (mapImage != null) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imgWidth = mapImage.getWidth(null);
                int imgHeight = mapImage.getHeight(null);

                double imgAspect = (double) imgWidth / imgHeight;
                double panelAspect = (double) panelWidth / panelHeight;

                int drawWidth, drawHeight;
                if (panelAspect > imgAspect) {
                    drawHeight = panelHeight;
                    drawWidth = (int) (panelHeight * imgAspect);
                } else {
                    drawWidth = panelWidth;
                    drawHeight = (int) (panelWidth / imgAspect);
                }

                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;

                g.drawImage(mapImage, x, y, drawWidth, drawHeight, this);
            }
        }
    }

    // Finish Simulation + Export Report
    private void finishSimulation() {
        String totalConsumption = "850W";
        String batteryInfo = "Pylontech Phantom-S, 51.2V 100Ah, ₱45,000";
        String panelInfo = "Jinko 550W Mono, ₱8,000";
        String inverterInfo = "Growatt 5kW Hybrid Inverter, ₱45,000"; // ✅ added inverter info
        String sunlightHours = "4.5 hrs/day";
        String panelsNeeded = "3 panels";
        String totalCost = "₱138,000";

        String location = "User Selected Area";
        String coordinates = coordLabel.getText().replace("📍 Coordinates: ", "");
        String tiltAngle = tiltLabel.getText().replace("🌄 Optimal Tilt Angle: ", "");
        String efficiency = efficiencyLabel.getText().replace("☀️ Efficiency Adjustment: ", "");

        // Export full report
        SolarReportExporter.exportReport(
                totalConsumption,
                batteryInfo,
                panelInfo,
                inverterInfo,
                sunlightHours,
                panelsNeeded,
                totalCost,
                location,
                coordinates,
                tiltAngle,
                efficiency
        );

        JOptionPane.showMessageDialog(this,
                "Simulation complete! The report has been exported to your Downloads folder.\n" +
                        "File name: SolarSystemReport.txt",
                "✅ Export Complete", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SolarMapPage().setVisible(true));
    }
}
