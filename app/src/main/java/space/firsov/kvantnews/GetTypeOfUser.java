package space.firsov.kvantnews;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GetTypeOfUser extends AsyncTask<String,Void,String> {
    private String login, password;

    GetTypeOfUser(String login, String password) {
        this.password = password;
        this.login = login;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String link = "https://kvantfp.000webhostapp.com/getTypeOfUser.php";
            String data = "login" + "=" + login + "&password=" + password;
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(data);
            //wr.write(data2);
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();//return Html.fromHtml(sb.toString()).toString();
        } catch (Exception e) {
            return "0";
        }
    }
}