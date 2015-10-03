package hackzurich.picturesharingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends Activity {

    public static void LOG_IN(Context context, String email, String password) {

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
