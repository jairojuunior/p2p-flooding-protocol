/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floodingclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Lucas
 */
public class ClientTCP extends Thread {
	// Information from the Peer that has the file
	private String IP;
	private String port;
	private String name;
	
	// Name of the file with .mp4 extension included
	private String fileName;
	
	// Full name of the path where file will be saved with the file name at the end
	private String filePath;
	
	// Socket to connect to the server process running at host
	private Socket socket = null;
	
	public ClientTCP(String peerIP, String peerPort, String peerName, String fileName, String fileDestinyPath) {
		this.IP = peerIP;
		this.port = peerPort;
		this.name = peerName;
		this.fileName = fileName;
		this.filePath = fileDestinyPath;
	}
	
	public void run() {
    	try {
    		socket = new Socket(IP, Integer.parseInt(port));
    		
    		requestFile();
    		System.out.println("Downloading " + fileName + " from " + name);
    		
    		receiveFile();
    		System.out.println(fileName + " download complete");
    		
    		socket.close();
    	} catch (Exception ex) {
    		// Exception from TCP connection
    		System.out.println("Something went very wrong");
    	}
    }
	
	// Send the name of the file to the Peer via TCP
	private void requestFile() {
		if (socket == null) {
			System.out.println("Socket is null. Operation failed");
			return;
		}
		
		try {
			// Creating output stream to write to Server
			OutputStream os = socket.getOutputStream();
			DataOutputStream serverWriter = new DataOutputStream(os);
			
			// Sending the request
			serverWriter.writeBytes(fileName + "\n");
		} catch (Exception ex) {
			System.out.println("Request of the file has failed");
		}
	}
	
	// Receive the file from the Peer via TCP and saves in the specified path
	private void receiveFile() {
		if (socket == null) {
			System.out.println("Socket is null. Operation failed");
			return;
		}
		
		try {
			// Creating input stream to read from the Server
			InputStream is = socket.getInputStream();
    		DataInputStream serverReader = new DataInputStream(is);
    		
    		// Creating buffer to read the file
    		int bufferSize = socket.getReceiveBufferSize();
    		byte[] buffer = new byte[bufferSize];
    		
    		// Creating output stream to save the file
    		OutputStream fileWriter = new FileOutputStream(filePath);
    		
    		// Receiving the file
    		int readCount;
    		while((readCount = serverReader.read(buffer)) != -1){
    			fileWriter.write(buffer, 0, readCount);
            }
    		
    		fileWriter.close();
		} catch (Exception ex) {
			System.out.println("Receipt of the file has failed");
		}
	}
}
