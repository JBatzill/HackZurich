package hackzurich.picturesharingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        this.adapter.addItem(new SharingItemContainer("test1", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 8, R.drawable.img1, 12), "42"));
        this.adapter.addItem(new SharingItemContainer("test2", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 8, R.drawable.img2, 12), "42"));
    }


    private void loadRecentSharings() {
        this.adapter.addItem(new SharingItemContainer("Fraänzen du alter hund!", BitmapHelper.LOAD_ROUND_CORNER_IMAGE(this.getResources(), 8, R.drawable.img3, 12), "42"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sharings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
