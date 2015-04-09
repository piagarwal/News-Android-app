package com.example.pi.news;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;
import org.apache.*;

public class MainActivity2 extends ListActivity {

    String yahooURLFirst = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20google.news%20where%20q%20%3D%20%22";
    String yahooURLSecond = "%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";


    private static final String TAG = "STOCKQUOTE";

    TableLayout newsTableLayout;
    //TextView exampleTextView;
    String[][] headlinesArray = new String[100][4];
    ArrayList<HashMap<String,CharSequence>> newsList = new ArrayList<HashMap<String, CharSequence>>();
    int indexHeadlineArray = 0;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

       // newsTableLayout = (TableLayout)findViewById(R.id.newsTableLayout);
      //  exampleTextView = (TextView)findViewById(R.id.exampleTextView);
        listView = getListView();

        Intent intent = getIntent();
        String location = intent.getStringExtra(MainActivity.LOCATION);
        location = Uri.encode(location);
        Log.d("location", location);
        final String yqlURL = yahooURLFirst + location + yahooURLSecond;
      //  final String yqlURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20google.news%20where%20q%20%3D%20%22new%20york%22&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        new MyAsyncTask().execute(yqlURL);



    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(true);

                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new InputStreamReader(getUrlData(params[0])));

                int eventType = parser.getEventType();
                // beginDocument(parser, "query");
                beginDocument2(parser, "results");
                parser.next();


                do {
                    HashMap<String, CharSequence> map = new HashMap<String,CharSequence>();
                    nextElement(parser);
                    fetchdata(parser, "publishedDate");
                 //   Log.d("test",headlinesArray[indexHeadlineArray][0]);
                 //   Log.d("test",headlinesArray[indexHeadlineArray][1]);
                    if (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        map.put("url", headlinesArray[indexHeadlineArray][0]);
                        map.put("title", Html.fromHtml(headlinesArray[indexHeadlineArray][1]));
                        map.put("publisher", headlinesArray[indexHeadlineArray][2]);
                        map.put("publishdate", headlinesArray[indexHeadlineArray++][3]);
                        newsList.add(map);
                    }

                eventType = parser.getEventType();
                } while (eventType != XmlPullParser.END_DOCUMENT);


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            } finally {
            }

            return null;

        }

    private void fetchdata(XmlPullParser parser, String element ) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            parser.next();
            int eventType = parser.getEventType();

            if (eventType == XmlPullParser.TEXT) {
                Log.d(TAG, parser.getText());
                String valueFromXml = parser.getText();
                parser.next();
                switch(element){
                    case "unescapedUrl":
                        headlinesArray[indexHeadlineArray][0] = valueFromXml;
                       // Log.d("test","done" );
                        break;
                    case "titleNoFormatting":
                        headlinesArray[indexHeadlineArray][1] = valueFromXml;
                        break;
                    case "publisher":
                        headlinesArray[indexHeadlineArray][2] = valueFromXml;
                        break;
                    case "publishedDate":
                        headlinesArray[indexHeadlineArray][3] = valueFromXml;
                       // Log.d("test","done" );
                }


            }
        }
    }


        public void beginDocument2(XmlPullParser parser, String Element) throws XmlPullParserException, IOException {


            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getName().equals(Element)) {
                        Log.d(TAG, parser.getName());
                        break;
                    }
                }
                parser.next();
            }


        }


        public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet method = new HttpGet(new URI(url));

            HttpResponse res = client.execute(method);

            return res.getEntity().getContent();
        }


        public final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
            int type = parser.getEventType();

            String urlElement = "unescapedUrl";

            String titleElement = "titleNoFormatting";
            String publisherElement = "publisher";
            String publishedDateElement = "publishedDate";



            while (type != parser.END_DOCUMENT) {
                if (type == XmlPullParser.START_TAG) {
                    if (parser.getName().equals(urlElement)) {
                        break;
                    }
                }
                parser.next();
                type = parser.getEventType();
            }
            fetchdata (parser,urlElement);

            while (type != parser.END_DOCUMENT) {

                if (type == XmlPullParser.START_TAG) {

                    if (parser.getName().equals(titleElement)) {
                        break;
                    }
                }
                parser.next();
                type = parser.getEventType();
            }
            fetchdata (parser,titleElement);

            while (type != parser.END_DOCUMENT) {

                if (type == XmlPullParser.START_TAG) {

                    if (parser.getName().equals(publisherElement)) {
                        break;
                    }
                }
                parser.next();
                type = parser.getEventType();
            }
            fetchdata (parser,publisherElement);

            while (type != parser.END_DOCUMENT) {

                if (type == XmlPullParser.START_TAG) {

                    if (parser.getName().equals(publishedDateElement)) {
                        break;
                    }
                }
                parser.next();
                type = parser.getEventType();
            }




        //  if(parser.getEventType() == XmlPullParser.END_DOCUMENT) Log.d(TAG,"END OF DOCUMENT");
        }

       private void addHeadLine(int index){
          //  LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          //  View newHeadline = inflater.inflate(R.layout.new_healine, null);
          //  TextView newHeadlineTextView = (TextView) newHeadline.findViewById(R.id.newHeadlineTextView);
           // Spanned e = Html.fromHtml(headlinesArray[index][1]);




           // newHeadlineTextView.setText(e);
          //  newsTableLayout.addView(newHeadline);
  /*          newHeadlineTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"CLICK DETECTED");

                    Intent openWebPage = new Intent(Intent.ACTION_VIEW,Uri.parse(headlinesArray[0][0]));
                    startActivity(openWebPage);
                }
            });*/

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG,"CLICK DETECTED");
                    Intent openWebPage = new Intent(Intent.ACTION_VIEW,Uri.parse(newsList.get(position).get("url").toString()));

                    startActivity(openWebPage);
                }
            });



           ListAdapter adapter = new SimpleAdapter(MainActivity2.this,newsList,R.layout.new_healine, new String[] {"title","publisher","publishdate"}, new int[] {R.id.newHeadlineTextView,R.id.sourceTextView,R.id.dateTextView});
           setListAdapter(adapter);

        }
        @Override
    protected void onPostExecute(String result){
            super.onPostExecute(result);
        for(int i = 0; i < indexHeadlineArray; i++){
            addHeadLine(i);
        }
      //  Log.d(TAG,"NAHI");
     //   exampleTextView.setText(headlinesArray[0]);
    }

        protected void onProgressUpdate(Integer... progress) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
