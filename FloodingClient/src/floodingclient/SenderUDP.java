/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floodingclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jairo
 */

public class SenderUDP extends Thread{
    private final String IP;
    private final String port;
    private final String name;
    private final JSONArray LIST_OF_PEERS;
    private final String filename;
    private final DatagramSocket socket;
    
    public SenderUDP(String IP, String port, String name, String filename, JSONArray LIST_OF_PEERS) throws SocketException{
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.LIST_OF_PEERS = LIST_OF_PEERS;
        this.filename = filename;
        this.socket = new DatagramSocket();
    }
    
    public void run(){
            String query = serializeData(this.IP, this.port, this.name, this.filename); 
            JSONObject peer = choosePeer();
            byte[] sendData = new byte[query.getBytes().length];
            sendData = query.getBytes();

            try {
                InetAddress destinationIP = InetAddress.getByName(peer.get("IP").toString());
                int destinationPort = Integer.parseInt(peer.get("port").toString());
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationIP, destinationPort);
                this.socket.send(sendPacket);
                
                System.out.println("Sent the query below to "+peer.get("name").toString());
                System.out.println(query);
                System.out.println("------------END OF QUERY------------");
            } catch (UnknownHostException ex) {
                Logger.getLogger(SenderUDP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SenderUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.socket.close();
    }         

    private String serializeData(String IP, String port, String name, String filename) {
        JSONObject json = new JSONObject();
        json.put("ip", IP);
        json.put("port", port);
        json.put("peer", name);
        json.put("filename", filename);
        
        return json.toString();
    }
    
    private JSONObject choosePeer(){
        Random rand = new Random();
        int number_of_peers = LIST_OF_PEERS.length();
        int selected_peer = rand.nextInt(number_of_peers);
        return this.LIST_OF_PEERS.getJSONObject(selected_peer).getJSONObject("peer");
    }
    
}