package fromNet;

import java.io.*;
 
public class LoadFiles {
 
    public byte[] loadBinaryFile(String name) {
        try {
 
            DataInputStream dis = new DataInputStream(new FileInputStream(name));
            byte[] theBytes = new byte[dis.available()];
            dis.read(theBytes, 0, dis.available());
            dis.close();
            return theBytes;
        } catch (IOException ex) {
        }
        return null;
    }
}