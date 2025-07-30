package tests;

import base.TestBase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DemoTest extends TestBase {

    @BeforeMethod
    public void before() {
        setup();
    }

    @Test
    public void openDemoSite() {
        String title = page.title();
        System.out.println("Page Title: " + title);
        assert title.toLowerCase().contains("todo");
    }

    @AfterMethod
    public void after() {
        tearDown();
    }
}
