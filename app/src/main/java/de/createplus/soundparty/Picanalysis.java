package de.createplus.soundparty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

public class Picanalysis extends AsyncTask<String, Void, Boolean> {

    private Camera camera;
    private  byte[]data;
    private  MainActivity MainTHIS;

    public Picanalysis(byte[] data, Camera camera,MainActivity MainTHIS){
        this.data = data;
        this.camera = camera;
        this.MainTHIS = MainTHIS;
    }

    @Override
    protected Boolean doInBackground(String... lul) {
        long start = 0;
        try{
            start = System.currentTimeMillis();

            //Log.e("PARAMS",""+ camera.getParameters().getSupportedPictureSizes().toArray()[0]);
            Camera.Size previewSize = camera.getParameters().getPreviewSize();
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21,previewSize.width,previewSize.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0,0,previewSize.width,previewSize.height),80,baos);
            byte[] jdata = baos.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(jdata,0,jdata.length);
            int c = 0;
            int hue = 0;

            for(int x = 0; x < bitmap.getWidth(); x+=20){
                for(int y = 0; y < bitmap.getHeight(); y+=20){
                    int pixel =bitmap.getPixel(x,y);

                    int r = (int) ((Math.pow(256,3) + pixel) / 65536);
                    int g = (int) (((Math.pow(256,3) + pixel) / 256 ) % 256 );
                    int b = (int) ((Math.pow(256,3) + pixel) % 256);
                    hue += r+g+b;
                    c+=3;
                }
            }

            hue = hue / c;
            MainActivity.text_av = ""+hue ;

            if(hue > 100){

                MainActivity.text_b = "ON";
                //Log.e("Trigger","ON");
            }else{


                MainActivity.text_b = "OFF";
            }


        }catch (RuntimeException e){
            Log.e("ERR",e.getMessage());
        }

        MainActivity.text_t_conv = "CONV: "+ (System.currentTimeMillis() -start);
        return true;
    }

}