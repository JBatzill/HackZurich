package hackzurich.pictureshareapp.adapter;
import java.util.ArrayList;
import java.util.TreeSet;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hackzurich.pictureshareapp.R;
import hackzurich.pictureshareapp.container.IShare;
import hackzurich.pictureshareapp.container.ShareElement;
import hackzurich.pictureshareapp.container.ShareGroupHeader;

public class ShareAdapter extends BaseAdapter {

    private ArrayList<IShare> mData = new ArrayList<IShare>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private Context context;

    public ShareAdapter(Context context) {
        this.context = context;
    }

    public void addItem(final IShare item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final IShare item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (sectionHeader.contains(position) ? IShare.TYPE.HEADER : IShare.TYPE.SHARE).getValue();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public IShare getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.context);

            if (rowType == IShare.TYPE.HEADER.getValue()) {
                v = vi.inflate(R.layout.share_group_header, null);
            } else {
                v = vi.inflate(R.layout.share_item, null);
            }
        }

        IShare p = getItem(position);
        if (p != null) {
            if (rowType == IShare.TYPE.HEADER.getValue()) {
                TextView txt_header = (TextView)v.findViewById(R.id.txt_header);
                txt_header.setText(((ShareGroupHeader)p).getHeader());
            } else {
                TextView txt_title = (TextView)v.findViewById(R.id.txt_title);
                ImageView img_preview = (ImageView)v.findViewById(R.id.img_preview);
                txt_title.setText(((ShareElement)p).getTitle());
                img_preview.setImageBitmap(((ShareElement)p).getImage());
            }
        }

        v.setClipToOutline(true);

        return v;
    }
}