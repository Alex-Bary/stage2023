import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class download {
	
	private static long uTime;
	private static long dTime;
	private static int sizeOfUpload;
	private static int sizeOfDownload;
	private static int sizeOfDownloadPacket;
	private static int sizeOfUploadPacket;
	private static int index = 0;
	private static int indexd = 0;
	
	public static void main(String[] args) throws IOException {
		
		SpeedTestSocket d = new SpeedTestSocket();
		
		System.out.println("beginning");
		
		int rdm = new Random().nextInt(10000);		
		
		
		String csvFilePathDownload = "download_data" + rdm + ".csv";
		final FileWriter writerDownload = new FileWriter(csvFilePathDownload);
		
		
		
		ISpeedTestListener Id = new ISpeedTestListener() {

			public void onCompletion(SpeedTestReport arg0) {
				BigDecimal speed = arg0.getTransferRateBit();
				BigDecimal speedMbps = speed.divide(new BigDecimal(1000000));
				System.out.println("download rate : " + speedMbps + " Mbps");
				
			}

			public void onError(SpeedTestError arg0, String arg1) {
				System.out.println("error : " + arg0 + " ; "+ arg1);
				
			}

			public void onProgress(float arg0, SpeedTestReport arg1) {
				/**
				indexd++;
				
				sizeOfDownloadPacket.add((int) (arg0/(100*indexd) * sizeOfDownload));
				
				
				int step = 100;
				if (indexd%step == 0) {
					int moyenne = 0;
					for(int i = sizeOfDownloadPacket.size() - step ; i<sizeOfDownloadPacket.size() - 1; i++) {
						moyenne = moyenne + sizeOfDownloadPacket.get(i);
					}
					moyenne = moyenne/step;
					
					System.out.println("Currente average sizeOfDownloadPacket : " + moyenne);
					System.out.println(arg0 + "%");
					dTime.add(System.nanoTime());
					int size = dTime.size();
					float diff = dTime.get(size-1) - dTime.get(size-2);
					float Mbps = step * moyenne/diff * 1000 * 8;
					DownloadRates.add(Mbps);
					System.out.println("Différence de temps : " + diff);
					System.out.println("uplaod rate calculé : " + Mbps + " Mbps");
				}
				*/
				
				 indexd++;
                 sizeOfDownloadPacket = (int) (arg0/(100*indexd) * sizeOfDownload);
                 try {
                     long currentTime = System.nanoTime();
                     writerDownload.write(Long.toString(currentTime));
                     writerDownload.write(",");
                     float diff = (float) (currentTime-dTime)/1000000;
                     long kbps = (long) (sizeOfDownloadPacket/diff);
                     dTime = currentTime;
                     writerDownload.write(Long.toString(kbps));
                     writerDownload.write("\n"); // Add a new line after each entry
                     System.out.println("time : " + currentTime + " kbps : " + kbps);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
			}
			
			
		};
		d.addSpeedTestListener(Id);
		dTime = System.nanoTime();
		sizeOfDownload = 1000000000;
		d.startDownload("http://speedtest.tele2.net/1GB.zip");
		
		
		//s.startDownload("http://speedtest.test.fr");
		

		
		
		System.out.println("finish");
	}

}
