/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jairo
 */
public class SenderUDP{
    
    //sendToClient(IP_client, port_client, name_client, IP, port, name, status, filename, filenameFullPath, fileSize)
    public static void sendToClient(String IP_client, String port_client, String name_client,
            String IP, String port, String name, String status, 
            String filename, String filenameFullPath, long fileSize) throws SocketException{
        
        String serializedData = serializeDataToClient(IP, port, name, status, filename, 
                filenameFullPath, fileSize);
        send(IP_client, port_client, name_client, serializedData);
    }

    private static String serializeDataToClient(String IP, String port, String name, String status, 
            String filename, String filenameFullPath, long fileSize) {

        String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        JSONObject from = new JSONObject();
        from.put("IP", IP);
        from.put("port", port);
        from.put("name", name);
        
        JSONObject json = new JSONObject();
        json.put("from", from.toString());
        json.put("status", status);
        json.put("filename", filename);
        json.put("filenameFullpath", filenameFullPath);
        json.put("fileSize", fileSize);
        json.put("Timestamp", timestamp);
        
        return json.toString();
    }

    //sendToClient(IP_client, port_client, name_client, IP, port, name, status, filename, filenameFullPath, fileSize)
    public static void sendToPeer(String IP_client, String port_client, String name_client,
            String IP, String port, String name, String filename, int TTL) throws SocketException{
        
        JSONObject chosenPeer = choosePeer(); //RANDOMLY CHOOSE A PEER
        while(!chosenPeer.get("name").toString().equals(name)){
            chosenPeer = choosePeer(); //RANDOM CHOOSE A PEER
        }
        String peerIP = chosenPeer.get("IP").toString();
        String peerPort = chosenPeer.get("port").toString();
        String peerName = chosenPeer.get("name").toString();
        
        String serializedData = serializeDataToPeer(IP_client, port_client, name_client, 
                IP, port, name, filename, TTL);
        
        send(peerIP, peerPort, peerName, serializedData);
    }    
    
    
    private static String serializeDataToPeer(String IP_client, String port_client, 
            String name_client, String IP, String port, String name,
            String filename, int TTL) {
        
        String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        //THIS PEER IDENTIFICATION
        JSONObject from = new JSONObject();
        from.put("IP", IP);
        from.put("port", port);
        from.put("name", name);
        
        //CLIENT IDENTIFICATION
        JSONObject replyTo = new JSONObject();
        replyTo.put("IP", IP_client);
        replyTo.put("port", port_client);
        replyTo.put("name", name_client);
        
        //MOUNTING FINAL JSON
        JSONObject json = new JSONObject();
        json.put("from", from.toString());
        json.put("replyTo", replyTo.toString());
        json.put("TTL", TTL);
        json.put("timestamp", timestamp);
        json.put("filename", filename);
        
        return json.toString();
    }
    
    private static JSONObject choosePeer(){
        Random rand = new Random();
        int number_of_peers = PeerDAO.LIST_OF_PEERS.length();
        int selected_peer = rand.nextInt(number_of_peers);
        return PeerDAO.LIST_OF_PEERS.getJSONObject(selected_peer).getJSONObject("peer");
    }
    
    private static void send(String IP, String port, String name, String serializedData) throws SocketException{
        DatagramSocket socket = new DatagramSocket();
        byte[] sendData = new byte[serializedData.getBytes().length];
        sendData = serializedData.getBytes();

        try {
            InetAddress destinationIP = InetAddress.getByName(IP);
            int destinationPort = Integer.parseInt(port);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationIP, destinationPort);
            socket.send(sendPacket);

            System.out.println("Sent the message below to "+name);
            System.out.println(serializedData);
            System.out.println("------------END OF QUERY------------");
        } catch (UnknownHostException ex) {
            Logger.getLogger(SenderUDP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SenderUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        socket.close();
    }
    
}
    
