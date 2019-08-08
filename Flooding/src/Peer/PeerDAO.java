package Peer;

import static Peer.Main.readJSON;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;

/**
 * @author Jairo
 */
public class PeerDAO { 
    /*PeerDAO is implemented as a singleton*/
    public static PeerDAO INSTANCE;
    public static String IP;
    public static String port;
    public static String name;
    public static int MAX_TTL;
    
    /*Object containing the files of this Peer*/
    public HashMapFilesDAO myFileTable = new HashMapFilesDAO();
    /*Object containing the recent queries received by this Peer*/
    public HashMapQueriesDAO recentQueries = new HashMapQueriesDAO();
    /*Queue containing the TCP requests waiting for processing by this Peer*/
    public TCPRequestsQueueDAO requestQueue = new TCPRequestsQueueDAO();
    public static JSONArray LIST_OF_PEERS;
    
    public static PeerDAO getInstance(){
        if(INSTANCE == null){
            synchronized (PeerDAO.class){
                if(INSTANCE ==  null){
                    INSTANCE = new PeerDAO();
                    try { 
                        LIST_OF_PEERS = readJSON("C:\\Users\\Jairo\\codes\\ufabc-sd\\simple-flooding-protocol\\FloodingClient\\values\\listOfPeers.json").getJSONArray("peers");
                    } catch (Exception ex) {
                        Logger.getLogger(PeerDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
        return INSTANCE;
    }
      
}