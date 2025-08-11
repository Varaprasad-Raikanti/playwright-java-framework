package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AllureUtils {

	public static void writeEnvToAllure(String appName) {
		Properties props = new Properties();

		try {
			// Load fresh config from file
			File configFile = new File("src/test/resources/config/" + appName + "/config.properties");
			Properties configProps = new Properties();
			try (FileInputStream fis = new FileInputStream(configFile)) {
				configProps.load(fis);
			}

			// Get values from loaded config
			String url = configProps.getProperty("url", "N/A");
			String browser = configProps.getProperty("browser", "N/A");
			String osName = System.getProperty("os.name");

			System.out.println("DEBUG: Loaded config for brand '" + appName + "'");
			System.out.println("DEBUG: URL = " + url);
			System.out.println("DEBUG: Browser = " + browser);
			System.out.println("DEBUG: OS = " + osName);

			// Set to allure environment props
			props.setProperty("URL", url);
			props.setProperty("Browser", browser);
			props.setProperty("OS", osName);

			// Ensure allure-results directory exists
			File allureResultsDir = new File("allure-results");
			if (!allureResultsDir.exists()) {
				boolean created = allureResultsDir.mkdirs();
				System.out.println("DEBUG: allure-results folder created: " + created);
			}

			// Write to environment.properties (overwrite)
			File envFile = new File(allureResultsDir, "environment.properties");
			try (FileOutputStream fos = new FileOutputStream(envFile, false)) {
				props.store(fos, "Allure Environment Properties");
			}

			System.out.println("✅ Allure environment file created at: " + envFile.getAbsolutePath());

		} catch (IOException e) {
			System.err.println("❌ Failed to write Allure environment metadata: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
