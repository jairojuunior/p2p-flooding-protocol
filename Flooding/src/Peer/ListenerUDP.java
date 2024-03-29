/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.json.JSONObject;

/**
 *
 * @author Jairo
 */
public class ListenerUDP extends Thread{
     private final String IP;
     private final String port;
     private MessageControllerUDP controller;
    public ListenerUDP(String IP, String port) {
        this.IP = IP;
        this.port = port;
        controller = new MessageControllerUDP();
    }
    
    //Run method from Thread inheritance. Continuously loop receiving messages and treating them
    public void run() {
    	try {
    		DatagramSocket socket = new DatagramSocket(Integer.parseInt(port));
    		
    		while (true) {
    			// Verify buffer length
    			byte[] buffer = new byte[1024];
    			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    			
    			socket.receive(packet);
    			
                        String serializedData = new String(packet.getData());
                        System.out.println("NEW MESSAGE RECEIVED: "+serializedData);
                        controller.main(serializedData);    			
    		}
    		
    		//socket.close();
    	} catch (Exception ex) {
    		// Exception from socket
    		System.out.println("Something went very wrong");
    	}
    }
}
