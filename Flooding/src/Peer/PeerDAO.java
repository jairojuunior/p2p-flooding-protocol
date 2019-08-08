package Peer;

import java.util.ArrayList;

/**
 * @author Jairo
 */
public class PeerDAO { 
    /*PeerDAO is implemented as a singleton*/
    public static PeerDAO INSTANCE;
    public static ArrayList<ArrayList<String>> LIST_OF_PEERS;
    
    
    /*Object containing the files of this Peer*/
    public HashMapFilesDAO myFileTable = new HashMapFilesDAO();
    /*Object containing the recent queries received by this Peer*/
    public HashMapQueriesDAO recentQueries = new HashMapQueriesDAO();
    /*Queue containing the TCP requests waiting for processing by this Peer*/
    public TCPRequestsQueueDAO requestQueue = new TCPRequestsQueueDAO();
    
    
    public static PeerDAO getInstance(){
        if(INSTANCE == null){
            synchronized (PeerDAO.class){
                if(INSTANCE ==  null){
                    INSTANCE = new PeerDAO();
                    LIST_OF_PEERS = new ArrayList();
                }
            }

        }
        return INSTANCE;
    }
      
}