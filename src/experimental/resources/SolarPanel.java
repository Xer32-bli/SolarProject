package experimental.resources;

public class SolarPanel {
    private final String brand;
    private final String details;

    public SolarPanel(String brand, String details) {
        this.brand = brand;
        this.details = details;
    }

    public String getBrand() {
        return brand;
    }

    public String getDetails() {
        return details;
    }
}
