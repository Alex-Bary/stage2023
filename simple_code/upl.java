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

public class upl {
	
	private static long uTime;
	private static int sizeOfUpload;

	private static long sizeOfUploadPacket;
	private static int indexu = 0;
	
	public static void main(String[] args) throws IOException {
		
		SpeedTestSocket d = new SpeedTestSocket();
		SpeedTestSocket socketUpload = new SpeedTestSocket();
		
		System.out.println("beginning");
		
		int rdm = new Random().nextInt(10000);

		
		

        String csvFilePathUpload = "upload_data" + rdm + ".csv";
		final FileWriter writerUpload = new FileWriter(csvFilePathUpload);
		
		
		ISpeedTestListener Iu = new ISpeedTestListener() {

			public void onCompletion(SpeedTestReport arg0) {
				BigDecimal speed = arg0.getTransferRateBit();
				BigDecimal speedMbps = speed.divide(new BigDecimal(1000000));
				
				System.out.println("uplaod rate : " + speedMbps + " Mbps");
				
			}

			public void onError(SpeedTestError arg0, String arg1) {
				
				System.out.println("error : " + arg0 + " ; "+ arg1);
				
			}

			public void onProgress(float arg0, SpeedTestReport arg1) {
				/**
				System.out.println(arg0 + "%");
				try {
					long currentTime = System.nanoTime();
					writerUpload.write(Float.toString(arg0));
					writerUpload.write(",");
					long mbps = (currentTime-uTime)/1000*sizeOfUploadPacket/1000000;
					uTime = currentTime;
					System.out.println("Mbps : " + mbps);
					writerUpload.write(Long.toString(mbps));
					writerUpload.write("\n"); // Add a new line after each entry
	            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		       */
				/**
				index++;
				int step = 100;
				if (index%step == 0) {
					uTime.add(System.nanoTime());
					System.out.println(arg0 + "%");
				}
				*/
				
				indexu++;
				//non nécessaire
				//sizeOfUploadPacket = (long) (arg0/(100*indexu) * sizeOfUpload);
				//System.out.println("sizeOfUploadPacket : " + sizeOfUploadPacket);
				
				try {
                    long currentTime = System.nanoTime();
                    writerUpload.write(Long.toString(currentTime));
                    writerUpload.write(",");
                    
                    long kbps = (long) 1000000*sizeOfUploadPacket/(currentTime-uTime);
                    uTime = currentTime;
                    writerUpload.write(Long.toString(kbps));
                    writerUpload.write("\n"); // Add a new line after each entry
                    System.out.println("time : " + currentTime + " kbps : " + kbps);
                } catch (IOException e) {
                e.printStackTrace();
                }
			}
			
			
		};
		List<ISpeedTestListener> L;
		L = new ArrayList<>();
		L.add(Iu);
		sizeOfUploadPacket = socketUpload.getUploadChunkSize();
		System.out.println("sizeOfUploadPacket : " + sizeOfUploadPacket);
		uTime = System.nanoTime();  //nano seconde (10^-9)
		System.out.println("Start time : " + uTime + "ns");
		upload u = new upload(socketUpload , L);
		sizeOfUpload = 1000 * 1000000;
		
		//u.startUploadRequest("http://localhost:8080/uploadServer/uploader.html", sizeOfUpload);
		
		u.startUploadRequest("https://ps.uci.edu/~franklin/doc/file_upload.html", sizeOfUpload);
		
		

		
		
		System.out.println("finish");
	}

}
