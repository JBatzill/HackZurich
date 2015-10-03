package hackzurich.picturesharingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }



    public void btnRegistr_OnClick(View v) {
        EditText txt_firstname = (EditText)this.findViewById(R.id.txt_register_firstname);
        EditText txt_lastname = (EditText)this.findViewById(R.id.txt_register_lastname);
        EditText txt_email = (EditText)this.findViewById(R.id.txt_register_email);
        EditText txt_password = (EditText)this.findViewById(R.id.txt_register_password);

        String firstname = txt_firstname.getText().toString();
        String lastname = txt_lastname.getText().toString();
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        this.registerUser(firstname, lastname, email, password);
    }

    private void registerUser(String firstname, String lastname, String email, String password) {
        GGWPAPI ggwp = GGWPAPI.getInstance();
        if (ggwp.register(firstname, lastname, email, password) != 0) {
            successfullRegistered(email, password);
        };
    }

    private void successfullRegistered(String email, String password) {
        LoginActivity.LOG_IN(this, email, password);
    }
}
