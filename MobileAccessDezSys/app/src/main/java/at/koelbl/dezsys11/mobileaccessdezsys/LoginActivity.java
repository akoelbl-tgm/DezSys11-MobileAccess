package at.koelbl.dezsys11.mobileaccessdezsys;

import android.support.v7.app.AppCompatActivity;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import cz.msebera.android.httpclient.Header;
import java.io.UnsupportedEncodingException;

/**
 * Created by Alexander Koelbl on 21.04.2016.
 * Login Activity Class
 */
public class LoginActivity extends AppCompatActivity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.login_error);
        // Find Email Edit View control by ID
        emailET = (EditText)findViewById(R.id.loginEmail);
        // Find Password Edit View control by ID
        pwdET = (EditText)findViewById(R.id.loginPassword);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Login button is clicked
     * @param view
     */
    public void loginUser(View view){
        // Get Email Edit View Value
        String email = emailET.getText().toString();
        // Get Password Edit View Value
        String password = pwdET.getText().toString();
        // Instantiate Http Request Param Object
        JSONObject params = new JSONObject();
        // When Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(email) && Utility.isNotNull(password)){
            // When Email entered is Valid
            if(Utility.validate(email)){
                try {
                    // Put Http parameter username with value of Email Edit View control
                    params.put("email", email);
                    // Put Http parameter password with value of Password Edit Value control
                    params.put("password", password);
                    // Invoke RESTful Web Service with Http parameters
                    invokeWS(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter an valid email", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Don't leave any fields blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     * @param params
     */
    public void invokeWS(JSONObject params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity request = null;
        try {
            request = new StringEntity(params.toString());
            request.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(this.getApplicationContext(), "http://10.0.2.2:8080/login", request, "application/json", new TextHttpResponseHandler() {

            /**
             * Method, when the response has the http response code 200
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                prgDialog.hide();
                if(statusCode == 200) {
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();

                    // Navigate to Home screen
                    navigatetoHomeActivity(responseBody);
                }

            }

            /**
             * Method, when the response has not the http response code 200
             */
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                prgDialog.hide();
                // When the response has the http response code 403
                if(statusCode == 403){
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                }
                // When Http response code is other than 403
                else{
                    Toast.makeText(getApplicationContext(), "Error occcured!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoHomeActivity(String response){
        Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    /**
     * Method gets triggered when Register button is clicked
     * @param view
     */
    public void navigatetoRegisterActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

}
