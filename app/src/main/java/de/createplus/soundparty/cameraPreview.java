package de.createplus.soundparty;

/**
 * Created by maxso on 18.04.2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/** A basic Camera preview class */
public class cameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private MainActivity MainTHIS;
    public byte[] buffer;

    public cameraPreview(Context context, Camera camera, MainActivity THIS) {
        super(context);
        MainTHIS = THIS;
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters params = mCamera.getParameters();
            Log.e("PARAMS",""+ Arrays.toString(params.getSupportedSceneModes().toArray()));
            params.setSceneMode("manual");
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            Log.e("PARAMS","RESULUTION: "+ sizes.get(sizes.size()-1).height + ":"+sizes.get(sizes.size()-1).width);
            params.setPictureSize(sizes.get(sizes.size()-1).width,sizes.get(sizes.size()-1).height);
            params.setPreviewSize(sizes.get(sizes.size()-1).width,sizes.get(sizes.size()-1).height);
            params.setPreviewFpsRange( 30000, 30000 ); // for 30 fps
            if ( params.isAutoExposureLockSupported() )
                params.setAutoExposureLock( true );
            //params.set("iso", 800);
            mCamera.setParameters(params);
            mCamera.startPreview();


            //params.set("mode", "m");

            //params.set("aperture", "28"); //can be 28 32 35 40 45 50 56 63 71 80 on default zoom

            //params.set("shutter-speed", 9); // depends on camera, eg. 1 means longest

        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
        buffer=new byte[460800];
        mCamera.addCallbackBuffer(buffer);
        mCamera.setPreviewCallbackWithBuffer(createPreviewCallback());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            //mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

        mCamera.setPreviewCallback(createPreviewCallback());
    }

    public Camera.PreviewCallback createPreviewCallback() {
        Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                new Picanalysis(data,camera,MainTHIS).execute();
                MainActivity.text_t_frame = "FRAME: "+ (System.currentTimeMillis()-MainActivity.speed);
                MainActivity.updateButtons();
                //Log.e("TIMES","MINE: "+ (System.currentTimeMillis() -start)+ "  CAM: "+ (System.currentTimeMillis()-MainActivity.speed));

                MainActivity.speed = System.currentTimeMillis();


            }
        };

        return previewCallback;
    }


}