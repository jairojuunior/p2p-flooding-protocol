/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Jairo
 */
public class GarbageCollector extends Thread {
    private final long refreshTime;
    
    public GarbageCollector(long refreshTime){
        this.refreshTime = refreshTime;
    }
    
    @Override
    public void run() {
    	while (true) {
    		PeerDAO peerDAO = PeerDAO.getInstance();
    		ArrayList<String> keys = PeerDAO.INSTANCE.recentQueries.getAllKeys();
    		
    		keys.forEach(key->{
                    int countdown = PeerDAO.INSTANCE.recentQueries.get(key).getBlacklistCount();
                    if(countdown == 0){ //If countdown = 0 then remove query from blacklist
                        PeerDAO.INSTANCE.recentQueries.delete(key);
                    }else{
                        PeerDAO.INSTANCE.recentQueries.get(key).decreaseCountdown();
                    }
                });
                        
    		//Wait for next run
    		try {
    			TimeUnit.SECONDS.sleep(refreshTime);
    		} catch (InterruptedException e) {
    			System.out.println("Error ocurred while executing sleep method");
    		}
    	}
    }
}
