# sport-eq-rent-app - Sports Equipment Rental Web Application

**SportRent** is a web application designed for companies that rent sports equipment to clients for a defined period of time.  
It allows users to browse available equipment, create accounts, make reservations, and manage rentals efficiently.  
The system also supports multiple languages and provides a secure registration process with email confirmation.

---

## Features

- User registration and login with password hashing  
- Email confirmation during registration  
- Multilingual interface (4 supported languages)  
- Pagination and data sorting  
- Custom validation annotations  
- Unit tests for core application logic  
- SQLite database integration  
- JPA (Hibernate) ORM support  
- Frontend built with HTML, CSS, JavaScript, and Thymeleaf templates

## Technologies Used

**Backend:**
- Java 17
- Spring Boot (MVC, Security, JPA)
- Hibernate ORM (used as the JPA implementation)
- Custom validation annotations  
- Unit testing with JUnit and Mockito  

**Frontend:**  
- HTML5 and CSS3 for layout and styling  
- JavaScript for client-side interactions  
- Thymeleaf template engine for dynamic views  

**Database:**  
- SQLite
- JPA integration with automatic schema generation  

**Build & Tools:**  
- Maven for dependency management  
- Git and GitHub for version control  
- Integrated mail service for registration confirmation  

---

## How to Run Locally

### Requirements
- Java 17 or higher  
- Maven

### Steps
1. Clone the repository:

**git clone https://github.com/cblazej77/sport-eq-rent-app.git**

2. Navigate to the project directory:

**cd sport-eq-rent-app**

3. Run the application:

**mvn spring-boot:run**

4. Open your browser and go to:

**http://localhost:8081**

---

## Supported Languages

The application supports four interface languages:
- English
- Polish
- Spanish
- French

Language selection can be changed dynamically within the user interface.

## Testing

To run unit tests:

**mvn test**

All critical components (services, validation, and repositories) include unit tests written with JUnit and Mockito.

## Security Overview

- Passwords are securely stored using Spring Security PasswordEncoder
- Registration requires email confirmation
- Form inputs are validated using custom annotation-based validation logic

---

## Author
Błażej Cieślewicz
Email: blazejcie07@gmail.com

## License
This project is licensed under the Apache License 2.0.

---

