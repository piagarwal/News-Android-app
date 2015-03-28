package com.example.pi.news;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText newsedittext;
    Button newsbutton;
    String location;
    public final static String LOCATION = "com.example.pi.news";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsedittext = (EditText)findViewById(R.id.newseditText);
        newsbutton = (Button)findViewById(R.id.newsbutton);
        newsbutton.setOnClickListener(getnews);

        newsedittext.requestFocus();
    }

       public View.OnClickListener getnews = new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              if(newsedittext.getText().length() > 0){
                location = newsedittext.getText().toString();

                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra(LOCATION,location);
                startActivity(intent);


              }else{
                  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                  builder.setTitle(R.string.invalid_text);
                  builder.setPositiveButton("ok", null);
                  AlertDialog theAlertDialog = builder.create();
                  theAlertDialog.show();
              }
           }
       };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
