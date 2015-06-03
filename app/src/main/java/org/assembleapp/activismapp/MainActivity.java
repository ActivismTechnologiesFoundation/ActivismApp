package org.assembleapp.activismapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    public static final String ZIPCODE = "ZIPCODE";
    public static final String CAUSES = "CAUSES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String zipcode = sharedPref.getString(getString(R.string.pref_location_key), "98034");
        ((EditText) findViewById(R.id.editText)).setText(zipcode);
        //TODO: set to pull in stored zip code value
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        View goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipcode = ((EditText) findViewById(R.id.editText)).getText().toString();
                if (((CheckBox) findViewById(R.id.save_data_checkbox)).isChecked()) {
                    saveZipcode(zipcode);
                }
//                Intent intent = new Intent(getApplication(), EventListActivity.class);
                Intent intent = new Intent(getApplication(), ChooseCauseActivity.class);
                intent.putExtra(ZIPCODE, zipcode);
                startActivity(intent);
            }
        });
        return true;
    }

    private void saveZipcode(String zipcode) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_location_key), zipcode);
        editor.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return launchActionSettings();
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean launchActionSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
}
