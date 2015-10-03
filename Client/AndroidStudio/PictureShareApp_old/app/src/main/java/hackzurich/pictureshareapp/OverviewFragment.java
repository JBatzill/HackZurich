package hackzurich.pictureshareapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import hackzurich.pictureshareapp.adapter.ShareAdapter;
import hackzurich.pictureshareapp.container.ShareElement;
import hackzurich.pictureshareapp.container.ShareGroupHeader;
import hackzurich.pictureshareapp.helper.BitmapHelper;

/**
 * Created by Johannes on 10/3/2015.
 */
public class OverviewFragment extends Fragment {


    private ShareAdapter adapter;

    public OverviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        adapter = new ShareAdapter(this.getActivity());

        this.loadActiveSharings();
        this.loadRecentSharings();

        ListView lv_sharings = (ListView)rootView.findViewById(R.id.lv_sharings);
        lv_sharings.setAdapter(this.adapter);

        return rootView;
    }


    private void loadActiveSharings() {

        this.adapter.addSectionHeaderItem(new ShareGroupHeader("Active"));
        this.adapter.addItem(new ShareElement("test1", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(getActivity().getResources(), 8, R.drawable.img1, 12), "42"));
        this.adapter.addItem(new ShareElement("test2", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(getActivity().getResources(), 8, R.drawable.img2, 12), "42"));
    }


    private void loadRecentSharings() {
        this.adapter.addSectionHeaderItem(new ShareGroupHeader("Recent"));
        this.adapter.addItem(new ShareElement("Fraänzen du alter hund!", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(getActivity().getResources(), 8, R.drawable.img3, 12), "42"));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((OverviewActivity) activity).onSectionAttached(1);
    }
}
