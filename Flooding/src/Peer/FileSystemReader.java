/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

/**
 *
 * @author Jairo
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemReader extends Thread{
    private final String IP;
    private final String port;
    private final String name;
    private final String fileSystem;
    private final int sleepTime;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    public FileSystemReader(String IP, String port, String name, String fileSystem, int sleepTime) throws IOException{
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.fileSystem = fileSystem;
        this.sleepTime = sleepTime;
    }
    //IP, port, name, fileName, fullPath, size
    public void run(){
        while(true){
            try {
                Thread.sleep(this.sleepTime);
                Stream<Path> local_files = Files.find(Paths.get(this.fileSystem),
                                    Integer.MAX_VALUE,
                                    (filePath, fileAttr) -> fileAttr.isRegularFile());

                local_files.forEach(filePath->{
                    try {
                        BasicFileAttributes fileAttr = Files.readAttributes(filePath, BasicFileAttributes.class);
                        createOrUpdateFile(this.IP, this.port, this.name, new Date(fileAttr.lastModifiedTime().toMillis()),
                                filePath.getFileName().toString(), filePath.toString(), (int) fileAttr.size());
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });                

            } catch (InterruptedException ex) {
                Logger.getLogger(FileSystemReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileSystemReader.class.getName()).log(Level.SEVERE, null, ex);
            }                 
        }
    }
    
    
    public void createOrUpdateFile(String IP, String port, String name, Date lastUpdateTimestamp, 
            String fileName, String fullPath, int size){
        String key = IP+port+name+fullPath;
        ArrayList<String> allKeys = PeerDAO.INSTANCE.myFileTable.getAllKeys();
        if(allKeys.contains(key)){
            PeerDAO.INSTANCE.myFileTable.update(key, lastUpdateTimestamp, size);
        }else{
            PeerDAO.INSTANCE.myFileTable.create(IP, port, name, fileName, fullPath, size);
        }
        
    }
    //PeerDAO.INSTANCE.myFileTable.update(this.key, this.filesMetadata);
    private ArrayList<String> updateMetadata(String fileSystem) throws IOException{
        Stream<Path> walk = Files.walk(Paths.get(fileSystem));
        ArrayList<String> result = new ArrayList();
        Date now = new Date();
        
        //Get files
        List<String> files = walk.filter(Files::isRegularFile)
                .map(x -> x.toString()).collect(Collectors.toList());
        //Get files metadata
        files.forEach((String file) -> {
            BasicFileAttributes attr;
            result.add(file);
        });
        //File(IP, port, name, Date lastUpdateTimestamp, fileName,fullPath, size)
        System.out.println("File system table updated");
        //System.out.println(result);
        
        return result;
    }
}

