/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;
import java.util.Date;
import java.util.LinkedList; 
import java.util.Queue; 
/**
 *
 * @author Jairo
 */
public class TCPRequestsQueueDAO {
    private Queue <Request> queue = new LinkedList<>();
    
    
    public void add(String IP, String port, String name, String fileName, String fullPath, Date senderTimestamp){
        Request request = new Request(IP, port, name, fileName, fullPath, senderTimestamp);
        queue.add(request);
    }
    public Request remove(){
        return queue.remove();
    }
    public int size(){
        return queue.size();
    }
}

class Request{
    private final String IP;
    private final String port;
    private final String name;
    private final String fileName;
    private final String fullPath;
    private final Date senderTimestamp;
    
    public Request(String IP, String port, String name, String fileName, String fullPath, Date senderTimestamp){
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.fileName = fileName;
        this.fullPath = fullPath;
        this.senderTimestamp = senderTimestamp;
    }
    
    /* GETTERS, SETTING NOT ALLOWED! */
    public String getIP(){
        return this.IP;
    }
    public String getPort(){
        return this.port;
    }
    public String getName(){
        return this.name;
    }
    public Date getSenderTimestamp(){
        return this.senderTimestamp;
    }
    public String getFileName(){
        return this.fileName;
    }    
    public String getFullPath(){
        return this.fullPath;
    }    
}
