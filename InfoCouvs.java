
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class InfoCouvs {
	String infoFilePath = "D:/polytechnique/Binets/JTX/2015-12-06 Ergometrix/testcsvcouvs.csv";
	List<String> repo_names;
	List<Boolean> to_encode;
	List<Boolean> to_synchronize;
	List<Boolean> is_encoded;
    String csvSplitBy = ",";
	InfoCouvs(){
		getInfo();
	}
	
	public void getInfo(){
		repo_names = new ArrayList<String>();
		to_encode = new ArrayList<Boolean>();
		to_synchronize = new ArrayList<Boolean>();
		is_encoded = new ArrayList<Boolean>();
		String line="";
		try {
			BufferedReader br = new BufferedReader(new FileReader(infoFilePath));
            while ((line = br.readLine()) != null) {
                String[] info_couv = line.split(csvSplitBy);
                repo_names.add(info_couv[0]);
                Boolean encode = info_couv[1].equals("1") ? true : false;
                to_encode.add(encode);
                Boolean synchro = info_couv[2].equals("1") ? true : false;
                to_synchronize.add(synchro);
                Boolean encoded = info_couv[3].equals("1") ? true : false;
                is_encoded.add(encoded);

            	System.out.println(encode+""+synchro+""+encoded);
            }

        } catch (Exception e) {
            System.out.println("Info file not found or corrupted");
        }
	}
	boolean valid(){
		if(to_encode.size()!=to_synchronize.size() || to_encode.size()!= repo_names.size())
			return false;

		for(String s:repo_names){
			if (s.equals(""))
				return false;
		}
		for(int i=0;i< to_encode.size();i++)
			if(!to_encode.get(i) && (to_synchronize.get(i) || is_encoded.get(i)))
				return false;
		return true;
	}
	
	void updateInfo(CouvList couvs){
		String line="";
		
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(infoFilePath));
			for(int i=0;i<couvs.couvertures.size();i++){
				char encode= couvs.couvertures.get(i).to_encode ? '1' : '0';
				char synchro= couvs.couvertures.get(i).to_synchronize ? '1' : '0';
				char encoded= couvs.couvertures.get(i).is_encoded ? '1' : '0';
				line=couvs.couvertures.get(i).repo_name + csvSplitBy + encode + csvSplitBy + synchro + csvSplitBy + encoded+"\n";
				br.write(line);
			}
			br.close();
		} catch (Exception e) {
            System.out.println("Info file not found");
        }
	}
}
