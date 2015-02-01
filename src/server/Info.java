package server;

public class Info{
	private final String title, info;

	public Info(String title, String info) {
		super();
		this.title = title;
		this.info = info;
	}

	public String getTitle() {
		return title;
	}

	public String getInfo() {
		return info;
	}
	public String toString(){
		return getTitle() + User.BETWEAN_TITLE_AND_INFO_SPACER + getInfo();
	}
	
	
}

///
