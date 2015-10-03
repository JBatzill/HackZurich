package hackzurich.pictureshareapp.container;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Johannes on 10/3/2015.
 */
public class ShareElement implements IShare {
    private Bitmap _bmp;
    private String _title;
    private String _GUID;

    private ShareElement() {
    }

    public ShareElement(String title, Bitmap bmp, String GUID) {
        this();
        this._bmp = bmp;
        this._title = title;
        this._GUID = GUID;
    }


    public IShare.TYPE getType() {
        return IShare.TYPE.HEADER;
    }

    public Bitmap getImage(){
        return this._bmp;
    }
    public String getTitle() {
        return this._title;
    }
    public String getGUID() { return this._GUID; }
}
