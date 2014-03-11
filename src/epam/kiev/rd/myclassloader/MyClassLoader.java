/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epam.kiev.rd.myclassloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ihor_Shnaider
 */
public class MyClassLoader extends ClassLoader {
                                                    
    private final String CLASS_FILE_LOCATION_START = ".//build//classes";
    private final String JAVA_FILE_LOCATION_START = ".//src//";

    private byte[] getBytes(String filename) throws IOException {
        // Find out the length of the file
        File file = new File(filename);
        long len = file.length();
        // Create an array that's just the right size for the file's
        // contents
        byte raw[] = new byte[(int) len];
        try (FileInputStream fin = new FileInputStream(file)) {
            int r = fin.read(raw);
            if (r != len) {
                throw new IOException("Can't read all, " + r + " != " + len);
            }
        }
        // And finally return the file contents as an array
        return raw;
    }

    private boolean compile(String javaFile) throws IOException, Exception {
        // Let the user know what's going on
        System.out.println("Application: Compiling " + javaFile + "...");
        // Start up the compiler
        Process process = Runtime.getRuntime().exec("javac -cp . -d "+CLASS_FILE_LOCATION_START+" " + javaFile);
        try {
            process.waitFor();
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        // Check the return code, in case of a compilation error
        int ret = process.exitValue();
        if (ret != 0) {
            throw new Exception("" + process.exitValue());
        }
        return true;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class resultClass = null;
        String fileStub = name.replace('.', '\\');
        String javaFileName = JAVA_FILE_LOCATION_START + fileStub + ".java";
        String classFileName = CLASS_FILE_LOCATION_START+"\\" + fileStub + ".class";
        File javaFile = new File(javaFileName);
        File classFile = new File(classFileName);
        if (name.contains("java")) {
            return super.findSystemClass(name);
        }
        if (javaFile.exists()) {
            try {
                if (!compile(javaFileName)) {
                    throw new ClassNotFoundException("Compile failed: " + javaFileName);
                }
            } catch (Exception ex) {
                Logger.getLogger(MyClassLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("Compile Error");
        }
        try {
            if (classFile.exists()) {
                System.out.println(classFileName);
                byte[] classFileBytes = getBytes(classFileName);
                if (resultClass == null) {
                    resultClass = defineClass(name, classFileBytes, 0, classFileBytes.length);
                }
            } else {
                System.out.println("Define Error");
            }
        } catch (IOException ex) {
            Logger.getLogger(MyClassLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultClass;
        //To change body of generated methods, choose Tools | Templates.
    }

}
