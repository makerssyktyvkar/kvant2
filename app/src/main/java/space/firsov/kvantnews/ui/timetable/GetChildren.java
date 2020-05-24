package space.firsov.kvantnews.ui.timetable;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetChildren extends AsyncTask<String, Void, Integer> {
    private String login;
    private ChildrenDB childrenDB;

    public GetChildren(String login, Context context) {
        this.login = login;
        childrenDB= new ChildrenDB(context);
    }

    @Override
    protected Integer doInBackground(String... args) {
        String url = "http://kvantfp.000webhostapp.com/ReturnChildren.php?login=" + login;
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Elements element = document.select("p[class=login]");
            childrenDB.deleteAll();
            for(int i=0;i<element.size();i++){
                childrenDB.insert(element.eq(i).text());
            }
        } catch (Exception e) {
            //
        }
        return 1;
    }
}