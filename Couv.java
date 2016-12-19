import java.util.Date;
import java.io.File;
import java.text.SimpleDateFormat;
public class Couv {
	String repo_path;
	String repo_name;
	Date date;
	String event;
	boolean to_encode;
	boolean to_synchronize;
	boolean is_encoded;
	boolean is_synchronized;
	
	Couv(File rep){
		repo_path = rep.getAbsolutePath();
		repo_name = rep.getName();
		date = parseDate(repo_name);
		event = parseEvent(repo_name);
	}
	
	public static Date parseDate(String s){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		try{

	    	//System.out.println(format.parse(s.substring(0,10)));
			return format.parse(s.substring(0,10));
		}
		catch(Exception e){
			//System.out.println(s);
			return null;
		}
	}
	public static String parseEvent(String s){
		return s.replace('_', ' ');
	}
	
	public boolean valid(){
		return date != null && event != "";
	}
}
