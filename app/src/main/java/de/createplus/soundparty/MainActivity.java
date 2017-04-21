package de.createplus.soundparty;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.wifi.WifiConfiguration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 1234;
    private Camera mCamera;
    private cameraPreview mPreview;
    public static MainActivity MainTHIS;
    public Button b;
    public Button av;
    public Button t_frame;
    public Button t_conv;
    public static String text_b = "";
    public static String text_av = "";
    public static String text_t_conv = "";
    public static String text_t_frame = "";

    public static long speed = 0;

    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button) findViewById(R.id.button_capture);
        av = (Button) findViewById(R.id.button_avarege);
        t_frame = (Button) findViewById(R.id.button_time_frames);
        t_conv = (Button) findViewById(R.id.button_time_convert);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_SETTINGS},
                    121);

        permissionManager = new PermissionManager();
        PermissionRequest request = new PermissionRequest(Manifest.permission.CAMERA,"Camera Sync","Um Camera Sync zu benutzen wird der Zugriff auf deine Kamera benötigt.") {
            @Override
            public void onPermissionAccepted() {
                //startCamSync();
            }

            @Override
            public void onPermissionDenied() {

            }
        };
        permissionManager.requestPermission(request, this);

        request = new PermissionRequest(Manifest.permission.CHANGE_WIFI_STATE,"WiFi Sync","No Comment.") {
            @Override
            public void onPermissionAccepted() {
                WifiSync();
            }

            @Override
            public void onPermissionDenied() {

            }
        };
        permissionManager.requestPermission(request,this);

        MainTHIS = this;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void startCamSync() {
        //TODO: checkCameraHardware popup
        // Create an instance of Camera
        mCamera = getCameraInstance();


        // Create our Preview view and set it as the content of our activity.
        mPreview = new cameraPreview(this, mCamera, this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        //preview.setVisibility(View.INVISIBLE);

        preview.addView(mPreview);
    }

    public static void updateButtons() {
        MainActivity.MainTHIS.b.setText(text_b);
        MainActivity.MainTHIS.av.setText(text_av);
        MainActivity.MainTHIS.t_conv.setText(text_t_conv);
        MainActivity.MainTHIS.t_frame.setText(text_t_frame);
    }

    public void WifiSync(){
        setWifiApState();
    }


    public void setWifiApState() {
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final ComponentName cn = new ComponentName(
                "com.android.settings",
                "com.android.settings.TetherSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( intent);
    }


}
