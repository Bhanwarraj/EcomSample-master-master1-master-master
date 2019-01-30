package com.allandroidprojects.ecomsample.utility;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.*;
import java.net.URL;

public class registrationtokenupload {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void openStream() throws IOException  {
        Log.d("Very","field");
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL("https://firebasestorage.googleapis.com/v0/b/ecommercenotify.appspot.com/o/token_id.csv?alt=media&token=66751a42-3378-4421-87c6-1c5cdb5c545a").openStream());
             FileOutputStream fileOS = new FileOutputStream("C:\\Users\\Bhanwar Raj\\Downloads\\Userinfo\\info.csv")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
        }
    }

        /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public static void main(String[] args) {
            StringBuffer stringBuffer = new StringBuffer();
            byte[] data ={};

            DecimalFormat decimalFormat = new DecimalFormat("###.###");

            File file = new File("C:\\Users\\Bhanwar Raj\\Downloads\\Userinfo");

            try {
                URL url = new URL("https://storage.googleapis.com/ecommercenotify.appspot.com/token_is.csv");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                System.out.println("Connected :)");

                InputStream inputStream = connection.getInputStream();

                long read_start = System.nanoTime();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                int i;

                while ((i = reader.read()) != -1) {
                    char c = (char) i;
                    if(c == '\n') {
                        stringBuffer.append("\n");
                    }else {
                        stringBuffer.append(String.valueOf(c));
                    }
                }

                reader.close();

                long read_end = System.nanoTime();

                System.out.println("Finished reading response in " + decimalFormat.format((read_end - read_start)/Math.pow(10,6)) + " milliseconds");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                data = stringBuffer.toString().getBytes();
            }

            try (FileOutputStream fop = new FileOutputStream(file)) {

                if (!file.exists()) {
                    file.createNewFile();
                }
                long now = System.nanoTime();


                System.out.println("Initializing write.....");

                fop.write(data);
                fop.flush();
                fop.close();

                System.out.println("Finished writing CSV in " + decimalFormat.format((System.nanoTime() - now)/Math.pow(10,6)) + " milliseconds!");


            } catch (IOException e) {
                e.printStackTrace();
            }

            System.gc();

        }
         */
    }
