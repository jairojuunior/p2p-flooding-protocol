/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floodingclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.json.JSONObject;

/**
 *
 * @author Lucas
 */
public class ListenerUDP extends Thread {
    private String IP;
    private String port;
    private MessageControllerUDP controller;
    // need to implement timeout
    
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
    			
    			String serializedData = packet.getData().toString();
    			
    			JSONObject message = controller.deserializeMessage(serializedData);
    			if (controller.validateMessage(message)) {
    				if (controller.checkResponse(message)) {
    					controller.startRequest(message);
    				}
    			}
    		}
    		
    		//socket.close();
    	} catch (Exception ex) {
    		// Exception from socket
    		System.out.println("Something went very wrong");
    	}
    }
}
