/**
 * 
 */
/**
 * @author alex
 *
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.io. * ;
import java.util.Scanner;



public class csv_modif {
	
	private int colomn_max;
	private int index_max = 15000;
	private List<String[]> lines;
	public static List<String[]> listModif;
	
	private void readCsvUpload(String csvFile) {
		try{
			colomn_max = 2;
			Scanner sc = new Scanner(new File(csvFile));
			//parsing a CSV file into the constructor of Scanner class 
			sc.useDelimiter(",");
			//setting comma as delimiter pattern
			lines = new ArrayList<String[]>();
			int index = 0;
			String time = "";
			while ((sc.hasNext()) & (index < index_max)) {
				String[] line = null;
				line = new String[colomn_max];
				if (index == 0) {
					time = sc.next();
				} else {
					String[] linebis = new String[colomn_max];
					
					linebis = sc.next().split("\n");
					
					line[0] = time;
					line[1] = linebis[0];
					time = linebis[1];
				}
				if (index != 0) {
					lines.add(line);
				}
				index++;
			}
			sc.close();
			//closes the scanner  
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	}
	
	private void readCsvDownload(String csvFile) {
		try{
			colomn_max = 2;
			Scanner sc = new Scanner(new File(csvFile));
			//parsing a CSV file into the constructor of Scanner class 
			sc.useDelimiter(",");
			//setting comma as delimiter pattern
			lines = new ArrayList<String[]>();
			int index = 0;
			String time = "";
			while ((sc.hasNext()) & (index < index_max)) {
				String[] line = null;
				line = new String[colomn_max];
				if (index == 0) {
					time = sc.next();
				} else {
					String[] linebis = new String[colomn_max];
					
					linebis = sc.next().split("\n");
					
					line[0] = time;
					line[1] = linebis[0];
					time = linebis[1];
				}
				if (index != 0) {
					lines.add(line);
				}
				index++;
			}
			sc.close();
			//closes the scanner  
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	}
	
	
	/** Afficher la liste brute
	 * 
	 */
	private void ShowListBrute(){
		
		for(String[] line : lines) {
			
			
			for (String cell : line) {
                System.out.print(cell + ", ");
            }
           
            System.out.println();
            System.out.println();
            System.out.println();
		}
	}
	
	/** Afficher la liste triée
	 * 
	 */
	private void ShowList(){
		
		for(String[] line : listModif) {
			for (String cell : line) {
                System.out.print(cell + ", ");
            }
            System.out.println();
            System.out.println();
            System.out.println();
		}
	}
	
	/** Modifier le csv 
	 * 
	 */
	private void modif_csv_upload() {
		
		int index = 0;
		long time = 0;
		long currentTime;
		
		
		
		for(String[] line : lines) {
			
			if (index == 0) {
				time = Long.parseLong(line[0]);
			} else {
				currentTime = Long.parseLong(line[0]);
				
				long diff = currentTime - time;
				
				String[] newline = null;
				newline = new String[colomn_max];
				
				newline[0] = Long.toString(currentTime);
				long kbps = 1000000000* Long.parseLong(line[1])/diff;
				System.out.println(kbps);
				kbps = 1000000000*kbps/diff;
				
				System.out.println(kbps);
				System.out.println();
				
				newline[1] = Long.toString(kbps);
				
				time = currentTime;
			}
            
           
           
			index++;
		}
		
		
	}
	
	
	private void modif_csv_download() {
		
		int index = 0;
		long time = 0;
		long currentTime;
		int centM = 100000000;
		int kbps = 0;
		listModif = new ArrayList<String[]>();
		
		
		for(String[] line : lines) {
			
			if (index == 0) {
				time = Long.parseLong(line[0]);
				kbps = Integer.parseInt(line[1]);
				index++;
			} else {
				currentTime = Long.parseLong(line[0]);
				
				if (currentTime/centM - time/centM == 0) {
					kbps = kbps + Integer.parseInt(line[1]);
					index++;
				} else {
					kbps = kbps/index;
					
					String[] newline = null;
					newline = new String[3];
					
					newline[0] = Long.toString(time/centM);
					newline[1] = Integer.toString(kbps);
					newline[2] = Integer.toString(index);
					listModif.add(newline);
					
					time = Long.parseLong(line[0]);
					kbps = Integer.parseInt(line[1]);
					index = 1;
				}
			}
            
           
           
		}
		
		
	}
	
	private List<String[]> getListModif() {
		return listModif;
	}

	 /* faire la commande ls dans le terminal
     * 
     */
    private static void cmd_ls(String repertoire) {
    	try
        {
            String lscmd = "ls " + repertoire; //../../Desktop";
            Runtime.getRuntime().exec(lscmd);
            Process p=Runtime.getRuntime().exec(lscmd);
            p.waitFor();
            BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line=reader.readLine();
            if (line == null) {
            	System.out.println("empty");
            } else if (line == "") {
            	System.out.println("\"\"");
            } else {
	            while(line!=null)
	            {
	                System.out.println(line);
	                line=reader.readLine();
	            }
            }
        }
        catch(IOException e1) {
            System.out.println("Pblm found1.");
        }
        catch(InterruptedException e2) {
            System.out.println("Pblm found2.");
        }

        System.out.println("ls ok.");
    }
    
    
    public static void main(String[] args) throws IOException {
    	
    	String repertoire = ".";
    	
    	
    	cmd_ls(repertoire);
    	
    	String csvFile = "upload_data6071.csv";
    	
    	//la modif
    	csv_modif modif= new csv_modif();
    	
    	modif.readCsvDownload(csvFile);
    	
    	modif.modif_csv_download();
    	
    	//modif.ShowList();
    	
    	
    	String newCsvFile = "upload_modif2.csv";
    	
    	FileWriter writerDownload = new FileWriter(newCsvFile);
    	
    	List<String[]> L = modif.getListModif();
    	
    	
    	for(String[] line : L) {
    		try {
                String currentTime = line[0];
                String kbps = line[1];
                
                System.out.print(currentTime + ", ");
                System.out.print(kbps + ", ");
                System.out.print(line[2] + "\n ");
                
                writerDownload.write(currentTime);
                writerDownload.write(",");
               
                writerDownload.write(kbps);
                writerDownload.write("\n"); // Add a new line after each entry
               
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
    	
    	writerDownload.close();
    	
    }
	
}