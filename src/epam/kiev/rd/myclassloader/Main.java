/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epam.kiev.rd.myclassloader;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ihor_Shnaider
 */
public class Main {

    private static final String CLASS_NAME = "epam.kiev.rd.myclassloader.TestModule";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Press <Enter> to get TestModule string.");
        System.out.println("Or input 'exit' to exit.");
        Scanner keyboard;
        keyboard = new Scanner(System.in);
        String input;

        boolean endFlag = true;
        while (endFlag) {
            input = keyboard.nextLine();
            if ("exit".equals(input)) {
                endFlag = false;
            } else {
                try {
                    MyClassLoader myClassLoader = new MyClassLoader();
                    Class testModule = myClassLoader.loadClass(CLASS_NAME);
                    if (testModule != null) {
                        System.out.println(testModule.newInstance());
                    }
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
