package hackzurich.picturesharingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import hackzurich.picturesharingapp.adapter.ShareAdapter;
import hackzurich.picturesharingapp.container.SharingItemContainer;
import hackzurich.picturesharingapp.helper.BitmapHelper;


public class SharingsActivity extends Activity {

    private ShareAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharings);

        adapter = new ShareAdapter(this);

        this.loadActiveSharings();
        this.loadRecentSharings();

        ListView lv_sharings = (ListView)this.findViewById(R.id.lv_sharings);
        lv_sharings.setAdapter(this.adapter);
    }


    private void loadActiveSharings() {
        this.adapter.addItem(new SharingItemContainer("Nord Amerika", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 4, R.drawable.img1, 12), "42"));
        this.adapter.addItem(new SharingItemContainer("Sonnenuntergang", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 4, R.drawable.img2, 12), "42"));
    }


    private void loadRecentSharings() {
        this.adapter.addItem(new SharingItemContainer("Fraänzen du alter hund!", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 4, R.drawable.img3, 12), "42"));
        this.adapter.addItem(new SharingItemContainer("Sharing is Caring!", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 4, R.drawable.img1, 12), "42"));
        this.adapter.addItem(new SharingItemContainer("Fraänzen du alter hund!", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 4, R.drawable.img3, 12), "42"));
    }


    public void btnPlay_OnClick(View v) {
        ImageButton btn = (ImageButton)v;

        if(String.valueOf(btn.getTag()) == "0") {
            btn.setTag(1);
            btn.setImageResource(R.drawable.pause_icon);
        } else {
            btn.setTag(0);
            btn.setImageResource(R.drawable.play_arrow);
        }
    }

    public void btnAdd_OnClick(View v) {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }
}
//281211