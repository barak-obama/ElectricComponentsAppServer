package server;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by obama on 23/01/15.
 */
public class ElectricComponent {
	public List<Info> info = new ArrayList<>();
    public ElectricComponent( Info... info) {
    	for(Info f : info){
    		this.info.add(f);
    	}
    }

    public List<Info> getAllInfo(){
    	return info;
    }
    
    public Info getInfoByTitle(String title){
    	for (Info inf: info) {
			if(inf.getTitle().equals(title)){
				return inf;
			}
		}
    	return null;
    }
}
