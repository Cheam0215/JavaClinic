/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinicmanagementsystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.JOptionPane;

public class Doctor extends User {
    private static final String APPOINTMENTS_FILE_PATH = "C:\\Users\\User\\OneDrive - Asia Pacific University\\JAVA\\appointments.txt";
    private static final String MEDICAL_FILE_PATH = "C:\\Users\\User\\OneDrive - Asia Pacific University\\JAVA\\medicalRecords.txt";
    private static final String SCHEDULE_FILE_PATH = "C:\\Users\\User\\OneDrive - Asia Pacific University\\JAVA\\schedule.txt";
    private final String username;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

    public Doctor(String username) {
        this.username = username;
    }

    public String getuserName() {
        return username;
    }

    public List<String[]> getDoctorDailyAppointments() {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if (lines[1].equals(username) && lines[3].equals(new dateFormatter().formatCurrentDate()) &&
                    (lines[lines.length-1].equals("Booked") || lines[lines.length-1].equals("Checked In"))) {
                    records.add(lines);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<String[]> getDoctorAppointments() throws IOException {
        List<String[]> records = new ArrayList<>();
        List<String> targetDates = getFutureDates(7); // Get the next 7 dates

        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if (lines[1].equals(username) && targetDates.contains(lines[3]) && lines[6].equals("Booked")) {
                    records.add(lines);
                }
            }
        }
        return records;
    }

