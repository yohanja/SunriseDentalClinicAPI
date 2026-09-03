# Sunrise Dental Clinic Management System

A distributed, web-based appointment and patient management system built for
Sunrise Dental Clinic, developed as the CIS6003 end-of-module assessment.

## Overview

The system allows clinic staff to log in securely, register new appointments,
search and view appointment and patient details, calculate and print bills,
and manage staff accounts. An administrator role can additionally register
new staff accounts and new dentists.

## Technology Stack

- **Language:** Java 17 (GraalVM)
- **Build tool:** Maven (WAR packaging)
- **Web server:** Apache Tomcat 11 (via the SmartTomcat IntelliJ plugin)
- **Database:** MySQL 9.7.1
- **Testing:** JUnit 5
- **Email:** Jakarta Mail with Gmail SMTP
- **Frontend:** HTML, CSS, JavaScript, FontAwesome icons

## Architecture

The project follows a layered structure inside the `com.sunrise` package:

- `dao` — Data Access Objects (PatientDAO, AppointmentDAO, BillDAO, DentistDAO,
  UserDAO, DashboardDAO) that handle all database access using JDBC
- `servlet` — handles HTTP requests and responses
- `util` — shared utilities, including a Singleton `DBConnection` class

Authentication uses `HttpSession`, with additional client-side protection
against back-button access to authenticated pages after logout.

## Key Features

- Secure staff login with session-based authentication
- Register new appointments (with automatic find-or-create patient logic)
- Search and view appointment details by appointment number or contact number
- Calculate and print bills (treatment cost + fixed consultation fee)
- Interactive Help section with search
- Admin-only staff and dentist registration
- Staff profile management with password change
- Branded HTML email confirmation on appointment booking

## Running the Project

1. Clone this repository
2. Import as a Maven project in IntelliJ IDEA
3. Set up a local MySQL database and update the connection details in
   `DBConnection`
4. Run the project using the SmartTomcat plugin

## Testing

Unit and integration tests are written using JUnit 5 and can be found under
`src/test/java`. Run them via Maven (`mvn test`) or directly in IntelliJ.

## Author

Yohan Jayaweera — CIS6003 End of Module Assessment