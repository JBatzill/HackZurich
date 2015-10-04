package hackzurich.picturesharingapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import hackzurich.picturesharingapp.network.GGWPAPI;


public class CreateGroupActivity extends Activity {


    private ArrayList<String> array = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        TextView txt_date_start = (TextView) findViewById(R.id.txt_date_start);
        TextView txt_date_end = (TextView) findViewById(R.id.txt_date_end);

        txt_date_start.setText("0");
        txt_date_end.setText("1");

        adapter = new ArrayAdapter<String>(this, R.layout.basic_list_view_item, array);

        ListView lv = (ListView)findViewById(R.id.lv_user);
        lv.setAdapter(adapter);
    }

    public void btnDate_OnClick(View v) {
        final TextView txt = (TextView) v;

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.time_title);


        // Set up the input
        final EditText input = new EditText(this);
        input.setText(txt.getText());
        input.setTextColor(getResources().getColor(R.color.textColorSecondary));
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txt.setText(input.getText());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void btnAddUser_OnClick(View v) {
        EditText txt_user = (EditText)findViewById(R.id.txt_user);
        String user = txt_user.getText().toString();

        if(user != null && user.length() > 5) {
            array.add(user);
            adapter.notifyDataSetChanged();
            txt_user.setText("");
        }
    }

    public void btnAdd_OnClick(View v) {
        EditText txt_name = (EditText)findViewById(R.id.txt_user);
        TextView tv_start = (TextView)findViewById(R.id.txt_date_start);
        TextView tv_end = (TextView)findViewById(R.id.txt_date_end);

        createNewGroup(txt_name.getText().toString(), Integer.valueOf(tv_start.getText().toString()), Integer.valueOf(tv_end.getText().toString()), array);
    }

    private void createNewGroup(final String name, final int start, final int end, final ArrayList<String> users) {
        new AsyncTask<Object, Object, Boolean>() {

            @Override
            protected Boolean doInBackground(Object... params) {
                try {
                    GGWPAPI ggwp = GGWPAPI.getInstance();
                    int id = ggwp.add_group(name);
                    for(String user : users) {
                        ggwp.add_group_member(id, user);
                    }
                    return true;
                } catch (Exception ex) {
                    Log.d("GGWPAPI", ex.getMessage());
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean successful) {
                onBackPressed();
            }
        }.execute();
    }

}