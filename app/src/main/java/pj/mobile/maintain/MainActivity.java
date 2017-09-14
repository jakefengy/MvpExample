package pj.mobile.maintain;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pj.mobile.maintain.example.view.view.Activity_Form;
import pj.mobile.maintain.example.view.view.Activity_Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toForm(View view) {
        startActivity(Activity_Form.getIntent(this));
    }

    public void toRealm(View view) {
        startActivity(Activity_Realm.getIntent(this));
    }

}
