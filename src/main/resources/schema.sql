CREATE DATABASE sunrise_dental_clinic;
USE sunrise_dental_clinic;

CREATE TABLE Dentist (
                         dentist_id INT PRIMARY KEY AUTO_INCREMENT,
                         dentist_name VARCHAR(100) NOT NULL,
                         specialization VARCHAR(100)
);

CREATE TABLE Patient (
                         patient_id INT PRIMARY KEY AUTO_INCREMENT,
                         patient_name VARCHAR(100) NOT NULL,
                         address VARCHAR(255),
                         contact_number VARCHAR(20) NOT NULL
);

CREATE TABLE Appointment (
                             appointment_id INT PRIMARY KEY AUTO_INCREMENT,
                             patient_id INT NOT NULL,
                             dentist_id INT NOT NULL,
                             treatment_type VARCHAR(50) NOT NULL,
                             appointment_date DATE NOT NULL,
                             appointment_time TIME NOT NULL,
                             FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
                             FOREIGN KEY (dentist_id) REFERENCES Dentist(dentist_id)
);

CREATE TABLE Bill (
                      bill_id INT PRIMARY KEY AUTO_INCREMENT,
                      appointment_id INT NOT NULL,
                      amount DECIMAL(10,2) NOT NULL,
                      payment_status VARCHAR(20) DEFAULT 'Unpaid',
                      FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id)
);

CREATE TABLE User (
                      user_id INT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(20) NOT NULL
);