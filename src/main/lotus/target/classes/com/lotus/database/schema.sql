-- =====-- ========================================
-- 1. DOCTORS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    specialization VARCHAR(100) NOT NULL,
    qualification VARCHAR(200),
    experience_years INT DEFAULT 0,
    background TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);===================
-- Lotus Healthcare System Database Schema
-- ========================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS lotus_healthcare;
USE lotus_healthcare;

-- ========================================
-- 1. DOCTORS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    specialisation VARCHAR(100) NOT NULL,
    qualification VARCHAR(200),
    experience_years INT DEFAULT 0,
    background TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ========================================
-- 2. PATIENTS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    address TEXT,
    medical_history TEXT,
    current_doctor_id INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (current_doctor_id) REFERENCES doctors(doctor_id) ON DELETE SET NULL
);

-- ========================================
-- 3. DOCTOR AVAILABILITY TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS doctor_availability (
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE
);

-- ========================================
-- 4. BOOKINGS/APPOINTMENTS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status ENUM('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED') DEFAULT 'SCHEDULED',
    reason_for_visit TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE,
    UNIQUE KEY unique_doctor_datetime (doctor_id, appointment_date, appointment_time)
);

-- ========================================
-- 5. VISIT DETAILS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS visit_details (
    visit_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    visit_date DATE NOT NULL,
    diagnosis TEXT,
    treatment_plan TEXT,
    doctor_notes TEXT,
    follow_up_required BOOLEAN DEFAULT FALSE,
    follow_up_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

-- ========================================
-- 6. PRESCRIPTIONS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS prescriptions (
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
    visit_id INT NOT NULL,
    medication_name VARCHAR(200) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration VARCHAR(100) NOT NULL,
    instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (visit_id) REFERENCES visit_details(visit_id) ON DELETE CASCADE
);

-- ========================================
-- 7. MESSAGES/NOTIFICATIONS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    recipient_type ENUM('PATIENT', 'DOCTOR') NOT NULL,
    recipient_id INT NOT NULL,
    message_type ENUM('BOOKING_CONFIRMATION', 'DOCTOR_CHANGE', 'APPOINTMENT_REMINDER', 'CANCELLATION', 'GENERAL') NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    booking_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL
);

-- ========================================
-- 0. USERS TABLE (Authentication)
-- ========================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- In production, store hashed passwords
    email VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    user_type ENUM('PATIENT', 'DOCTOR', 'ADMIN') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ========================================
-- 0.1 ACCESS LOGS TABLE (Authorization tracking)
-- ========================================
CREATE TABLE IF NOT EXISTS access_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    access_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ========================================
-- INDEXES FOR BETTER PERFORMANCE
-- ========================================
CREATE INDEX idx_patients_email ON patients(email);
CREATE INDEX idx_doctors_email ON doctors(email);
CREATE INDEX idx_doctors_specialisation ON doctors(specialisation);
CREATE INDEX idx_bookings_date ON bookings(appointment_date);
CREATE INDEX idx_bookings_patient ON bookings(patient_id);
CREATE INDEX idx_bookings_doctor ON bookings(doctor_id);
CREATE INDEX idx_availability_doctor ON doctor_availability(doctor_id);
CREATE INDEX idx_notifications_recipient ON notifications(recipient_type, recipient_id);

-- ========================================
-- SAMPLE DATA FOR TESTING
-- ========================================

-- Insert sample doctors
INSERT INTO doctors (name, email, phone, specialisation, qualification, experience_years, background) VALUES
('Dr. Sarah Johnson', 'sarah.johnson@lotus.com', '+1-555-0101', 'Cardiology', 'MD, PhD in Cardiology', 15, 'Specialist in heart diseases with 15 years of experience'),
('Dr. Michael Chen', 'michael.chen@lotus.com', '+1-555-0102', 'Pediatrics', 'MD, Board Certified Pediatrician', 12, 'Child healthcare specialist with focus on preventive care'),
('Dr. Emily Rodriguez', 'emily.rodriguez@lotus.com', '+1-555-0103', 'Dermatology', 'MD, Dermatology Residency', 8, 'Skin care specialist with expertise in cosmetic and medical dermatology'),
('Dr. David Wilson', 'david.wilson@lotus.com', '+1-555-0104', 'Orthopedics', 'MD, Orthopedic Surgery Fellowship', 20, 'Bone and joint specialist with extensive surgical experience'),
('Dr. Lisa Thompson', 'lisa.thompson@lotus.com', '+1-555-0105', 'Internal Medicine', 'MD, Internal Medicine Board Certified', 10, 'Primary care physician specializing in adult healthcare');

