package com.example.dhruvil.parkmomo.ui;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.custom_components.CameraPreview;
import com.example.dhruvil.parkmomo.helpers.PrefUtils;
import com.example.dhruvil.parkmomo.model.ParkingList;
import com.google.android.gms.ads.AdView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class OfferScanActivity extends ActionBarActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private TextView tapText;
    ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private ParkingList parkingList;
    static {
        System.loadLibrary("iconv");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_scan);
        parkingList= PrefUtils.getSingleOffer(OfferScanActivity.this);
//        parkingList.CampaignLocations.
        tapText= (TextView) findViewById(R.id.tapText);
        tapText.setVisibility(View.GONE);
    }

    public void onPause() {

        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    tapText.setVisibility(View.VISIBLE);
                    if(PrefUtils.getSingleOffer(OfferScanActivity.this).qRcode.QRMetadata.equalsIgnoreCase(sym.getData().toString())){
                        Toast.makeText(OfferScanActivity.this,"valid",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OfferScanActivity.this,"invalid",Toast.LENGTH_LONG).show();
                    }


                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (barcodeScanned) {
                    tapText.setVisibility(View.GONE);
                    barcodeScanned = false;

                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
    }
    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        // Destroy the AdView.

        super.onDestroy();
    }
}
