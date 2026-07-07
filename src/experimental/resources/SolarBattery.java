package experimental.resources;

public class SolarBattery {
    private final String brand;
    private final String details;

    public SolarBattery(String brand, String details) {
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