-- Insert sample doctor availability (Monday to Friday, 9 AM to 5 PM)
INSERT INTO doctor_availability (doctor_id, day_of_week, start_time, end_time) VALUES
-- Dr. Sarah Johnson
(1, 'MONDAY', '09:00:00', '17:00:00'),
(1, 'TUESDAY', '09:00:00', '17:00:00'),
(1, 'WEDNESDAY', '09:00:00', '17:00:00'),
(1, 'THURSDAY', '09:00:00', '17:00:00'),
(1, 'FRIDAY', '09:00:00', '17:00:00'),
-- Dr. Michael Chen
(2, 'MONDAY', '08:00:00', '16:00:00'),
(2, 'TUESDAY', '08:00:00', '16:00:00'),
(2, 'WEDNESDAY', '08:00:00', '16:00:00'),
(2, 'THURSDAY', '08:00:00', '16:00:00'),
(2, 'FRIDAY', '08:00:00', '16:00:00'),
-- Dr. Emily Rodriguez
(3, 'TUESDAY', '10:00:00', '18:00:00'),
(3, 'WEDNESDAY', '10:00:00', '18:00:00'),
(3, 'THURSDAY', '10:00:00', '18:00:00'),
(3, 'FRIDAY', '10:00:00', '18:00:00'),
(3, 'SATURDAY', '09:00:00', '13:00:00'),
-- Dr. David Wilson
(4, 'MONDAY', '07:00:00', '15:00:00'),
(4, 'TUESDAY', '07:00:00', '15:00:00'),
(4, 'WEDNESDAY', '07:00:00', '15:00:00'),
(4, 'THURSDAY', '07:00:00', '15:00:00'),
(4, 'FRIDAY', '07:00:00', '15:00:00'),
-- Dr. Lisa Thompson
(5, 'MONDAY', '09:00:00', '17:00:00'),
(5, 'TUESDAY', '09:00:00', '17:00:00'),
(5, 'WEDNESDAY', '09:00:00', '17:00:00'),
(5, 'THURSDAY', '09:00:00', '17:00:00'),
(5, 'FRIDAY', '09:00:00', '17:00:00');

-- Insert sample patients
INSERT INTO patients (name, email, phone, date_of_birth, gender, address, emergency_contact_name, emergency_contact_phone, current_doctor_id) VALUES
('John Smith', 'john.smith@email.com', '+1-555-1001', '1985-03-15', 'MALE', '123 Main St, Anytown, AT 12345', 'Jane Smith', '+1-555-1002', 1),
('Maria Garcia', 'maria.garcia@email.com', '+1-555-1003', '1990-07-22', 'FEMALE', '456 Oak Ave, Somewhere, SW 67890', 'Carlos Garcia', '+1-555-1004', 2),
('Robert Johnson', 'robert.johnson@email.com', '+1-555-1005', '1978-11-08', 'MALE', '789 Pine Rd, Elsewhere, EW 13579', 'Susan Johnson', '+1-555-1006', 3),
('Jennifer Brown', 'jennifer.brown@email.com', '+1-555-1007', '1982-05-30', 'FEMALE', '321 Elm St, Nowhere, NW 24680', 'Michael Brown', '+1-555-1008', 4),
('William Davis', 'william.davis@email.com', '+1-555-1009', '1975-09-12', 'MALE', '654 Maple Dr, Anywhere, AW 97531', 'Linda Davis', '+1-555-1010', 5);

-- Insert sample bookings
INSERT INTO bookings (patient_id, doctor_id, appointment_date, appointment_time, status, reason_for_visit) VALUES
(1, 1, '2025-07-10', '10:00:00', 'SCHEDULED', 'Regular checkup'),
(2, 2, '2025-07-11', '14:00:00', 'CONFIRMED', 'Child vaccination'),
(3, 3, '2025-07-12', '11:30:00', 'SCHEDULED', 'Skin consultation'),
(4, 4, '2025-07-13', '09:00:00', 'CONFIRMED', 'Knee pain assessment'),
(5, 5, '2025-07-14', '15:30:00', 'SCHEDULED', 'Annual physical');

-- ========================================
-- VIEWS FOR COMMON QUERIES
-- ========================================

