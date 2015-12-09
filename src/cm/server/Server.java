/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author linhsan
 */
public class Server {
    public static void main(String[] args) {        
        try {
            ServerSocket s = new ServerSocket(8189);
            int i = 1;
            while (true) {
                Socket incoming = s.accept();
                System.out.println("spawning "+i);
                Runnable r = new ServerThread(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
