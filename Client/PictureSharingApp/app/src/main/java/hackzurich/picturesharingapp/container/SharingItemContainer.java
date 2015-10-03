package hackzurich.picturesharingapp.container;

import android.graphics.Bitmap;

/**
 * Created by Johannes on 10/3/2015.
 */
public class SharingItemContainer {
    private Bitmap _bmp;
    private String _title;
    private String _GUID;

    private SharingItemContainer() {
    }

    public SharingItemContainer(String title, Bitmap bmp, String GUID) {
        this();
        this._bmp = bmp;
        this._title = title;
        this._GUID = GUID;
    }

    public Bitmap getImage(){
        return this._bmp;
    }
    public String getTitle() {
        return this._title;
    }
    public String getGUID() { return this._GUID; }
}
