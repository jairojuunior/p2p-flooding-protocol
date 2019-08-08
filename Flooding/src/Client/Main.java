/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Jairo
 */
public class Main {
    public static JSONObject LIST_OF_PEERS;
    public static void main(String[] args) throws Exception {
        // commmand line myIP myPort myName filename
        JSONArray LIST_OF_PEERS = readJSON("C:\\Users\\Jairo\\codes\\ufabc-sd\\simple-flooding-protocol\\Flooding\\values\\listOfPeers.json").getJSONArray("peers"); 
        /*
        String IP = args[0];
        String port = args[1];
        String name = args[2];
        String filename = args[3];
        */
        String IP = "127.0.0.1";
        String port = "9998";
        String name = "Client1";
        String filename = "test.txt";
        
        System.out.println("Client listening via UDP at: "+IP+":"+port+"("+name+")");
        System.out.println("File to be searched: "+filename);

        SenderUDP sender = new SenderUDP(IP, port, name, filename, LIST_OF_PEERS);
        sender.start();
    }
    public static String readFileAsString(String fileName)throws Exception{ 
        String data = ""; 
        data = new String(Files.readAllBytes(Paths.get(fileName))); 
        return data; 
    } 
    /*Return a JSON from a string if is valid and not an array*/
    public static JSONObject readJSON(String filepath) throws Exception {
        String fileContent  = readFileAsString(filepath);
        JSONTokener tokener = new JSONTokener(fileContent);
        JSONObject object = new JSONObject(tokener);
        
        return object;
    }
}
