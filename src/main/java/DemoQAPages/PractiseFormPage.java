package DemoQAPages;

import AbstractComponents.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

public class PractiseFormPage extends BasePage {

    WebDriver driver;
    private String firstNameText;
    private String lastNameText;
    private String emailText;
    private String genderText;
    private String mobileNumber;
    private String dateOfBirthText;
    private String subjectText;
    private List<String> hobbiesTexts;
    private String currentAddressText;
    private String stateText;
    private String cityText;

    private final String practiseFormPageURl = "https://demoqa.com/automation-practice-form";

    @FindBy(id = "firstName")
    WebElement firstNameField;

    @FindBy(id = "lastName")
    WebElement lastNameField;

    @FindBy(id = "userEmail")
    WebElement emailField;

    @FindBy(id = "userNumber")
    WebElement phoneNumberField;

    @FindBy(id = "dateOfBirthInput")
    WebElement dateOfBirthField;

    @FindBy(id = "subjectsInput")
    WebElement subjectsTextField;

    @FindBy(id = "currentAddress")
    WebElement currentAddressField;

    @FindBy(id = "react-select-3-input")
    WebElement stateDropdown;

    @FindBy(id = "react-select-4-input")
    WebElement cityDropdown;

    @FindBy(id = "submit")
    WebElement submitButton;

    @FindBy(id = "example-modal-sizes-title-lg")
    WebElement confirmationModal;

    @FindBy(id = "closeLargeModal")
    WebElement confirmationModalCloseButton;

    public PractiseFormPage(WebDriver driver) {
        super(driver);
        this.driver=driver;
        PageFactory.initElements(driver, this);
    }

    public void goTo() {
        super.goTo(practiseFormPageURl); // Calls the method in AbstractComponents
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(practiseFormPageURl));
    }

    public void enterFirstName(String firstName){
        firstNameText = firstName;
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName){
        lastNameText = lastName;
        lastNameField.sendKeys(lastName);
    }

    public void enterEmail(String email){
        emailText = email;
        emailField.sendKeys(email);
    }

    public void selectGender(String gender){
        genderText = gender;
        String xpath = "//div[@class='custom-control custom-radio custom-control-inline' and contains(., '" + gender + "')]";
        WebElement genderRadioButton = driver.findElement(By.xpath(xpath));
        genderRadioButton.click();
    }

    public void enterMobile(String mobile){
        mobileNumber = mobile;
        phoneNumberField.sendKeys(mobile);
    }

    public void setDateOfBirth(String dateOfBirth) {
        dateOfBirthText = dateOfBirth;

        // Parse input date like "02 Jan 1990"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDate date = LocalDate.parse(dateOfBirth, formatter);

        // Click input to open date picker
        dateOfBirthField.click();

        // Select year
        WebElement yearDropdown = driver.findElement(By.className("react-datepicker__year-select"));
        Select selectYear = new Select(yearDropdown);
        selectYear.selectByValue(String.valueOf(date.getYear()));

        // Select month
        WebElement monthDropdown = driver.findElement(By.className("react-datepicker__month-select"));
        Select selectMonth = new Select(monthDropdown);
        selectMonth.selectByIndex(date.getMonthValue() - 1);  // months are 0-based in picker

        // Select day - beware: some days from other months may appear with different class
        String dayLocator = String.format("//div[contains(@class,'react-datepicker__day') and text()='%d' and not(contains(@class,'react-datepicker__day--outside-month'))]", date.getDayOfMonth());
        WebElement day = driver.findElement(By.xpath(dayLocator));
        day.click();
    }

    public void enterSubject(String subject){
        subjectText = subject;
        WaitForWebElementToAppear(subjectsTextField);
        subjectsTextField.sendKeys(subject);
        subjectsTextField.sendKeys(Keys.ENTER);
    }

    public void selectHobbies(List<String> hobbies){
        hobbiesTexts = hobbies;
        for (String hobby : hobbies){
            driver.findElement(By.xpath("//label[text()='" + hobby + "']")).click();
        }
    }

    public void enterCurrentAddress(String address){
        currentAddressText = address;
        currentAddressField.sendKeys(address);
    }

    public void selectStateAndCity(String state, String city){
        stateText = state;
        cityText = city;
        scrollToElement(stateDropdown);
        WaitForWebElementToAppear(stateDropdown);
        stateDropdown.sendKeys(state);
        stateDropdown.sendKeys(Keys.ENTER);
        WaitForWebElementToAppear(cityDropdown);
        cityDropdown.sendKeys(city);
        cityDropdown.sendKeys(Keys.ENTER);
    }

    public void submitForm(){
        scrollToElement(submitButton);
        moveToElement(submitButton);
        submitButton.click();
    }

    public void verifyConfirmationModalIsDisplayed(){
        if(confirmationModal.isDisplayed()){
            System.out.println("Confirmation Modal Is Displayed Successfully!");
        }
        else{
            System.out.println("Confirmation Modal Failed To Display!");
        }
    }

    public boolean verifySubmissionField(String label, String expectedValue) {
        String xpath = "//td[text()='" + label + "']/following-sibling::td";
        WebElement valueElement = driver.findElement(By.xpath(xpath));
        return valueElement.getText().trim().equals(expectedValue.trim());
    }

    public void verifyConfirmationModalInformation() {
        String fullName = firstNameText + " " + lastNameText;
        String formattedDob = formatDateOfBirth(dateOfBirthText);
        String hobbiesCombined = String.join(", ", hobbiesTexts);
        String stateAndCity = stateText + " " + cityText;
        assert verifySubmissionField("Student Name", fullName);
        assert verifySubmissionField("Student Email", emailText);
        assert verifySubmissionField("Gender", genderText);
        assert verifySubmissionField("Mobile", mobileNumber);
        assert verifySubmissionField("Date of Birth", formattedDob);
        assert verifySubmissionField("Subjects", subjectText);
        assert verifySubmissionField("Hobbies", hobbiesCombined);
        assert verifySubmissionField("Address", currentAddressText);
        assert verifySubmissionField("State and City", stateAndCity);
    }

    private String formatDateOfBirth(String inputDate) {
        // Assuming format is "dd MMM yyyy" → convert to "dd MMMM,yyyy"
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd MMMM,yyyy");
        LocalDate date = LocalDate.parse(inputDate, inputFormat);
        return date.format(outputFormat);
    }

    public void closeConfirmationModal(){
        confirmationModalCloseButton.click();
    }

    public void verifyConfirmationModalIsClosed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("modal")));

        List<WebElement> modalElements = driver.findElements(By.id("example-modal-sizes-title-lg"));
        if (modalElements.isEmpty() || !modalElements.get(0).isDisplayed()) {
            Assert.assertTrue(true, "Modal is not visible, as expected.");
        } else {
            Assert.fail("Modal is still visible when it should be closed.");
        }
    }
}
