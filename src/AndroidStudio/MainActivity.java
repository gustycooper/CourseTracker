package com.example.apex.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.lang.Object;

public class MainActivity extends AppCompatActivity {




    Button submit;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button)findViewById(R.id.loginButton);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submit();
            }
        });

    }

    public void submit ()  {
        EditText netId = (EditText)findViewById(R.id.NetId);
        EditText password = (EditText)findViewById(R.id.password);
        String id = netId.getText().toString();
        String pass = password.getText().toString();

        if (id.length() == 0) {

            //display message if text field is empty
            Toast.makeText(getBaseContext(),"Enter NetId",Toast.LENGTH_SHORT).show();
        }
        else if (pass.length() == 0){
            //display message if text field is not long enough
            Toast.makeText(getBaseContext(),"Password length 0",Toast.LENGTH_SHORT).show();

        } else {

            ArrayList<Map<String,String>> user_creds = RemoteFetch(Config.USERS_URL,  id, pass);
            if (user_creds.isEmpty()){
                Toast.makeText(getBaseContext(),"Empty",Toast.LENGTH_SHORT).show();;
                return;
            }
            user = new User(user_creds.get(0));
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);

        }


    }


    private ArrayList<Map<String,String>> RemoteFetch(String urlConn, String id, String pass) {


            try {
                URL url = new URL(String.format(urlConn));
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();
                String userpass = id + ":" + pass;
                String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

                System.out.print(url);
                connection.setRequestProperty("Authorization", basicAuth);


//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(connection.getInputStream()));
                InputStream input = url.getInputStream();



                Map<String,Object> data = new Gson().fromJson(new InputStreamReader(input, "UTF-8"),
                        new TypeToken<Map<String, String>>(){}.getType());
//                StringBuffer data = new StringBuffer(1024);
//                String tmp="";
//                while((tmp=reader.readLine())!=null)
//                    data.append(tmp).append("\n");
//                reader.close();



                System.out.print(data);
                // This value will be 404 if the request was not
                // successful
//                if(data.getInt("cod") != 200){
//                    return null;
//                }

                return (ArrayList<Map<String, String>>)data.get("root");
            }catch(IOException | RuntimeException e){

                return new ArrayList<>();
            }
        }
}

//public JSONObject getJSON(){

//}



//    public void submit()
//    {
//        EditText netId = (EditText)findViewById(R.id.NetId);
//        EditText password = (EditText)findViewById(R.id.password);
//        String id = netId.getText().toString();
//        String pass = password.getText().toString();
//
////       //todo write if statement to check that netId isnt blank and password is >= 6 chars
//         if(id.length() > 0 && pass.length() >= 6){
//
//             RemoteFetch();
//             Intent intent = new Intent(this, HomeScreen.class);
//             startActivity(intent);
//
//         } else if (id.length() == 0) {
//
//             //display message if text field is empty
//             Toast.makeText(getBaseContext(),"Enter NetId",Toast.LENGTH_SHORT).show();
//         }
//         else if (pass.length() < 6){
//             //display message if text field is not long enough
//             Toast.makeText(getBaseContext(),"Password length needs to be 6 or more",Toast.LENGTH_SHORT).show();
//         }
//    }




//    public String PostData(String[] data) {
//        String s="";
//        data;
//
//
//    public JSONObject getJSON(Context context, String[] data)
//    {
//        String COURSETRACKER_DB =
//                "https://coursetrackerumw.herokuapp.com/users?";
//        try{
//            URL url = new URL(String.format(COURSETRACKER_DB, data));
//            HttpURLConnection connection =
//                    (HttpURLConnection)url.openConnection();
//
//
//            connection.setRequestMethod("GET");
//
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(connection.getInputStream()));
//
//            StringBuffer json = new StringBuffer(1024);
//            String tmp="";
//            while((tmp=reader.readLine())!=null)
//                json.append(tmp).append("\n");
//            reader.close();
//
//            JSONObject j_data = new JSONObject(json.toString());
//
//            // This value will be 404 if the request was not
//            // successful
//            if(j_data.getInt("cod") != 200){
//                return null;
//            }
//
//            return j_data;
//        }catch(Exception e){
//            return null;
//        }
//    }

