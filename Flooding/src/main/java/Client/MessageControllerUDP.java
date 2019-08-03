/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Lucas
 */
public class MessageControllerUDP {
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
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
	public boolean validateMessage(JSONObject message) {
		if (message == null) {
			System.out.println("Message is null");
			return false;
		} else if (!message.has("from")) {
			System.out.println("Message doesn't have From property");
			return false;
		} else if (message.getJSONArray("from").length() != 3) {
			System.out.println("Message From property doesn't is a valid arrray");
			return false;
		} else if (!message.has("status")) {
			System.out.println("Message doesn't have Port property");
			return false;
		} else if (!message.has("fileName")) {
			System.out.println("Message doesn't have FileName property");
			return false;
		} else if (!message.has("fileNameFullPath")) {
			System.out.println("Message doesn't have FileNameFullPath property");
			return false;
		} else if (!message.has("fileSize")) {
			System.out.println("Message doesn't have FileSize property");
			return false;
		} else if (!message.has("timestamp")) {
			System.out.println("Message doesn't have Timestamp property");
			return false;
		}
		
		return true;
	}
	
	//Check if the response message indicates that the file was found or not
	public boolean checkResponse(JSONObject message) {
		boolean fileFound = message.getString("status").equals("OK");
		String fileName = message.getString("fileName");
		
		if (fileFound) {
			System.out.println(fileName + " found in " + message.getJSONArray("from").getString(2));
		} else {
			System.out.println(fileName + " not found");
		}
		
		return fileFound;
	}
	
	//Start the request of the file by TCP
	public void startRequest(JSONObject message) {
		//needs implementation
	}
}
