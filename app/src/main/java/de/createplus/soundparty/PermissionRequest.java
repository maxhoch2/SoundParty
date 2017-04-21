package de.createplus.soundparty;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.Random;

/**
 * Created by Max Nuglisch on 19.04.2017.
 */
public abstract class PermissionRequest {
    private String Permission;
    private int PermissionKey;
    private boolean explinationPopUp = false;
    private String explinationText = "";
    private String explinationTitle = "";

    /**
     * @param Permition The Android Permition to request
     */
    public PermissionRequest(String Permition) {
        this.Permission = Permition;
        this.PermissionKey = new Random().nextInt(500);
        explinationPopUp = false;
    }

    public PermissionRequest(String Permition, String explinationTitle, String explinationText) {
        this.Permission = Permition;
        this.PermissionKey = new Random().nextInt(500);
        this.explinationText = explinationText;
        this.explinationTitle = explinationTitle;
        explinationPopUp = true;
    }

    protected void request(Activity CurrentActivity) {
        if (explinationPopUp) {
            if (ContextCompat.checkSelfPermission(CurrentActivity, Permission) != PackageManager.PERMISSION_GRANTED) {

                final Activity CURRENTACTIVITY = CurrentActivity;
                AlertDialog.Builder builder = new AlertDialog.Builder(CURRENTACTIVITY);
                builder.setTitle(explinationTitle)
                        .setMessage(explinationText)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ActivityCompat.requestPermissions(CURRENTACTIVITY,
                                        new String[]{Permission},
                                        PermissionKey);

                            }
                        });
                builder.create().show();
            } else {
                onPermissionAccepted();
            }
        } else {
            if (ContextCompat.checkSelfPermission(CurrentActivity, Permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CurrentActivity,
                        new String[]{Permission},
                        PermissionKey);
            } else {
                onPermissionAccepted();
            }
        }
    }

    public abstract void onPermissionAccepted();

    public abstract void onPermissionDenied();

    public int getPermissionKey() {
        return PermissionKey;
    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }

    public void setPermissionKey(int permissionKey) {
        PermissionKey = permissionKey;
    }

    public void setExplinationText(String explinationText) {
        this.explinationText = explinationText;
        explinationPopUp = true;
    }

    public void disableExplinationPopUp() {
        explinationPopUp = false;
    }

    public void enableExplinationPopUp() {
        explinationPopUp = false;
    }
}