package space.firsov.kvantnews.ui.support;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SubmitSupport extends AsyncTask<String, Void, Integer> {
    private String login, question;
    private SupportsDB supportDB;

    public SubmitSupport(String login, String question, Context context) {
        this.login = login;
        this.question = question;
        supportDB= new SupportsDB(context);
    }

    @Override
    protected Integer doInBackground(String... args) {
        try {
            String url = "https://kvantfp.000webhostapp.com/AddNewSupport.php?login=" +
                    login + "&question=" + question;
            Document document = Jsoup.connect(url).get();
        } catch (Exception e) {
            //
        }
        return 1;
    }
}