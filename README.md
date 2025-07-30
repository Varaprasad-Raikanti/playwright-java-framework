# 🎭 Playwright Java Automation Framework

This project is a **Test Automation Framework** built with **Playwright + Java**, following best practices such as the **Page Object Model (POM)**, externalized test data, and **Allure Reporting**.

---

##  Project Structure

playwright-java-framework/
│
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── base/ # Base classes (e.g., TestBase)
│ │ │ ├── pages/ # Page Object classes
│ │ │ └── utils/ # Utilities (Excel, ConfigReader, etc.)
│ └── test/
│ └── java/tests/ # Test classes
│
├── test-data/ # Excel test data files
├── src/test/resources/ # Config files (e.g., config.properties)
├── pom.xml # Maven dependencies
├── testng.xml # TestNG suite configuration
└── allure-results/ # Allure result files (generated after tests)

yaml
Copy
Edit

---

## Features

- 🔹 **Playwright with Java** bindings
- 📄 **Page Object Model (POM)** for clean test structure
- 📊 **Allure Reports** for detailed test execution summaries
- 📚 **Excel-based test data** support
- ⚙️ **ConfigReader** to manage environment settings
- ✅ TestNG framework for test management and parallel execution

---

##  Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/Varaprasad-Raikanti/playwright-java-framework.git
cd playwright-java-framework
2. Prerequisites
Java 11 or later

Maven

Node.js and npm (for Allure commandline)

Playwright dependencies (installed via Playwright CLI)

▶️ Run Tests
bash
Copy
Edit
mvn clean test
📊 View Allure Report
1. Install Allure Commandline (if not already installed)
bash
Copy
Edit
npm install -g allure-commandline --save-dev
2. Serve the Report
bash
Copy
Edit
allure serve allure-results
The report will open automatically in your browser.

Sample Test

@Test
public void testRegistration() {
    RegistrationPage regPage = new RegistrationPage(page);
    regPage.navigateTo();
    regPage.fillRegistrationForm("John", "john@example.com", "123 Main St", "1234567890");
}
📌 To-Do / Enhancements
 Add GitHub Actions CI pipeline

 Add test data validations

 Support multiple browsers and environments

🙌 Contributing
Feel free to fork the project, raise issues, or create pull requests.

🧑 Author
Varaprasad Raikanti

