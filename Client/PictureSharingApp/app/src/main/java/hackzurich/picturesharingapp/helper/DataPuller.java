package hackzurich.picturesharingapp.helper;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import hackzurich.picturesharingapp.network.GGWPAPI;

/**
 * Created by Johannes on 10/4/2015.
 */
public class DataPuller extends AsyncTask<Object, Object, Object>{
    @Override
    protected Object doInBackground(Object... params) {
        ArrayList<Integer> groups = GGWPAPI.getInstance().get_user_groups();

        for(int group : groups) {
            ArrayList<Integer> newPictures = GGWPAPI.getInstance().get_shares(group);

            for(int pic : newPictures) {
                Bitmap bmp = GGWPAPI.getInstance().get_photo(pic);

                saveBitmamp(bmp, String.valueOf(group) + "_" + String.valueOf(pic) + ".jpg");
            }
        }

        return null;
    }

    private void saveBitmap(Bitmap bmp, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
