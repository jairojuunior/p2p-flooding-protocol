/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jairo
 */
public class MessageControllerUDP {
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
        public void main(String serializedData) throws SocketException{
            JSONObject message = deserializeMessage(serializedData);
            int validationCode = validateMessage(message);
            if (validationCode==1) { //CODE 1: message from client
                String client_ip = message.getString("ip");
                String client_port = message.getString("port");
                String client_name = message.getString("name");
                String client_filename = message.getString("filename");
                int blacklistTime = 5;
                System.out.println("here!");
                
                //PUT IN RECENTQUERIES
                PeerDAO.INSTANCE.recentQueries.create(client_ip,
                        client_port, 
                        client_name,
                        client_filename,
                        blacklistTime);
                System.out.println("Request saved on RECENT QUERIES");
                
                //VERIFY IF HAS FILE IN MYFILETABLE
                ArrayList filesAvailable = PeerDAO.INSTANCE.myFileTable.getAllKeys();
                
                if(filesAvailable.contains(client_filename)){
                    //ANSWER TO CLIENT (SUCCESSFUL)
                    
                    String fullPath = PeerDAO.INSTANCE.myFileTable.get(client_filename).getFullPath();
                    long fileSize = PeerDAO.INSTANCE.myFileTable.get(client_filename).getSize();

                    SenderUDP.sendToClient(client_ip, client_port, client_name, 
                            PeerDAO.IP, PeerDAO.port, PeerDAO.name, "OK",
                            client_filename, fullPath, fileSize);
                    
                }else{
                    //RELAY QUERY TO ANOTHER PEER WITH MAX_TTL
                    SenderUDP.sendToPeer(client_ip, client_port, client_name, 
                            PeerDAO.IP, PeerDAO.port, PeerDAO.name, 
                            client_filename, PeerDAO.MAX_TTL);
                }
                
                
            }else if(validationCode == 2){//CODE 2: message from peer
                String client_ip = message.getJSONObject("replyTo").get("IP").toString();
                String client_port = message.getJSONObject("replyTo").get("port").toString();
                String client_name = message.getJSONObject("replyTo").get("name").toString();
                String client_filename = message.get("filename").toString();
                String peerSender_ip = message.getJSONObject("from").get("IP").toString();
                String peerSender_port = message.getJSONObject("from").get("port").toString();
                String peerSender_name = message.getJSONObject("from").get("name").toString();
                int TTL = Integer.parseInt(message.get("TTL").toString());
                
                //ALL FILES AVAILABLES IN THIS PEER
                ArrayList filesAvailable = PeerDAO.INSTANCE.myFileTable.getAllKeys();
                
                //CHECK IF QUERY IS BLOCKED (IF SO, RELAY WITHOUT DECREASING TTL)
                ArrayList blacklistedQueries = PeerDAO.INSTANCE.recentQueries.getAllKeys();
                if(blacklistedQueries.contains(client_ip+client_port+client_filename)){
                    System.out.println("REQUEST WAS BLACKLISTED => RELAYING...");
                    SenderUDP.sendToPeer(client_ip, client_port, client_name, 
                            PeerDAO.IP, PeerDAO.port, PeerDAO.name, 
                            client_filename, TTL);
                }else if(TTL==0){//IF TTL=0 RETURN TO CLIENT FILE NOT FOUND
                    SenderUDP.sendToClient(client_ip, client_port, client_name, 
                            PeerDAO.IP, PeerDAO.port, PeerDAO.name, "FILE NOT FOUND",
                            client_filename, "", 0);
                }else if(filesAvailable.contains(client_filename)){ //IF FILE IS AVAILABLE, RETURN TO CLIENT OK
                    
                    String fullPath = PeerDAO.INSTANCE.myFileTable.get(client_filename).getFullPath();
                    long fileSize = PeerDAO.INSTANCE.myFileTable.get(client_filename).getSize();
                    
                    SenderUDP.sendToClient(client_ip, client_port, client_name, 
                            PeerDAO.IP, PeerDAO.port, PeerDAO.name, "OK",
                            client_filename, fullPath, fileSize);
                }else{//RELAY TO ANOTHER PEER DECREASING TTL
                    SenderUDP.sendToPeer(client_ip, client_port, client_name, 
                            PeerDAO.IP, PeerDAO.port, PeerDAO.name, 
                            client_filename, TTL-1);
                }

            }
        }
        /*Return a JSON from a string if is valid and not an array*/
	public JSONObject deserializeMessage(String serializedData) {
		try {
			JSONObject data = new JSONObject(serializedData);
			return data;
		} catch (JSONException e) {
			// it is possible that JSON is an array while in catch
			System.out.println("Invalid data. Cannot be deserialized. Null value is assigned.");
			return null;
		}
	}
        //Check if a JSON contains all properties of the Response Message Template
	//Properties: from [IP, port, name], status, fileName, fileNameFullPath, fileSize, timestamp
	public int validateMessage(JSONObject message) {
		if (message == null) {
			System.out.println("Message is null");
			return 0;
		}else if (message.has("replyTo")){
                    //VALIDATION PEER TO PEER
                    if (!message.has("from")) {
			System.out.println("Message doesn't have from property");
			return 0;
                    }else if (!message.has("TTL")) {
			System.out.println("Message doesn't have TTL property");
			return 0;
                    }else if (!message.has("timestamp")) {
			System.out.println("Message doesn't have timestamp property");
			return 0;
                    }else if (!message.has("filename")) {
			System.out.println("Message doesn't have filename property");
			return 0;
                    }
                    System.out.println("Message received from peer validated!");
                    return 2;
                }else{
                    //VALIDATION CLIENT TO PEER
                    if (!message.has("ip")) {
			System.out.println("Message doesn't have IP property");
			return 0;
                    }else if (!message.has("port")) {
			System.out.println("Message doesn't have port property");
			return 0;
                    }else if (!message.has("peer")) {
			System.out.println("Message doesn't have peer (name) property");
			return 0;
                    }else if (!message.has("filename")) {
			System.out.println("Message doesn't have filename property");
			return 0;
                    }
                    System.out.println("Message received from client validated!");
                    return 1;
                }
	}
    
}
