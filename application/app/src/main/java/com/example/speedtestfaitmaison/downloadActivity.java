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

public class downloadActivity extends AppCompatActivity {

    thread_speedtest downloadT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        downloadT = new thread_speedtest();

        downloadT.start();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStopDownload:
                downloadT.stopThread();

                break;
        }
    }

    private GraphView graph = (GraphView) findViewById(R.id.graphUpload);



    private class thread_speedtest extends Thread {

        private volatile boolean isRunning = true;

        private static long dTime = 0;
        private static int sizeOfDownload;
        private static int sizeOfDownloadPacket;
        private static int indexd = 0;

        private static int getRandomNumberInRange(int min, int max) {

            if (min >= max) {
                throw new IllegalArgumentException("max must be greater than min");
            }

            Random r = new Random();
            return r.nextInt((max - min) + 1) + min;
        }
        int rdm = new Random().nextInt(10000);


        String csvFilePathUpload = "download_data" + rdm + ".csv";


        final FileWriter writerDownload;

        {
            try {
                writerDownload = new FileWriter(csvFilePathUpload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        private static int statusDownload = 0;

        GraphView graphView = findViewById(R.id.graphDownload); // Assuming you have a GraphView widget in your layout
        private int downloadTest(){


            SpeedTestSocket socketDownload = new SpeedTestSocket();
            ISpeedTestListener Id = new ISpeedTestListener() {

                public void onCompletion(SpeedTestReport arg0) {
                    /**
                    BigDecimal speed = arg0.getTransferRateBit();
                    BigDecimal speedMbps = speed.divide(new BigDecimal(1000000));
                    System.out.println("download rate : " + speedMbps + " Mbps");

                    float Mbps = 0;
                    for(int i = 0; i < DownloadRates.size(); i++) {
                        Mbps = Mbps + DownloadRates.get(i);
                    }
                    Mbps = Mbps/indexd*100;
                    System.out.println("download rate calculé : " + Mbps + " Mbps");
                     */
                }

                public void onError(SpeedTestError arg0, String arg1) {
                    System.out.println("error : " + arg0 + " ; "+ arg1);

                }

                public void onProgress(float arg0, SpeedTestReport arg1) {
                    indexd++;
                    sizeOfDownloadPacket = (int) (arg0/(100*indexd) * sizeOfDownload);
                    try {
                        long currentTime = System.nanoTime();
                        writerDownload.write(Float.toString(arg0));
                        writerDownload.write(",");
                        long mbps = (currentTime-dTime)/1000*sizeOfDownloadPacket/1000000;
                        dTime = currentTime;
                        writerDownload.write(Long.toString(mbps));
                        writerDownload.write("\n"); // Add a new line after each entry
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(dTime, mbps),
                        });
                        graphView.addSeries(series);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /**
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
                }


            };
            socketDownload.addSpeedTestListener(Id);
            dTime = System.nanoTime();
            sizeOfDownload = 1000000000;
            socketDownload.startDownload("http://speedtest.tele2.net/1GB.zip");


            while(true) {
                if (statusDownload != 0) {
                    return statusDownload;
                }
            }

        }

        public void run() {
            while (isRunning) {
                downloadTest();
                statusDownload = 0;
            }
            try {
                writerDownload.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void stopThread() {
            isRunning = false;
        }
    }
}