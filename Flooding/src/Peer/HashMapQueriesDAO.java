/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jairo
 */
public class HashMapQueriesDAO {
    /* The object below uses as key the concatanation of IP, port and name of the peer
    The FileTable has the following fields: IP, PORT, NAME, LAST_UPDATE_TIMESTAMP,
    LIST_OF_FILES */
    private Map <String, Query> HashMap = Collections.synchronizedMap(new HashMap<String, Query>());
    
    /*DAO CRUD (Create, Read (get), Update, Delete)*/
    public void create(String IP, String port, String name, String fileName, int blackListStartCountdown){
        String key = (IP+port+fileName);
        Query query = new Query(IP, port, name, fileName, blackListStartCountdown);
        
        HashMap.put(key, query);
    }
    public Query get(String key){
        return HashMap.get(key);
    }
    
    public void update(String key){
        //IT'S ONLY ALLOWED TO DECREASE THE QUERY BLACKLIST COUNTDOWN
        Query currentQueryState = HashMap.get(key);
        Query newQueryState = currentQueryState;

        newQueryState.decreaseCountdown();
        
        HashMap.replace(key, newQueryState);
    }
    public void delete(String key){
        HashMap.remove(key);
    }
    public ArrayList<String> getAllKeys(){
        return new ArrayList(Arrays.asList(HashMap.keySet().toArray()));
    }
    public int size(){
        return HashMap.size();
    }
}

class Query{
    private final String IP;
    private final String port;
    private final String name;
    private final String fileName;
    private int blacklistCountdown;
    
    public Query(String IP, String port, String name, String fileName, int blackListStartCountdown){
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.fileName = fileName;
        this.blacklistCountdown = blackListStartCountdown;
    }
    
    /* GETTERS AND SETTERS */
    public String getIP(){
        return this.IP;
    }
    public String getPort(){
        return this.port;
    }
    public String getName(){
        return this.name;
    }
    public String getFileName(){
        return this.fileName;
    }    
    public int getBlacklistCount(){
        return this.blacklistCountdown;
    }
    public void decreaseCountdown(){
        this.blacklistCountdown = this.blacklistCountdown - 1;
    }    
}