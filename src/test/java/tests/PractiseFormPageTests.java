package tests;

import AbstractComponents.BaseTest;
import DemoQAPages.PractiseFormPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.Arrays;

public class PractiseFormPageTests extends BaseTest {

    @BeforeMethod
    public void setup() throws IOException {
        practiseFormPage = getPage(PractiseFormPage.class); // Initialize the field-level practiseFormPage
        practiseFormPage.goTo();
        assert practiseFormPage != null : "practiseFormPage was not initialized properly.";
    }

    @Test
    @DisplayName("should verify if the homepage title is correct")
    public void VerifyPractiseFormPageRegistrationForm(){
        practiseFormPage.enterFirstName("Ivan");
        practiseFormPage.enterLastName("Petrov");
        practiseFormPage.enterEmail("ivan.petrov@example.com");
        practiseFormPage.selectGender("Male");
        practiseFormPage.enterMobile("0888123456");
        practiseFormPage.setDateOfBirth("01 Jan 1990");
        practiseFormPage.enterSubject("Maths");
        practiseFormPage.selectHobbies(Arrays.asList("Sports", "Reading"));
        practiseFormPage.enterCurrentAddress("123 Test Street");
        practiseFormPage.selectStateAndCity("Haryana", "Karnal");
        practiseFormPage.submitForm();
        practiseFormPage.verifyConfirmationModalIsDisplayed();
        practiseFormPage.verifyConfirmationModalInformation();
        practiseFormPage.closeConfirmationModal();
        practiseFormPage.verifyConfirmationModalIsClosed();
    }
}