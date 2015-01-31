package server;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by obama on 23/01/15.
 */
public class ElectricComponent implements Serializable{
	public List<Info> info = new ArrayList<>();
    public ElectricComponent( Info... info) {
    	for(Info f : info){
    		this.info.add(f);
    	}
    }
    public Info getInfo(String title){
    	for (Info f: info){
    		if(f.getTitle().equals(title)){
    			return f;
    		}
    			
    	}
    	return null;
    }
    public List<Info> getAllInfo(){
    	return info;
    }
}
