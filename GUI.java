import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GUI extends JFrame implements ActionListener{
	CouvList couvlist;
	InfoCouvs infocouvs;
    private JPanel mainPan = new JPanel(); 
    private JPanel checkPan = new JPanel();
    private JPanel buttonPan = new JPanel();
    private JPanel infoPan = new JPanel();
    private JButton quit = new JButton("Fermer");
    private JButton save = new JButton("Enregistrer");
    private JButton exec = new JButton("Encoder");
    private JButton sync = new JButton("Programmer une synchronisation");
    private JLabel synclabel = new JLabel("");

    Object[][] menu;
    String[]  title = {"Dossier", "Encoder", "Synchroniser"};
    TableauModel tabmodel;
    JTable tableau;
    private boolean to_sync;
	public GUI(){
		couvlist = new CouvList();
		infocouvs = new InfoCouvs();
		couvlist.setInfo(infocouvs);
		
		
		to_sync=false;
	    this.setTitle("JTX-sync");
	
	    this.setSize(800, 600);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    
	    mainPan.setLayout(new BorderLayout());
	    mainPan.add(buttonPan,BorderLayout.SOUTH);
	    mainPan.add(infoPan,BorderLayout.NORTH);
	    mainPan.add(checkPan,BorderLayout.CENTER);
	    
	    menu = new Object[couvlist.couvertures.size()][3];
	    tabmodel = new TableauModel(menu, title, this);
	    tableau = new JTable(tabmodel);
	    for(int i=0;i<couvlist.couvertures.size();i++){
	    	menu[i][0] = (couvlist.couvertures.get(i).repo_name);
	    	menu[i][1] = new Boolean(couvlist.couvertures.get(i).to_encode);
	    	menu[i][2] = new Boolean(couvlist.couvertures.get(i).to_synchronize);
	    	System.out.println("une couv");
	    }
	    tableau.setSize(500,300);
	    //tableau.getColumn("Encoder").setCellEditor(new CheckEditor(new JCheckBox()));
	    //tableau.getColumn("Synchroniser").setCellEditor(new CheckEditor(new JCheckBox()));
	    //checkPan.setSize(500,300);
	    checkPan.add(new JScrollPane(tableau));
	    exec.setEnabled(couvlist.somethingToEncode());
	    sync.setEnabled(false);
	    save.setEnabled(false);
	    buttonPan.add(save);
	    buttonPan.add(exec);
	    buttonPan.add(sync);
	    save.addActionListener(this);
	    sync.addActionListener(this);
	    quit.addActionListener(this);
	    exec.addActionListener(this);
	    buttonPan.add(quit);
	    infoPan.add(synclabel);
	    this.setContentPane(mainPan);
	    
	    this.setVisible(true);
	}
	
	 public void actionPerformed(ActionEvent arg0) {
		 if(arg0.getSource() == save){
			  updateCouvList();
		      infocouvs.updateInfo(couvlist);
		      exec.setEnabled(couvlist.somethingToEncode());
		      sync.setEnabled(true);
		      save.setEnabled(false);
		      to_sync=false;
		      
		 }
		 else if(arg0.getSource() == quit)
			 this.dispose();
		 else if(arg0.getSource() == exec){
			exec.setEnabled(false);
			couvlist.encode();
		 	infocouvs.updateInfo(couvlist);
		 	if(to_sync)
		 		setSync();
		 }
		 else if(arg0.getSource() == sync){
			 sync.setEnabled(false);
			 to_sync = true;
			 if(couvlist.somethingToEncode())
				 synclabel.setText("Vous devez encoder pour que la synchronisation ait lieu !");
			 else{
				 setSync();
			 }
			 
		 }
	  }

	 public void setSync(){
		 to_sync=false;
		 
		 synclabel.setText("La synchronisation aura lieu cette nuit !");
	 }
	 
	 public void hasChanged(){
		 sync.setEnabled(false);
		 exec.setEnabled(false);
		 save.setEnabled(true);
	 }
    public void updateCouvList(){
    	for(int i=0;i<couvlist.couvertures.size();i++){
    		couvlist.couvertures.get(i).to_encode = (boolean)menu[i][1];
    		couvlist.couvertures.get(i).to_synchronize = (boolean)menu[i][2];
    	}
    }

}
class TableauModel extends AbstractTableModel{
    private Object[][] data;
    private String[] title;
    private GUI parent;

    //Constructeur
    public TableauModel(Object[][] data, String[] title, GUI frame){
      this.data = data;
      this.title = title;
      this.parent = frame;
    }

    //Retourne le nombre de colonnes
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        if (col == 1 || col == 2) {
            return Boolean.class;
        }
        return super.getColumnClass(col);
    }

    @Override
    public int getColumnCount() {
        return title.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
    	if(col==2){
    		if((boolean)getValueAt(row,1) || !(boolean)value ){
    			data[row][col] = value;
    			parent.hasChanged();
    		}
    	}
    	else if(col==1){
    		data[row][col] = value;
    		if (!(boolean)value)
    			setValueAt(false,row,2);
			parent.hasChanged();
    	}
    	
        
    }
    
}