    private List<String> getFutureDates(int days) {
        List<String> dates = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            dates.add(dateFormatter.formatFutureDate(i));
        }
        return dates;
    }

    public List<String[]> searchDate(String searchDate, String name) throws IOException {
        List<String[]> results = new ArrayList<>();
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                boolean dateMatches = searchDate != null && !searchDate.isEmpty() && fields[3].equals(searchDate);
                boolean nameMatches = name != null && !name.isEmpty() && fields[1].equalsIgnoreCase(name);

                if (dateMatches && nameMatches) {
                    results.add(fields);
                } else if (dateMatches && (name == null || name.isEmpty())) {
                    results.add(fields);
                } else if (nameMatches && (searchDate == null || searchDate.isEmpty())) {
                    results.add(fields);
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return results;
    }



    public class dateFormatter {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

        public static String formatCurrentDate() {
            LocalDate currentDate = LocalDate.now();
            return currentDate.format(formatter);
        }

        public static String formatFutureDate(int daysToAdd) {
            LocalDate futureDate = LocalDate.now().plusDays(daysToAdd);
            return futureDate.format(formatter);
        }
    }

    public boolean cancelAppointments(String ICNumber, String appDate, String time) {
        List<String> lines = new ArrayList<>();
        boolean isCancelled = false; // To track if the appointment was cancelled

        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[2].equals(ICNumber) && fields[3].equals(appDate) && fields[4].equals(time)) { 
                    fields[fields.length - 1] = "Cancelled"; 
                    line = String.join(",", fields);
                    isCancelled = true; // Mark as cancelled
                }
                lines.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Return false if there's an error
        }

        try (FileWriter fw = new FileWriter(APPOINTMENTS_FILE_PATH);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (String updatedLine : lines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Return false if there's an error
        }

        return isCancelled; // Return the cancellation status
    }
    


    public void addMedicalRecord(String patientName, String patientICNumber, String gender, String date, String diagnosis, String prescription, String comment) {
        String paymentStatus = "Unpaid";
        String amountPaid = "0"; 

        String newMedicalRecord = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                patientName, patientICNumber, gender, date, diagnosis, prescription, comment, paymentStatus, amountPaid);

        try (FileWriter fw = new FileWriter(MEDICAL_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(newMedicalRecord);
            bw.newLine(); 
            JOptionPane.showMessageDialog(null, "Record added successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving record: " + e.getMessage());
        }
    }

    public void updateAppointmentStatus(String ICNumber, String appDate, String newStatus) {
        List<String> updatedLines = new ArrayList<>();
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 7 && fields[2].trim().equals(ICNumber.trim()) && fields[3].trim().equals(appDate.trim())) {
                    fields[6] = newStatus; 
                    line = String.join(",", fields);
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit method if there's an error
        }

        try (FileWriter fw = new FileWriter(APPOINTMENTS_FILE_PATH);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
            JOptionPane.showMessageDialog(null, "Appointment status updated successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public List<String[]> searchMedical(String name) throws IOException {
        List<String[]> results = new ArrayList<>();
        try (FileReader fr = new FileReader(MEDICAL_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                boolean nameMatches = name != null && !name.isEmpty() && fields[0].trim().equalsIgnoreCase(name.trim());

                if (nameMatches) {
                    results.add(fields);
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return results;
    }
    
    public List<String[]> getDoctorSchedule(String doctorName, String date) {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader(SCHEDULE_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[parts.length - 1].equals(date)) {
                    String[] slots = parts[1].split(";");
                    for (String slot : slots) {
                        String[] timeAndStatus = slot.split(":");
                        String time = timeAndStatus[0];
                        String statusCode = timeAndStatus[1];
                        String status;

                        // Handle different status codes
                        switch (statusCode) {
                            case "F":
                                status = "Available";
                                break;
                            case "U":
                                status = "Unavailable";
                                break;
                            case "B":
                                status = "Booked";
                                break;
                            default:
                                status = "Unknown";
                                break;
                        }
                        String[] slotDetails = {time, status};
                        records.add(slotDetails);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public void writeNewSchedule(String doctorName, String date) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCHEDULE_FILE_PATH, true))) {
            StringBuilder newLine = new StringBuilder(doctorName + ",");

            for (int i = 9; i <= 11; i++) {
                newLine.append(i).append("am:F;");
            }

            newLine.append("12pm:F;");

            // Add PM timeslots
            for (int i = 1; i <= 8; i++) {
                newLine.append(i).append("pm:F;");
            }

            newLine.append(",").append(date);  // Note the comma before the date
            bw.write(newLine.toString());
            bw.newLine();
            } catch (IOException ex) {
        }
    }
    
    public void unavailableSchedule(String doctorName, String time, String date) {
        try {
            // Read all lines into memory
            List<String> lines = Files.readAllLines(Paths.get(SCHEDULE_FILE_PATH));

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts[0].equals(doctorName) && parts[parts.length - 1].equals(date)) {
                    String[] slots = parts[1].split(";");
                    for (int j = 0; j < slots.length; j++) {
                        String[] timeAndStatus = slots[j].split(":");
                        if (timeAndStatus[0].equals(time) && (timeAndStatus[1].equals("F")||timeAndStatus[1].equals("B"))) {
                            timeAndStatus[1] = "U";
                            slots[j] = timeAndStatus[0] + ":" + timeAndStatus[1];
                            JOptionPane.showMessageDialog(null, "Schedule upload successfully!");
                        }
                    }
                    parts[1] = String.join(";", slots);
                    lines.set(i, String.join(",", parts));
                }
            }

            // Write all lines back to the file
            Files.write(Paths.get(SCHEDULE_FILE_PATH), lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void availableSchedule(String doctorName, String time, String date) {
        try {
            // Read all lines into memory
            List<String> lines = Files.readAllLines(Paths.get(SCHEDULE_FILE_PATH));

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts[0].equals(doctorName) && parts[parts.length - 1].equals(date)) {
                    String[] slots = parts[1].split(";");
                    for (int j = 0; j < slots.length; j++) {
                        String[] timeAndStatus = slots[j].split(":");
                        if (timeAndStatus[0].equals(time) && timeAndStatus[1].equals("U")) {
                            timeAndStatus[1] = "F";
                            slots[j] = timeAndStatus[0] + ":" + timeAndStatus[1];
                            JOptionPane.showMessageDialog(null, "Schedule upload successfully!");
                        }
                    }
                    parts[1] = String.join(";", slots);
                    lines.set(i, String.join(",", parts));
                }
            }

            // Write all lines back to the file
            Files.write(Paths.get(SCHEDULE_FILE_PATH), lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void cancelAppointmentStatus(String doctorName, String time, String date) {
        try {
            // Read all lines from the schedule file
            List<String> lines = Files.readAllLines(Paths.get(SCHEDULE_FILE_PATH));
            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts[0].equals(doctorName) && parts[parts.length - 1].equals(date)) {
                    String[] timeslots = parts[1].split(";");
                    for (int j = 0; j < timeslots.length; j++) {
                        String[] timeAndStatus = timeslots[j].split(":");
                        if (timeAndStatus[0].equals(time) && timeAndStatus[1].equals("B")) {
                            timeslots[j] = time + ":U"; 
                            updated = true;
                            break;
                        }
                    }
                    // Reconstruct the line with updated timeslots
                    parts[1] = String.join(";", timeslots);
                    lines.set(i, String.join(",", parts));
                    break;
                }
            }

            if (updated) {
                // Write all lines back to the schedule file
                Files.write(Paths.get(SCHEDULE_FILE_PATH), lines);

            } else {
                System.out.println("No matching appointment found or appointment is not available for cancellation.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getDoctorAppointmentsHistory() {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if (lines[1].equals(username) && lines[6].equals("Done")) {
                    records.add(lines);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    public List<String[]> searchHistory(String searchDate, String name) throws IOException {
        List<String[]> results = new ArrayList<>();
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                boolean dateMatches = searchDate != null && !searchDate.isEmpty() && fields[3].equals(searchDate);
                boolean nameMatches = name != null && !name.isEmpty() && fields[1].equalsIgnoreCase(name);

                if (dateMatches && nameMatches && fields[6].equals("Done")) {
                    results.add(fields);
                } else if (dateMatches && (name == null || name.isEmpty())) {
                    results.add(fields);
                } else if (nameMatches && (searchDate == null || searchDate.isEmpty())) {
                    results.add(fields);
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return results;
    }
  
    
}
