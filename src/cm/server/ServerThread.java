/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.server;

import cm.ConnectToDatabase;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author linhsan
 */
public class ServerThread implements Runnable {
    private Socket incoming;
    private ConnectToDatabase con;
    public ServerThread(Socket i) {
        con = new ConnectToDatabase();
        incoming = i;
    }

    @Override
    public void run() {
        try {
            try {
                //create inputstream, outputstream of socket for this thread
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
                
                //Scanner for receive query from client
                Scanner in = new Scanner(inStream);
                //Return cacherowset object to client
                ObjectOutputStream oos = new ObjectOutputStream(outStream);
                
                boolean done = false;                        
                while (!done) {
                    if (in.hasNextLine()) {
                        String query = in.nextLine();
                        //receive "done", this thread will be close
                        if (query.toLowerCase().equals("done")) {
                            con.conClose();
                            done = true;
                        }
                        else if (query.toLowerCase().contains("insert") || query.toLowerCase().contains("update") || query.toLowerCase().contains("delete")) { 
                            con.update(query);
                        }
                        else if (query.toLowerCase().contains("select")) {
                            CachedRowSet crs = con.getCRS(query);
                            if (crs.isBeforeFirst()) {
                                //send the result
                                oos.writeObject(crs);
                            }
                            else {
                                //not have result, send a String
                                oos.writeObject("don't have any result");
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                System.out.println("finish this thread");
                incoming.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