-- View for patient bookings with doctor information
CREATE VIEW patient_bookings_view AS
SELECT 
    b.booking_id,
    b.appointment_date,
    b.appointment_time,
    b.status,
    b.reason_for_visit,
    p.name AS patient_name,
    p.email AS patient_email,
    d.name AS doctor_name,
    d.specialisation,
    d.phone AS doctor_phone
FROM bookings b
JOIN patients p ON b.patient_id = p.patient_id
JOIN doctors d ON b.doctor_id = d.doctor_id;

-- View for doctor availability with doctor information
CREATE VIEW doctor_availability_view AS
SELECT 
    da.availability_id,
    da.day_of_week,
    da.start_time,
    da.end_time,
    da.is_available,
    d.name AS doctor_name,
    d.specialisation,
    d.email AS doctor_email,
    d.phone AS doctor_phone
FROM doctor_availability da
JOIN doctors d ON da.doctor_id = d.doctor_id
WHERE da.is_available = TRUE AND d.is_active = TRUE;

-- ========================================
-- STORED PROCEDURES FOR COMMON OPERATIONS
-- ========================================

DELIMITER //

-- Procedure to check doctor availability for a specific date and time
CREATE PROCEDURE CheckDoctorAvailability(
    IN p_doctor_id INT,
    IN p_appointment_date DATE,
    IN p_appointment_time TIME
)
BEGIN
    DECLARE v_day_name VARCHAR(10);
    DECLARE v_available_count INT DEFAULT 0;
    DECLARE v_booking_count INT DEFAULT 0;
    
    -- Get day of week
    SET v_day_name = UPPER(DAYNAME(p_appointment_date));
    
    -- Check if doctor is available on this day and time
    SELECT COUNT(*) INTO v_available_count
    FROM doctor_availability da
    JOIN doctors d ON da.doctor_id = d.doctor_id
    WHERE da.doctor_id = p_doctor_id
    AND da.day_of_week = v_day_name
    AND p_appointment_time BETWEEN da.start_time AND da.end_time
    AND da.is_available = TRUE
    AND d.is_active = TRUE;
    
    -- Check if there's already a booking at this time
    SELECT COUNT(*) INTO v_booking_count
    FROM bookings
    WHERE doctor_id = p_doctor_id
    AND appointment_date = p_appointment_date
    AND appointment_time = p_appointment_time
    AND status IN ('SCHEDULED', 'CONFIRMED');
    
    -- Return result
    SELECT 
        CASE 
            WHEN v_available_count > 0 AND v_booking_count = 0 THEN 'AVAILABLE'
            WHEN v_available_count = 0 THEN 'NOT_AVAILABLE_TIME'
            WHEN v_booking_count > 0 THEN 'ALREADY_BOOKED'
            ELSE 'UNKNOWN'
        END AS availability_status;
END //

DELIMITER ;

-- Insert sample users for testing
INSERT INTO users (username, password, email, name, user_type) VALUES
('admin', 'admin123', 'admin@lotus.com', 'System Administrator', 'ADMIN'),
('drjohnson', 'doctor123', 'sarah.johnson@lotus.com', 'Dr. Sarah Johnson', 'DOCTOR'),
('drchen', 'doctor123', 'michael.chen@lotus.com', 'Dr. Michael Chen', 'DOCTOR'),
('drrodriguez', 'doctor123', 'emily.rodriguez@lotus.com', 'Dr. Emily Rodriguez', 'DOCTOR'),
('drwilson', 'doctor123', 'david.wilson@lotus.com', 'Dr. David Wilson', 'DOCTOR'),
('drthompson', 'doctor123', 'lisa.thompson@lotus.com', 'Dr. Lisa Thompson', 'DOCTOR'),
('patient1', 'patient123', 'john.smith@email.com', 'John Smith', 'PATIENT'),
('patient2', 'patient123', 'maria.garcia@email.com', 'Maria Garcia', 'PATIENT'),
('patient3', 'patient123', 'robert.johnson@email.com', 'Robert Johnson', 'PATIENT'),
('patient4', 'patient123', 'jennifer.brown@email.com', 'Jennifer Brown', 'PATIENT'),
('patient5', 'patient123', 'william.davis@email.com', 'William Davis', 'PATIENT');

-- Update doctors table to link with users
UPDATE doctors SET user_id = (SELECT user_id FROM users WHERE email = doctors.email);

-- Update patients table to link with users 
UPDATE patients SET user_id = (SELECT user_id FROM users WHERE email = patients.email);


