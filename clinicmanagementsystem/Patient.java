/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinicmanagementsystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Sheng Ting
 */
public class Patient extends User{
    private String role = "Patient";
    
    
    public Patient(){
    }
    
    public Patient (String ICNumber, String username, String password, String gender, String birthDate){
        super(ICNumber,username,password,gender,birthDate);
        super.setRole(this.role);
    }
    
    
   public List<String[]> getSchedule(String doctorName, String date) {
    List<String[]> records = new ArrayList<>();
    try (FileReader fr = new FileReader("schedule.txt");
         BufferedReader br = new BufferedReader(fr)) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals(doctorName) && parts[parts.length - 1].equals(date)) {
                // Extract time slots
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
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("schedule.txt", true))) {
        StringBuilder newLine = new StringBuilder(doctorName + ",");

        // Add AM timeslots
        for (int i = 9; i <= 11; i++) {
            newLine.append(i).append("am:F;");
        }

        // Add 12 PM timeslot
        newLine.append("12pm:F;");

        // Add PM timeslots
        for (int i = 1; i <= 8; i++) {
            newLine.append(i).append("pm:F;");
        }

        // Append date separated by a comma
        newLine.append(",").append(date);  // Note the comma before the date
        bw.write(newLine.toString());
        bw.newLine();
    } catch (IOException ex) {
        Logger.getLogger(Patient.class.getName()).log(Level.SEVERE, null, ex);
    }
}



    
    public void makeAppointment(String doctorName, String time, String date) {
    try {
        // Read all lines into memory
        List<String> lines = Files.readAllLines(Paths.get("schedule.txt"));
        
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            
            if (parts[0].equals(doctorName) && parts[parts.length - 1].equals(date)) {
                String[] slots = parts[1].split(";");
                for (int j = 0; j < slots.length; j++) {
                    String[] timeAndStatus = slots[j].split(":");
                    if (timeAndStatus[0].equals(time) && timeAndStatus[1].equals("F")) {
                        timeAndStatus[1] = "B";
                        slots[j] = timeAndStatus[0] + ":" + timeAndStatus[1];
                        JOptionPane.showMessageDialog(null, "Appointment made successfully!");
                    }
                }
                parts[1] = String.join(";", slots);
                lines.set(i, String.join(",", parts));
            }
        }
        
        // Write all lines back to the file
        Files.write(Paths.get("schedule.txt"), lines);
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
 public void addAppointments(String ICNumber, String date, String time, String doctorName, String description, String status) {

        // If no matching appointment is found, add the new appointment
        try (FileWriter fw = new FileWriter("appointments.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String newAppointment = String.join(",",getNamebyICNumber(ICNumber), doctorName, ICNumber, date, time, description, status) + "\n";
            bw.write(newAppointment);
            JOptionPane.showMessageDialog(null," Record added successfully!");
        } catch (IOException e) {
           
        }
    }

}
