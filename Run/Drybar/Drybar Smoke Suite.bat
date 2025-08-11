@echo off
REM ================================
REM Step 1: Clear old Allure results
REM ================================
echo Deleting old Allure results...
rd /s /q "C:\Users\varap\playwright-java-framework\allure-results"

REM ================================
REM Step 2: Run Maven tests
REM ================================
echo Running tests...
cd /d "C:\Users\varap\playwright-java-framework"
mvn clean test -DsuiteXmlFile=src/test/resources/Testng/Drybar/DrybarSmoke.xml

REM ================================
REM Step 3: Open Allure report
REM ================================
echo Opening Allure report...
allure serve "C:\Users\varap\playwright-java-framework\allure-results"

timeout /t 400 >nul


pause