package space.firsov.kvantnews.ui.achievements;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetUserAchievements extends AsyncTask<String, Void, Integer> {
    private String login;
    private AchievementsDB achievementDB;

    public GetUserAchievements(String login, Context context) {
        this.login = login;
        achievementDB= new AchievementsDB(context);
    }

    @Override
    protected Integer doInBackground(String... args) {
        try {
            String url = "https://kvantfp.000webhostapp.com/ReturnAchievementsForStudent.php?login=" + login;
            Document document = Jsoup.connect(url).get();
            Elements element = document.select("p[class=achievement]");
            achievementDB.deleteAll();
            for (int i = 0; i < element.size(); i++) {
                String achievement = element.eq(i).text();
                achievementDB.insert(achievement);
            }
        } catch (Exception e) {
            //
        }
        return 1;
    }
}