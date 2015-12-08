/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author linhsan
 */
public class ConnectToServer {
    private Socket s;
    private InputStream inStream;
    private OutputStream outStream;
    
    private ObjectInputStream readFromStream;
    private PrintWriter printToStream;

    public ConnectToServer() {
        try {
            //connect to socket
            s = new Socket("localhost", 8189);
            inStream = s.getInputStream();
            outStream = s.getOutputStream();
            readFromStream = new ObjectInputStream(inStream);
            printToStream = new PrintWriter(outStream, true);
        } catch (IOException ex) {
            Logger.getLogger(ConnectToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendToServer(String query) {
        printToStream.println(query);
    }
    
    public Object receiveFromServer() {
        try {
            return readFromStream.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ConnectToServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
