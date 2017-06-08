package dd.com.myq.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;
import dd.com.myq.App.Config;
import dd.com.myq.R;
import dd.com.myq.Util.SessionManager;

import static dd.com.myq.Util.SessionManager.KEY_ABOUTME;
import static dd.com.myq.Util.SessionManager.KEY_DOB;
import static dd.com.myq.Util.SessionManager.KEY_EMAIL;
import static dd.com.myq.Util.SessionManager.KEY_UID;
import static dd.com.myq.Util.SessionManager.KEY_USERNAME;

public class EditProf extends AppCompatActivity {

    Button b1, b2;
    EditText e1, e2, e3, e4;
    String s, profilepicture,uid,a,b,d,e,first,c;
    RadioGroup rg;
    private ProgressDialog progress;
    private ImageButton save_profile;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addListenerRadioButton();

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        progress = new ProgressDialog(this);
        progress.setMessage("Updating Profile...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);


        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);
        e3 = (EditText) findViewById(R.id.editText3);
        e4 = (EditText) findViewById(R.id.editText7);



        Object name = user.get(KEY_USERNAME);
        e1.setText(name.toString());

        Object email = user.get(KEY_EMAIL);
        e2.setText(email.toString());

        final Object aboutme = user.get(KEY_ABOUTME);
        e3.setText(aboutme.toString());
        final Object dob = user.get(KEY_DOB);


        StringTokenizer tokens = new StringTokenizer(dob.toString(), "T");
        first = tokens.nextToken();
        e4.setText(first);

        final Object userid = user.get(KEY_UID);
        uid = userid.toString();
        profilepicture = null;


        save_profile = (ImageButton) findViewById(R.id.save_profile);
        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1 = (EditText) findViewById(R.id.editText);
                e2 = (EditText) findViewById(R.id.editText2);
                e3 = (EditText) findViewById(R.id.editText3);
                e4 = (EditText) findViewById(R.id.editText7);
                a = e1.getText().toString();
                b = e2.getText().toString();
                c = s;
                d = e3.getText().toString();
                e = e4.getText().toString();

                login(uid, b, a, e, c, d);
            }
        });
    }


    public void login(final String userid, String email, String username, String dob, String gender, String aboutme) {

        progress.show();

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userid);
        requestParams.put("emailaddress", email);
        requestParams.put("aboutme", aboutme);
        requestParams.put("username", username);
        requestParams.put("gender", gender);
        requestParams.put("dob", dob);


        client.post(this, Config.UpdateDetailAPIUrl, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                sessionManager.UpdateLoginSession(uid,a,b,d,e,c);

                progress.hide();
                Log.e("Response Login: ", response.toString());
                Toast.makeText(EditProf.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(EditProf.this, HomeActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progress.hide();
                Toast.makeText(EditProf.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progress.hide();
                Toast.makeText(EditProf.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {
                super.onFailure(statusCode, headers, throwable, responseString);
                progress.hide();
                Toast.makeText(EditProf.this, "Error Occurred", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addListenerRadioButton() {

        rg = (RadioGroup) findViewById(R.id.radioGroup);
        b1 = (Button) findViewById(R.id.radioButton);
        b2 = (Button) findViewById(R.id.radioButton2);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton:
                        s=b1.getText().toString();
                        break;
                    case R.id.radioButton2:
                        s=b2.getText().toString();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
