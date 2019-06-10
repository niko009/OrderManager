package ordermanager.niko.com;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ordermanager.niko.com.utils.Constants.PROGRAMM_DIRECTORY_NAME;
import static ordermanager.niko.com.utils.Constants.PROGRAMM_IMAGES_NAME;


public class ImageLab {
    public static final String TAG = "ImageManagert";
    private final String ImageDirectoryName = "Image";
    private final Context mContext;

    private static ImageLab sImageLab;
    private Map<String, Drawable> iconDictionar;

    public ImageLab(Context mContext) {
        this.mContext = mContext;
    }

    public static ImageLab get(Context context) {
        if (sImageLab == null) {
            sImageLab = new ImageLab(context);
        }
        return sImageLab;
    }

    public void saveImage(String orderName, byte[] imageBuffer, String time) {
        if (imageBuffer == null)
            return;
        String pathFull = getImagesDirectory() + "/" + orderName;
        File dir = new File(pathFull);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            writeFile(imageBuffer, pathFull + "/" + time + ".jpeg");
        } catch (Exception ex) {

        }
    }


    public File getTmpFile(String orderName) {
        File fullPath = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (fullPath == null)
            return null;
        return new File(fullPath, orderName.replace(' ', '_') + ".jpg");
    }

    public File getFileById(String orderName) {
        String fullPath = getImagesDirectory() + "/" + orderName;

        if (!new File(fullPath).exists())
            return null;
        return new File(fullPath);
    }

    public void copyImage(File file, String name) {
        try {
            FileInputStream inStream = new FileInputStream(file);
            String datetime = DateFormat.format("yyyy_MM_dd_kk_mm", new Date()).toString();
            File directory = new File(getImagesDirectory() + "/" + name + "/");
            if (!directory.exists())
                directory.mkdirs();
            File destFile = new File(getImagesDirectory() + "/" + name + "/" + datetime + ".jpg");
            if (!destFile.exists())
                destFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(destFile);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error safe photo " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void copyImage(Uri fileUri, String name) {
        try {
            FileInputStream inStream = new FileInputStream(mContext.getContentResolver().openFileDescriptor(fileUri, "r").getFileDescriptor());
            String datetime = DateFormat.format("yyyy_MM_dd_kk_mm", new Date()).toString();
            File directory = new File(getImagesDirectory() + "/" + name + "/");
            if (!directory.exists())
                directory.mkdirs();
            File destFile = new File(getImagesDirectory() + "/" + name + "/" + datetime + ".jpg");
            if (!destFile.exists())
                destFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(destFile);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error safe photo " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public File[] getFilesById(String orderName) {
        String fullPath = getImagesDirectory() + "/" + orderName.replace(' ', '_');
        if (!new File(fullPath).exists())
            return new File[0];
        File folder = new File(fullPath);
        File[] res = folder.listFiles();

        List<File> list = Arrays.asList(res);
        Collections.reverse(list);
        return (File[]) list.toArray();
    }

    public String getImagesDirectory() {
        if (Environment.getExternalStorageState() != null) {
            File directory = new File(Environment.getExternalStorageDirectory() + PROGRAMM_DIRECTORY_NAME);

            if (!directory.exists()) {
                directory.mkdir();
            }

            File dirProfilers = new File(Environment.getExternalStorageDirectory() + PROGRAMM_DIRECTORY_NAME + PROGRAMM_IMAGES_NAME);
            if (!dirProfilers.exists())
                dirProfilers.mkdir();


            return dirProfilers.getAbsolutePath();
        } else {

            File dirProfilers = new File(Environment.getDataDirectory()
                    + PROGRAMM_DIRECTORY_NAME + PROGRAMM_IMAGES_NAME);
            if (!dirProfilers.exists())
                dirProfilers.mkdir();

            return dirProfilers.getAbsolutePath();
            // if phone DOES have sd card
        }
    }

    private void writeFile(byte[] data, String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
        try {
            File fcm = new File(fileName);
            if (fcm != null) {
                chmod(fcm, 1777);
            }
        } catch (Exception ex) {
        }
    }

    private void chmod(File path, int mode) throws Exception {
        try {
            String command = "chmod " + mode + " " + path.getAbsolutePath().toString();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
