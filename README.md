UI Test Framework (Java + Selenium)
This is a lightweight UI testing framework built using Java and Selenium WebDriver, designed to follow the Page Object Model (POM) for better maintainability and scalability of test code.

🔧 Key Components
BaseTest (Abstract Class):
Handles WebDriver setup, teardown, and common test configurations.

BasePage (Abstract Class):
Provides common utilities for interacting with web elements, used as a foundation for all page classes.

Page Object (Concrete Class):
Encapsulates all page-specific elements and user actions following the POM pattern.

Test Class:
Contains actual test cases that interact with the Page Object to validate UI behavior.
