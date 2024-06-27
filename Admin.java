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
    private static final String APPOINTMENTS_FILE_PATH = "C:\\Users\\Sheng Ting\\Desktop\\appointments.txt";
    public String getData(){
        return null;
    }
    
    public List<String[]> getDailyAppointments() {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
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
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (searchDate != null && !searchDate.isEmpty()) {
                    if (fields[3].equals(searchDate) && fields[0].equalsIgnoreCase(name)) { // Assuming date is at index 3 and name at index 0
                        results.add(fields);
                    }
                } else {
                    if (fields[0].equalsIgnoreCase(name)) {
                        results.add(fields);
                    }
                }
            }
        } catch (IOException e) {
            
            throw e;
        }
        return results;
    }
    
    public void checkIn(String name,String appDate){
        File file = new File(APPOINTMENTS_FILE_PATH);
        List<String[]> records = new ArrayList<>();
        boolean updated = false;
        
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
        try (FileReader fr = new FileReader("C:\\Users\\Sheng Ting\\Desktop\\medicalRecords.txt");
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

        try (FileWriter fw = new FileWriter("C:\\Users\\Sheng Ting\\Desktop\\medicalRecords.txt");
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
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
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

        try (FileWriter fw = new FileWriter(APPOINTMENTS_FILE_PATH);
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
        try (FileWriter fw = new FileWriter(APPOINTMENTS_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String newAppointment = String.join(",",getNamebyICNumber(ICNumber), doctorName, ICNumber, date, time, description, status) + "\n";
            bw.write(newAppointment);
            JOptionPane.showMessageDialog(null," Record added successfully!");
        } catch (IOException e) {
           
        }
    }
    
    private String getNamebyICNumber(String ICNumber){
        try (FileReader fr = new FileReader("C:\\Users\\Sheng Ting\\Desktop\\user.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if(ICNumber.equals(fields[0]))
                    return fields[1];
            
            }
        }catch (IOException e){
    
        }
        return null;
    }
    

    private boolean isAppointmentExists(String ICNumber, String date, String time) {
        try (FileReader fr = new FileReader(APPOINTMENTS_FILE_PATH);
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
}


          
    



