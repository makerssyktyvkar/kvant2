package space.firsov.kvantnews.ui.support;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DeleteSupportByID extends AsyncTask<String, Void, Integer> {
    private long id;
    private SupportsDB supportDB;

    public DeleteSupportByID(long id, Context context) {
        this.id = id;
        supportDB= new SupportsDB(context);
    }

    @Override
    protected Integer doInBackground(String... args) {
        try {
            String url = "https://kvantfp.000webhostapp.com/DeleteSupportById.php?id=" + id;
            Document document = Jsoup.connect(url).get();
        } catch (Exception e) {
            //
        }
        return 1;
    }
}