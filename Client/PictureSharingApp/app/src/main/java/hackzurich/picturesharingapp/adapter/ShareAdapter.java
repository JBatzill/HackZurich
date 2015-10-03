package hackzurich.picturesharingapp.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hackzurich.picturesharingapp.R;
import hackzurich.picturesharingapp.container.SharingItemContainer;

public class ShareAdapter extends BaseAdapter {

    private ArrayList<SharingItemContainer> data = new ArrayList<SharingItemContainer>();

    private Context context;

    public ShareAdapter(Context context) {
        this.context = context;
    }

    public void addItem(final SharingItemContainer item) {
        data.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SharingItemContainer getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.context);
            //v = vi.inflate(R.layout.share_item, null);
        }

        SharingItemContainer p = getItem(position);
        if (p != null) {
            //TextView txt_title = (TextView)v.findViewById(R.id.txt_title);
            //ImageView img_preview = (ImageView)v.findViewById(R.id.img_preview);
            //txt_title.setText(((SharingItemContainer)p).getTitle());
            //img_preview.setImageBitmap(((SharingItemContainer)p).getImage());
        }
        return v;
    }
}