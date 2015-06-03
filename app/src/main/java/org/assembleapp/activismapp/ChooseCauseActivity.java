package org.assembleapp.activismapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class ChooseCauseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cause);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CauseListFragment())
                    .commit();
        }

        Button goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String causes = generateCausesString();
                if (((CheckBox) findViewById(R.id.save_data_checkbox)).isChecked()) {
                    saveCausesToSettings(causes);
                }
                Intent intent = new Intent(getApplication(), EventListActivity.class);
                intent.putExtra(MainActivity.ZIPCODE,
                        getIntent().getStringExtra(MainActivity.ZIPCODE));
                intent.putExtra(MainActivity.CAUSES, causes);
                startActivity(intent);
            }
        });
    }

    private void saveCausesToSettings(String causes) {
    }

    private String generateCausesString() {
        return "";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_cause, menu);
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
