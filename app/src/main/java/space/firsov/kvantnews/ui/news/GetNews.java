package space.firsov.kvantnews.ui.news;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import space.firsov.kvantnews.ui.achievements.AchievementsDB;

public class GetNews extends AsyncTask<String, Void, Integer> {
    private NewsDB newsDB;

    public GetNews(Context context) {
        newsDB = new NewsDB(context);
    }

    @Override
    protected Integer doInBackground(String... args) {
        if(newsDB.selectAll().size() == 0) {
            try {
                String url = "https://kvantfp.000webhostapp.com/ReturnNews.php";
                Document document = Jsoup.connect(url).get();
                Elements element = document.select("li[class=news-item]");
                newsDB.deleteAll();
                for (int i = 0; i < element.size(); i++) {
                    String title = element.eq(i).select("h2[class=title]").eq(0).text();
                    String desc = element.eq(i).select("p[class=message]").eq(0).text();
                    String time = element.eq(i).select("p[class=time]").eq(0).text();
                    String linkImage;
                    try {
                        linkImage = element.eq(i).select("img").eq(0).attr("src").substring(24);
                    } catch (Exception e) {
                        linkImage = "";
                    }
                    newsDB.insert(title, desc, linkImage, time);
                }
            } catch (Exception e) {
                //
            }
        }
        return 1;
    }
}