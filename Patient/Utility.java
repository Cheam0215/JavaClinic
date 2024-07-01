/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;

import ASSG01.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static List<String[]> readTextFile(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Read line: " + line);  
                String[] row = line.split(",");
                if (row.length == 9) { 
                    data.add(row);
                } else {
                    System.err.println("Invalid row: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
