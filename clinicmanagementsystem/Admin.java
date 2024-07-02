/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinicmanagementsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Admin extends User {
    
    public String getData(){
        return null;
    }
    
    public List<String[]> getDailyAppointments() {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader("appointments.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                records.add(lines);
            }
        } catch (IOException e) {
            
        }
        return records;
    }

     public List<String[]> searchDate(String searchDate, String name) throws IOException {
        List<String[]> results = new ArrayList<>();
        try (FileReader fr = new FileReader("appointments.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                boolean dateMatches = searchDate != null && !searchDate.isEmpty() && fields[3].equals(searchDate);
                boolean nameMatches = name != null && !name.isEmpty() && fields[0].equalsIgnoreCase(name);

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
     
     public List<String[]> searchMedical(String searchDate, String name) throws IOException {
        List<String[]> results = new ArrayList<>();
        try (FileReader fr = new FileReader("medicalRecords.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                boolean dateMatches = searchDate != null && !searchDate.isEmpty() && fields[3].equals(searchDate);
                boolean nameMatches = name != null && !name.isEmpty() && fields[0].equalsIgnoreCase(name);

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

    
    public void checkIn(String name,String appDate){
        File file = new File("appointments.txt");
        List<String[]> records = new ArrayList<>();
        boolean updated = true;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if (name.equals(lines[0]) && appDate.equals(lines[3])) {
                    lines[lines.length - 1] = "Checked In";
                    updated = true;
                }
                records.add(lines);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        
        if (updated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String[] record : records) {
                    bw.write(String.join(",", record));
                    bw.newLine();
                }
                JOptionPane.showMessageDialog(null, "Check-in successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occurred while writing to the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "No matching appointment found for check-in.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
   
    
    public void makePayment(String ICNumber, String appDate, String paymentAmount) {
        List<String> lines = new ArrayList<>();
        try (FileReader fr = new FileReader("medicalRecords.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[1].equals(ICNumber) && fields[3].equals(appDate)) {
                    fields[fields.length - 2] = "Paid"; // Update payment status
                    fields[fields.length - 1] = paymentAmount; // Update amount paid
                    line = String.join(",", fields);
                }
                lines.add(line);
            }
            
        } catch (IOException e) {
           
        }

        try (FileWriter fw = new FileWriter("medicalRecords.txt");
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (String updatedLine : lines) {
                bw.write(String.join(",", updatedLine));
                bw.newLine();
            }
        } catch (IOException e) {
            
        }
    }
    
    public void cancelAppointments(String ICNumber, String appDate, String description) {
        List<String> lines = new ArrayList<>();
        try (FileReader fr = new FileReader("appointments.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[2].equals(ICNumber) && fields[3].equals(appDate) && fields[5].equals(description)) { 
                    fields[fields.length - 1] = "Cancelled"; 
                    line = String.join(",", fields);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            
        }

        try (FileWriter fw = new FileWriter("appointments.txt");
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (String updatedLine : lines) {
                bw.write(String.join(",", updatedLine));
                bw.newLine();
            }
        } catch (IOException e) {
        }
    }
    
    public void addAppointments(String ICNumber, String date, String time, String doctorName, String description, String status) {
        // Check if an appointment with the same IC Number, date, and time already exists
        if (isAppointmentExists(ICNumber, date, time)) {
            JOptionPane.showMessageDialog(null, "An appointment with the same IC Number, date, and time already exists !");
        }

        // If no matching appointment is found, add the new appointment
        try (FileWriter fw = new FileWriter("appointments.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String newAppointment = String.join(",",getNamebyICNumber(ICNumber), doctorName, ICNumber, date, time, description, status) + "\n";
            bw.write(newAppointment);
            JOptionPane.showMessageDialog(null," Record added successfully!");
        } catch (IOException e) {
           
        }
    }
    

    private boolean isAppointmentExists(String ICNumber, String date, String time) {
        try (FileReader fr = new FileReader("appointments.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6 && fields[0].equals(ICNumber) && fields[1].equals(date) && fields[2].equals(time)) {
                    return true; // Matching appointment found
                }
            }
        } catch (IOException e) {
            
        }
        return false; // No matching appointment found
    }
    
    public List<String[]> getAllPaymentRecord() {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader("medicalRecords.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if (lines.length >= 8 && lines[7].equals("Paid")) {
                    String[] record = new String[5];
                    record[0] = lines[0]; // patientName
                    record[1] = lines[1]; // ICNumber
                    record[2] = lines[3]; // Date
                    record[3] = lines[5]; // prescription
                    record[4] = lines[8]; // paymentAmount
                    records.add(record);
                }
            }
        } catch (IOException e) {
           
        }
        return records; 
    }
    
    public List<String[]> searchPaymentDate(String searchDate){
        List<String[]> records = new ArrayList<>();
         try (FileReader fr = new FileReader("medicalRecords.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if (lines.length >= 8 && lines[7].equals("Paid") && lines[3].equals(searchDate)) {
                    String[] record = new String[5];
                    record[0] = lines[0]; // patientName
                    record[1] = lines[1]; // ICNumber
                    record[2] = lines[3]; // Date
                    record[3] = lines[5]; // prescription
                    record[4] = lines[8]; // paymentAmount
                    records.add(record);
                }
            }
        } catch (IOException e) {
           
        }
        return records;
    }
    
    }



          
    



