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

public class HashMapFilesDAO{
    /* The object below uses as key the concatanation of IP, port and name of the peer
    The FileTable has the following fields: IP, PORT, NAME, LAST_UPDATE_TIMESTAMP,
    LIST_OF_FILES */
    private Map <String, File> HashMap = Collections.synchronizedMap(new HashMap<String, File>());
    
    /*DAO CRUD (Create, Read (get), Update, Delete)*/
    public void create(String IP, String port, String name, String fileName, 
            String fullPath, int size){
        String key = (fileName);
        Date lastUpdateTimestamp = new Date();
        File filetable = new File(IP, port, name, lastUpdateTimestamp, fileName, fullPath, size);
        
        HashMap.put(key, filetable);
    }
    public File get(String key){
        return HashMap.get(key);
    }
    public void update(String key, Date lastUpdateTimestamp, int size){
        File currentFileState = HashMap.get(key);
        File newFileState = currentFileState;

        newFileState.setUpdateDate(lastUpdateTimestamp);
        newFileState.setSize(size);
        
        HashMap.replace(key, newFileState);
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

class File{
    private final String IP;
    private final String port;
    private final String name;
    private Date lastUpdateTimestamp;
    private String fileName;
    private String fullPath;
    private int size;
    
    public File(String IP, String port, String name, Date lastUpdateTimestamp, 
            String fileName, String fullPath, int size){
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.fileName = fileName;
        this.fullPath = fullPath;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.size = size;
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
    public Date getUpdateDate(){
        return this.lastUpdateTimestamp;
    }
    public void setUpdateDate(Date timestamp){
        this.lastUpdateTimestamp = timestamp;
    }
    public String getFileName(){
        return this.fileName;
    }    
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFullPath(){
        return this.fullPath;
    }    
    public void setFullPath(String fullPath){
        this.fullPath = fullPath;
    }
    public int getSize(){
        return this.size;
    }    
    public void setSize(int size){
        this.size = size;
    }
}