package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class AllureUtils {

	public static void writeEnvToAllure() {
		try {
			Properties props = new Properties();

			// Pull from ConfigReader
			props.setProperty("Environment", ConfigReader.getProperty("Environment"));
			props.setProperty("Base URL", ConfigReader.getProperty("url"));
			props.setProperty("Browser", ConfigReader.getProperty("browser"));
			props.setProperty("Headless", ConfigReader.getProperty("headless"));
			props.setProperty("Execution Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

			File file = new File("allure-results/environment.properties");
			file.getParentFile().mkdirs();

			try (FileOutputStream fos = new FileOutputStream(file)) {
				props.store(fos, "Allure Environment Info");
			}

			System.out.println("✅ Allure environment metadata written to allure-results/environment.properties");
		} catch (Exception e) {
			System.err.println("❌ Failed to write Allure environment metadata: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Optional: Add metadata to each test (if required)
	public static void attachTestMetadataToReport() {
		// For future enhancements
	}

}
