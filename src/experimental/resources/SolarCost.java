package experimental.resources;

public class SolarCost {
    private double batteryCost;
    private double panelCost;
    private double inverterCost;

    public SolarCost(double batteryCost, double panelCost, double inverterCost) {
        this.batteryCost = batteryCost;
        this.panelCost = panelCost;
        this.inverterCost = inverterCost;
    }

    public double getBatteryCost() {
        return batteryCost;
    }

    public double getPanelCost() {
        return panelCost;
    }

    public double getInverterCost() {
        return inverterCost;
    }

    public double getTotalCost() {
        double materials = batteryCost + panelCost + inverterCost;
        double labor = materials * 0.15;
        return materials + labor;
    }

    public String getCostBreakdown() {
        double materials = batteryCost + panelCost + inverterCost;
        double labor = materials * 0.15;
        return String.format("""
                Battery Cost: ₱%,.2f
                Panel Cost: ₱%,.2f
                Inverter Cost: ₱%,.2f
                Installation + Labor (15%%): ₱%,.2f
                ---------------------------------------
                Total Estimated Cost: ₱%,.2f
                """, batteryCost, panelCost, inverterCost, labor, getTotalCost());
    }
}
