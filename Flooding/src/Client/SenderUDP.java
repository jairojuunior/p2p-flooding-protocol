/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Jairo
 */

public class SenderUDP extends Thread{
    private final String IP;
    private final String port;
    private final String name;
    private final String IP_peer;
    private final String port_peer;
    private final String name_peer;
    private final String filename;
    private final DatagramSocket socket;
    
    public SenderUDP(String IP, String port, String name, String IP_peer, 
            String port_peer, String name_peer, String filename) throws SocketException{
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.IP_peer = IP_peer;
        this.port_peer = port_peer;
        this.name_peer = name_peer;
        this.filename = filename;
        this.socket = new DatagramSocket();
    }
    
    public void run(){
            String query = serializeData(this.IP, this.port, this.name, this.filename); 
            
            byte[] sendData = new byte[query.getBytes().length];
            sendData = query.getBytes();

            try {
                InetAddress destinationIP = InetAddress.getByName(this.IP_peer);
                int destinationPort = Integer.parseInt(this.port_peer);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationIP, destinationPort);
                this.socket.send(sendPacket);
                
                System.out.println("Sent the table below to "+this.name_peer);
                System.out.println(query);
                System.out.println("------------END OF TABLE------------");
            } catch (UnknownHostException ex) {
                Logger.getLogger(SenderUDP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SenderUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
    }         

    private String serializeData(String IP, String port, String name, String filename) {
        JSONObject json = new JSONObject();
        
        json.put("ip", IP);
        json.put("port", port);
        json.put("peer", name);
        json.put("filename", filename);
        
        return json.toString();
    }
}