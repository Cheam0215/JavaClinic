package clinicmanagementsystem;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

 
public class User {
    private String ICNumber;
    private String username;
    private String password;
    private String gender;
    private String birthDate;
    private String role;

    private static final String USER_FILE_PATH = "user.txt";

    
    public User(){
        
    }
    
    public User(String ICNumber, String username, String password, String gender, String birthDate){
       this.ICNumber = ICNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        
   }
    
   public User(String ICNumber, String username, String password, String gender, String birthDate, String role){
       this.ICNumber = ICNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
   }
    
    public void setData(String ICNumber, String username, String password, String gender, String birthDate, String role){
        this.ICNumber = ICNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
    }
    
    public String getICNumber() {
        return ICNumber;
    }

    public void setICNumber(String ICNumber) {
        this.ICNumber = ICNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    
    public void setRole(String role){
        this.role = role;
    }
    

    public void register(JFrame currentFrame) {
        if(isValidIC(this.ICNumber)) {
            if (icExists(this.ICNumber)) {
            JOptionPane.showMessageDialog(null, "IC Number already exists!");
        } else {
            saveUserToFile(currentFrame);
        } 
        }else{
            JOptionPane.showMessageDialog(null, "Incorrect IC format");
        }
        
    }

    public boolean icExists(String ICNumber) {
        try (FileReader fr = new FileReader(USER_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if (ICNumber.equals(lines[0])) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while checking the IC Number: " + e.getMessage());
        }
        return false;
    }

     void saveUserToFile(JFrame currentFrame) {
         
        try (FileWriter fw = new FileWriter(USER_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String userData = String.join(",", this.ICNumber, this.username, this.password, this.gender, this.birthDate, this.role) + "\n";
            bw.write(userData);
            JOptionPane.showMessageDialog(null, "Registration successful!");
            currentFrame.dispose();
            Login login = new Login();
            login.setVisible(true);
            
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to the file: " + e.getMessage());
        }
    }
     
     public List<String[]> getAllMedicalRecords() {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader("medicalRecords.txt");
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

     
      public List<String[]> searchMedicalRecords(String searchName, String medDate) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (FileReader fr = new FileReader("medicalRecords.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                if(searchName.equals(lines[0]) || medDate.equals(lines[3])){
                    records.add(lines);
                }
                
            }
        } catch (IOException e) {
            
        }
        return records;
    }

      public static boolean isValidIC(String ICNumber) {
        // Check if the IC number is exactly 12 digits
        if (ICNumber == null || ICNumber.length() != 12) {
            return false;
        }

        // Check if all characters are digits
        for (char c : ICNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        String birthDate = ICNumber.substring(0, 6);
        
        if (isValidDate(birthDate)) {
        } else {
            return false;
        }

        return true;
    }
      
      private static boolean isValidDate(String date) {
        if (date.length() != 6) {
            return false;
        }

        int month = Integer.parseInt(date.substring(2, 4));
        int day = Integer.parseInt(date.substring(4, 6));

        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }

        return true;
    }

    public String getNamebyICNumber(String ICNumber){
    try (FileReader fr = new FileReader("user.txt");
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
    
    


}
