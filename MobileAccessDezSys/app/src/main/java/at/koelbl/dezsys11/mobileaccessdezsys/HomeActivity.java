package at.koelbl.dezsys11.mobileaccessdezsys;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
/**
/**
 * Created by Alexander Koelbl on 21.04.2016.
 * Home Screen Activity
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Displays Home Screen
        setContentView(R.layout.home);
    }

}