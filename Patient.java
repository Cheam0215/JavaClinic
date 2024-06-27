/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinicmanagementsystem;

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
    
    
}
