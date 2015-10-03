package hackzurich.pictureshareapp.container;

/**
 * Created by Johannes on 10/3/2015.
 */
public class ShareGroupHeader implements IShare {
    private String _header;

    private ShareGroupHeader() {}

    public ShareGroupHeader(String header) {
        this._header = header;
    }

    public TYPE getType() {
        return TYPE.HEADER;
    }

    public String getHeader() {
        return this._header;
    }
}
