import java.io.File;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Encoder {
	static String command_options = "-threads 0 -c:v libx264 -b:v 10M -r 25 -s 1920x1080 -x264opts level=4 -pix_fmt yuv420p -c:a aac -strict experimental -b:a 192k -y";
	static String[] extensions_rushs = {"mp4","MP4","MTS","mts","mov","MOV","mkv","MKV"};
	
	public static boolean encodeRepo(String path_repo){
		File folder = new File(path_repo);
		try{
		    for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		        	if(!encodeRepo(fileEntry.getAbsolutePath()))
		        		return false;
		        }
		        else{
		        	String path_input=fileEntry.getAbsolutePath();
		        	if(isEncodable(path_input)){
		        		System.out.println("1 encodable file : "+ path_input);
		        		String path_output = toEncodedMP4(path_input);
		        		encodeFile(path_input,path_output);
		        	}
		        	else
		        		System.out.println("1 non encodable file : "+path_input);
		        }
		        
		    }
		    return true;
		}
		catch(Exception e){
			System.out.println("Problem with : "+path_repo);
			return false;
		}
	}
	
	static boolean isEncodable(String path){
		//extension
		String[] s = path.split("\\.");
		System.out.println(s[1]+" "+encodableExtension(s[1]));
		return (s.length==2 && encodableExtension(s[1]) && !alreadyEncoded(s[0]));
	}
	
	static boolean encodableExtension(String ext){
		for(int i=0;i<extensions_rushs.length;i++)
			if(ext.equals(extensions_rushs[i]))
				return true;
		return false;
	}
	
	static boolean alreadyEncoded(String name){
		return name.length()>7 && name.substring(name.length() - 7).equals("_encode");
	}
	
	static String toEncodedMP4(String path){
		String name = path.split("\\.")[0];
		return name+"_encode.mp4";
	}
	
	static void encodeFile(String input, String output){ //yet to really make
		
		String command = "ffmpeg -i "+input+" "+command_options+" "+output;
		StringBuffer output_command = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";
			while ((line = reader.readLine())!= null) {
				output_command.append(line + "\n");
			}
			new File(input).delete();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Impossible to encode");
		}
		
	}
}
