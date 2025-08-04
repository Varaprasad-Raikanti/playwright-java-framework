package pages;

import com.microsoft.playwright.Page;

public class OXOHelper {
    private final Page page;

    public OXOHelper(Page page) {
        this.page = page;
    }
    
    public void Login() {
        page.click("#firstName");;
    }
    
    
    
    
    
    
    
    
    

    public void enterFirstName(String firstName) {
        page.fill("#firstName", firstName);
    }

    public void enterLastName(String lastName) {
        page.fill("#lastName", lastName);
    }

    public void enterEmail(String email) {
        page.fill("#userEmail", email);
    }

    public void selectGender() {
        page.click("label[for='gender-radio-1']"); // Select "Male"
    }

    public void enterMobile(String mobile) {
        page.fill("#userNumber", mobile);
    }

    public void clickSubmit() {
        page.click("#submit");
    }
}
