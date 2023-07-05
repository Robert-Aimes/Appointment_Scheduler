Project Title: Java Desktop Appointment Scheduler Application
Purpose: The purpose of this application is to provide a GUI based appointment scheduling application using JavaFX. User's can add/update/delete appointments and customers, as well as view various reports about the customers and appointments. All data is saved and retrieved from a MySQL database.

Author Information:
- Author: Robert Aimes
- Student ID: 001445280
- Contact Info: raimes1@wgu.edu
- Version: 1.1
- Date: 6/30/2023

IDE and Java Information:
- IDE: IntelliJ IDEA 2021.1.3 (Community Edition)
- JDK: Java SE 11.0.11
- JavaFX: JavaFX 11

Application Directions:
- Upon building and running the application, users will be prompted to log in with their username and password.
- Upon successful login, the user will be prompted if they have/don't have any upcoming appointment within 15 minutes of logging in. Users will be brought to a main screen showing 2 different tables. The top table shows all appointments and the bottom shows all the customers.
- Users can select buttons to add new appointments, update or delete a selected appointment from the appointment table. Adding or updating will open a new screen for users to enter the appointment information. Upon saving, the appointment will either be inserted or updated in the MySQL DB Appointment table.
- Users can select buttons to add new customers, update or delete a selected customer from the customer table. Adding or updating will open a new screen for users to enter the customer information. Upon saving, the customer will either be inserted or updated in the MySQL DB customer Table.
- From the main screen, users can also select a "Reports" button that will open a new screen to view three different reports about a specific employees appointment schedule, and two other reports for a summarized view of appointments by month and type, and customers by state or province.

Additional Report:
- The Additional Report that I added in the Reports screen is a summarized view of total customers by state/province (division). Upon entering the Report screen, users the table will query the customer and divisions table and populate a tableview with the total number of customers grouped by division.

MySQL Connector:
- mysql-connector-java-8.0.25
