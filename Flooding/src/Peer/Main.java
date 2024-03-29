/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

import java.io.IOException;
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

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {
        
        PeerDAO DAO = PeerDAO.getInstance();
        
        PeerDAO.IP = "127.0.0.1";
        PeerDAO.port = "10001";
        PeerDAO.name = "Peer1";
        PeerDAO.MAX_TTL = 3;
        /*
        PeerDAO.IP = args[0];
        PeerDAO.port = args[1];
        PeerDAO.name = args[2];*/
        
        ListenerUDP listener = new ListenerUDP(PeerDAO.IP, PeerDAO.port);
        listener.start();
        System.out.println("Peer listening via UDP at: "+PeerDAO.IP+":"+PeerDAO.port+"("+PeerDAO.name+")");
        while(true){
            
        }
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
