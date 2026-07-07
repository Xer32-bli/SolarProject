package experimental.resources;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SolarReportExporter {

    public static void exportReport(
            String totalConsumption,
            String batteryInfo,
            String panelInfo,
            String inverterInfo,
            String sunlightHours,
            String panelsNeeded,
            String totalCost,
            String location,
            String coordinates,
            String tiltAngle,
            String efficiency) {

        try {
            // Detect user's Downloads folder
            String userHome = System.getProperty("user.home");
            File downloadDir = new File(userHome, "Downloads");
            if (!downloadDir.exists()) downloadDir.mkdirs();

            File reportFile = new File(downloadDir, "SolarSystemReport.txt");

            try (FileWriter writer = new FileWriter(reportFile)) {

                writer.write("=============================================\n");
                writer.write("           Solar Dashboard Report          \n");
                writer.write("=============================================\n\n");

                writer.write(" APPLIANCE SUMMARY\n");
                writer.write("---------------------------------------------\n");
                writer.write(String.format("Total Power Consumption: %s\n", totalConsumption));
                writer.write(String.format("Battery Recommendation: %s\n", batteryInfo));
                writer.write(String.format("Solar Panel Recommendation: %s\n", panelInfo));
                writer.write(String.format("Inverter Recommendation: %s\n", inverterInfo));
                writer.write(String.format("Estimated Sunlight Hours: %s\n", sunlightHours));
                writer.write(String.format("Estimated Panels Needed: %s\n", panelsNeeded));
                writer.write(String.format("Total Cost: %s\n\n", totalCost));

                writer.write(" MAP INFORMATION\n");
                writer.write("---------------------------------------------\n");
                writer.write(String.format("Location: %s\n", location));
                writer.write(String.format("Coordinates: %s\n", coordinates));
                writer.write(String.format("Tilt Angle: %s\n", tiltAngle));
                writer.write(String.format("Efficiency: %s\n", efficiency));
                writer.write("\n=============================================\n");
                writer.write(" End of Report\n");
                writer.write("=============================================\n");
            }

            System.out.println(" Report successfully saved to: " + reportFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println(" Error exporting report: " + e.getMessage());
        }
    }
}
