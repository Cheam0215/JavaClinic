/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;

/**
 *
 * @author Maxcm
 */
public class Session {
    private static String currentIC;

    public static String getCurrentIC() {
        return currentIC;
    }

    public static void setCurrentIC(String ic) {
        currentIC = ic;
    }

    public static void clear() {
        currentIC = null;
    }
}