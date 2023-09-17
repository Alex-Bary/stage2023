package com.example.speedtestfaitmaison;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class uploadActivity extends AppCompatActivity {

    thread_speedtest uploadT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadT = new thread_speedtest();

        uploadT.start();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStopUpload:
                uploadT.stopThread();

                break;
        }
    }

    private GraphView graph = (GraphView) findViewById(R.id.graphUpload);



    private class thread_speedtest extends Thread {

        private volatile boolean isRunning = true;

        private static long uTime = 0;
        private static int sizeOfUpload;
        private static int sizeOfUploadPacket;
        private static int indexu = 0;

        private static int getRandomNumberInRange(int min, int max) {

            if (min >= max) {
                throw new IllegalArgumentException("max must be greater than min");
            }

            Random r = new Random();
            return r.nextInt((max - min) + 1) + min;
        }
        int rdm = new Random().nextInt(10000);


         String csvFilePathUpload = "upload_data" + rdm + ".csv";


         final FileWriter writerUpload;

        {
            try {
                writerUpload = new FileWriter(csvFilePathUpload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        private static int statusUpload = 0;

        GraphView graphView = findViewById(R.id.graphUpload); // Assuming you have a GraphView widget in your layout
        private int uploadTest(){


            SpeedTestSocket socketUpload = new SpeedTestSocket();
            ISpeedTestListener Iu = new ISpeedTestListener() {

                public void onCompletion(SpeedTestReport arg0) {

                    /** Pas utile
                     *
                     *
                    BigDecimal speed = arg0.getTransferRateBit();
                    BigDecimal speedMbps = speed.divide(new BigDecimal(1000000));

                    System.out.println("uplaod rate : " + speedMbps + " Mbps");
                    uTime.add(System.nanoTime());
                    int q = indexu/100;
                    float diff = uTime.get(uTime.size()-2) - uTime.get(0);
                    float Mbps = 100 * q * sizeOfUploadPacket/diff * 1000 * 8;
                    System.out.println("Nbr de tour : " + q);
                    System.out.println("uplaod rate calculé : " + Mbps + " Mbps");
                    statusUpload = 1;
                    */
                }

                public void onError(SpeedTestError arg0, String arg1) {
                    statusUpload = 2;
                    System.out.println("error : " + arg0 + " ; "+ arg1);

                }

                public void onProgress(float arg0, SpeedTestReport arg1) {


                     try {
                         long currentTime = System.nanoTime();
                         writerUpload.write(Float.toString(arg0));
                         writerUpload.write(",");
                         long mbps = (currentTime-uTime)/1000*sizeOfUploadPacket/1000000;
                         uTime = currentTime;
                         writerUpload.write(Long.toString(mbps));
                         writerUpload.write("\n"); // Add a new line after each entry
                         LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                 new DataPoint(uTime, mbps),
                         });
                         graphView.addSeries(series);
                     } catch (IOException e) {
                     e.printStackTrace();
                     }


                    /**
                    indexu++;
                    int step = 100;
                    if (indexu%step == 0) {
                        uTime.add(System.nanoTime());
                        System.out.println(arg0 + "%");
                    }
                     */
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

            while(true) {
                if (statusUpload != 0) {
                    return statusUpload;
                }
            }

        }

        public void run() {
            while (isRunning) {
                uploadTest();
                statusUpload = 0;
            }
            try {
                writerUpload.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void stopThread() {
            isRunning = false;
        }
    }
}

