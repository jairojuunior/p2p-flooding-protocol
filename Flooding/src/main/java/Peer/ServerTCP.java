/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Lucas
 */
public class ServerTCP extends Thread {
	// Size of the buffer used to send file
	private final int fileBufferSize;
	
	// Port used to transfer the file
	private String port;
	
	// Socket to connect to the client
	private Socket socket = null;
	
	// Name of the requested file
	private String fileName;
	
	// Full name of the local path of the file
	private String filePath;
	
	public ServerTCP(String port) {
		this.port = port;
		//problem when peerDAO is null
		PeerDAO peerDAO = PeerDAO.getInstance();
	}
	
    public void run() {
    	try {
    		ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port));
    		
    		// Waits for a new connection. Accepts connection from multiple clients
    		while (true) {
    			System.out.println("Waiting connection on port " + port);
    			socket = serverSocket.accept();
    			System.out.println("Connected with " + socket.getInetAddress());
    			
    			receiveRequest();
    			System.out.println(fileName + " download requested");
    			
    			fileBufferSize = (peerDAO.myFileTable.get(fileName).getSize()) * 11 / 10;
			//filePath = "C:\\Users\\lucas\\Desktop\\FloodingTestServer\\" + fileName;
			filePath = peerDAO.myFileTable.get(fileName).getFullPath();
    			
    			sendFile();
    			System.out.println(fileName + " download finished");
    			
    			socket.close();
    		}
    		
    		//serverSocket.close();
    	} catch (Exception ex) {
    		// Exception from TCP connection
    		System.out.println("Something went very wrong");
    	}
    }
    
    // Receive the request of a file from the Client by its name via TCP
    private void receiveRequest() {
    	if (socket == null) {
			System.out.println("Socket is null. Operation failed");
			return;
		}
    	
    	try {
    		// Creating input stream to read from Client
    		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
    		BufferedReader br = new BufferedReader(isr);
    		
    		// Receive the file name
    		fileName = br.readLine();
    	} catch (Exception ex) {
    		System.out.println("Receipt of request has failed");
    	}
    }
    
    // Send the file to the Client via TCP
    private void sendFile() {
    	if (socket == null) {
			System.out.println("Socket is null. Operation failed");
			return;
		}
    	
    	try {
    		// Creating output stream to write to Client
    		OutputStream os = socket.getOutputStream();
    		DataOutputStream clientWriter = new DataOutputStream(os);
    		
    		// Creating buffer to send file
    		byte[] buffer = new byte[fileBufferSize];
    		
    		// Creating input stream to read the file
    		final File myFile = new File(filePath);
			FileInputStream fis = new FileInputStream(myFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream fileReader = new DataInputStream(bis);
			
			// Sending the file
			int readCount;
			while ((readCount = fileReader.read(buffer)) != -1) {
				clientWriter.write(buffer, 0, readCount);
			}
    		
			fileReader.close();
    	} catch (Exception ex) {
    		System.out.println("File submission has failed");
    	}
    }
}
