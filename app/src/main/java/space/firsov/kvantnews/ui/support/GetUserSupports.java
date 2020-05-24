package space.firsov.kvantnews.ui.support;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import space.firsov.kvantnews.ui.support.SupportsDB;

public class GetUserSupports extends AsyncTask<String, Void, Integer> {
    private String login;
    private SupportsDB supportDB;

    public GetUserSupports(String login, Context context) {
        this.login = login;
        supportDB= new SupportsDB(context);
    }

    @Override
    protected Integer doInBackground(String... args) {
        try {
            String url = "https://kvantfp.000webhostapp.com/ReturnSupportsForUser.php?login=" + login;
            Document document = Jsoup.connect(url).get();
            Elements element = document.select("li[class=support-item]");
            supportDB.deleteAll();
            for (int i = 0; i < element.size(); i++) {
                long id = Integer.parseInt(element.eq(i).select("p[class=id]").eq(0).text());
                String answer = element.eq(i).select("p[class=answer]").eq(0).text();
                String question = element.eq(i).select("p[class=question]").eq(0).text();
                supportDB.insert(id, answer,question);
            }
        } catch (Exception e) {
            //
        }
        return 1;
    }
}