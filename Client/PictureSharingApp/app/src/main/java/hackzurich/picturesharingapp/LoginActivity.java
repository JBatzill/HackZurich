package hackzurich.picturesharingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import hackzurich.picturesharingapp.network.GGWPAPI;


public class LoginActivity extends Activity {

    public static void LOG_IN(final Context context, final String email, final String password) {
        new AsyncTask<Object, Object, Boolean>() {

            @Override
            protected Boolean doInBackground(Object... params) {
                try {
                    GGWPAPI ggwp = GGWPAPI.getInstance();
                    ggwp.login(email, password);
                    return true;
                } catch (Exception ex) {
                    Log.d("GGWPAPI", ex.getMessage());
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean successful) {
                if(successful) {
                    Intent intent = new Intent(context, RegisterActivity.class);
                    context.startActivity(intent);
                }
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnRegistr_OnClick(View v) {
        EditText txt_email = (EditText)this.findViewById(R.id.txt_login_email);
        EditText txt_password = (EditText)this.findViewById(R.id.txt_login_password);

        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        LOG_IN(this, email, password);
    }


    public void txtRegister_OnClick(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
