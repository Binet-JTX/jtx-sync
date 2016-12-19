import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CouvList {
	String pathCouvs = "D:/polytechnique/Binets/JTX/2015-12-06 Ergometrix/Rushs";
	List<File> repos;
	List<Couv> couvertures;
	
	CouvList(){
		readFiles();
	}
	void readFiles(){
		repos = listFilesFromFolder(new File(pathCouvs));
		couvertures = new ArrayList<Couv>();
		for(File f : repos){
			Couv c = new Couv(f);

	    	//System.out.println(c.valid());
			if (c.valid())
				couvertures.add(c);
		}
	}
	
	public static List<File> listFilesFromFolder(final File folder) {
		List<File> l = new ArrayList<File>();
		try{
		    for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
	        		l.add(fileEntry);
		        }
		    }
		}
		catch(Exception e){
			System.out.println("Folder not found");
		}
	    return l;
	}
	
	public void setInfo(InfoCouvs inf){
		if (inf.valid()){
			for(int i=0;i<inf.repo_names.size();i++){
				int j=searchIndexCouv(inf.repo_names.get(i));
				if(j>=0){
					couvertures.get(j).to_encode=inf.to_encode.get(i);
					couvertures.get(j).to_synchronize=inf.to_synchronize.get(i);
					couvertures.get(j).is_encoded=inf.is_encoded.get(i);
					System.out.println("sth to change here");
				}
			}
		}
	}
	
	public int searchIndexCouv(String name){
		
		for(int i=0;i<couvertures.size();i++){
			if(couvertures.get(i).repo_name.equals(name))
				return i;
		}
		return -1;
	}
	
	void encode(){
		for(Couv c:couvertures)
			if(c.to_encode && !c.is_encoded){
				
				c.is_encoded=Encoder.encodeRepo(c.repo_path);
			}
	}
	boolean somethingToEncode(){
		for(Couv c:couvertures)
			if(c.to_encode && !c.is_encoded){
				return true;
			}
		return false;
	}
}
