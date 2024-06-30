package Assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class Doctor extends User {
    private static final String APPOINTMENTS_FILE_PATH = "C:\\Users\\User\\OneDrive - Asia Pacific University\\JAVA\\appointments.txt";
    private static final String MEDICAL_FILE_PATH = "C:\\Users\\User\\OneDrive - Asia Pacific University\\JAVA\\medicalRecords.txt";
    private static final String SCHEDULE_FILE_PATH = "C:\\Users\\User\\OneDrive - Asia Pacific University\\JAVA\\schedule.txt";
    private final String username;

    // Updated formatter to use "MMMM yyyy"
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
                    (lines[6].equals("Booked") || lines[6].equals("Checked In"))) {
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
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
            BufferedReader br = new BufferedReader(fr)) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(",");
            if (lines[1].equals(username) && lines[3].equals(new dateFormatter().formatFutureDate(1)) &&
                (lines[6].equals("Booked") || lines[6].equals("Checked In"))) {
            records.add(lines);
        }
    }
    }
  return records;
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
                if (fields.length >= 7 && fields[2].equals(ICNumber) && fields[3].equals(appDate)) {
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
    
    public List<String> getDoctorSchedule() {
        try {
          FileReader reader = new FileReader(SCHEDULE_FILE_PATH);
          BufferedReader br = new BufferedReader(reader);

          String line = br.readLine();

          // Extract doctor schedule from the line
          if (line != null) {
            String[] doctorSchedule = line.split(",");
            String username = doctorSchedule[0];
            String[] scheduleDetails = doctorSchedule[1].split(";");
            String date = doctorSchedule[2];

            // Return list of time-availability strings
            return Arrays.asList(scheduleDetails).stream()
                .map(detail -> String.format("%s:%s", detail.split(":")[0], detail.split(":")[1]))
                .collect(Collectors.toList());

            } else {
            return new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
          return new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error: IOException");
          return new ArrayList<>();
        }
    }

  
}

