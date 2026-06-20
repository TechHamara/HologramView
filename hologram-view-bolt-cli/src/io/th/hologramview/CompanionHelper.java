package io.th.hologramview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.google.appinventor.components.runtime.ComponentContainer;

public class CompanionHelper {
    private static final String TAG = "CompanionHelper";
    private final ComponentContainer container;

    public CompanionHelper(ComponentContainer container) {
        this.container = container;
    }

    /**
     * Loads an image from assets considering both regular app and companion mode
     * 
     * @param assetFileName Name of the asset file to load
     * @return Bitmap if successful, null if failed
     */
    public Bitmap loadImageFromAssets(String assetFileName) {
        if (assetFileName == null || assetFileName.isEmpty()) {
            Log.w(TAG, "loadImageFromAssets: empty filename");
            return null;
        }

        // First try regular assets
        try {
            InputStream is = container.$context().getAssets().open(assetFileName);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            is.close();
            if (bmp != null) {
                return bmp;
            }
        } catch (IOException ignored) {
            // Fall through to companion path
        }

        // If in companion mode, try companion paths
        if (isCompanion()) {
            String companionPath = getCompanionFilePath(assetFileName);
            try {
                File file = new File(companionPath);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    Bitmap bmp = BitmapFactory.decodeStream(fis);
                    fis.close();
                    if (bmp != null) {
                        return bmp;
                    }
                }
                Log.w(TAG, "loadImageFromAssets: companion file not found: " + companionPath);
            } catch (IOException e) {
                Log.w(TAG, "loadImageFromAssets: companion file error: " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * Detects if running in MIT App Inventor Companion or similar
     */
    private boolean isCompanion() {
        try {
            String packageName = container.$context().getPackageName();
            return packageName.contains("appinventor.ai_") ||
                    packageName.contains("aicompanion") ||
                    packageName.contains("io.kodular.companion") ||
                    packageName.contains("com.niotron.companion") ||
                    packageName.contains("com.appzard.companion") ||
                    packageName.contains("com.powerapps.companion") ||
                    packageName.contains("edu.mit.appinventor");
        } catch (Exception e) {
            Log.w(TAG, "isCompanion check failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the appropriate file path for companion mode assets based on Android
     * version
     */
    private String getCompanionFilePath(String fileName) {
        Context context = container.$context();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Android 10+ (API 29+) - use scoped storage
            File externalAssetsDir = context.getExternalFilesDir("assets");
            if (externalAssetsDir != null) {
                return externalAssetsDir.getAbsolutePath() + "/" + fileName;
            }
            // Fallback path for Android 10+
            return context.getExternalFilesDir(null).getAbsolutePath() +
                    "/assets/" + fileName;
        } else {
            // Legacy path for pre-Android 10
            return "/storage/emulated/0/AppInventor/assets/" + fileName;
        }
    }
}
