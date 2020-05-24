package space.firsov.kvantnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class GetCourses extends AsyncTask<String, Void, String> {
    private String login;
    private CoursesOfUserDB coursesOfUserDB;

    GetCourses(String login, Context context) {
        this.login = login;
        coursesOfUserDB = new CoursesOfUserDB(context);
    }

    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(String... args) {
        try {
            String url = "https://kvantfp.000webhostapp.com/ReturnCourses.php?login=" + login;
            Document document = Jsoup.connect(url).get();
            Elements element = document.select("li[class=course-item]");
            coursesOfUserDB.deleteAll();
            for (int i = 0; i < element.size(); i++) {
                String name = element.eq(i).select("h1[class=name_course]").eq(0).text();
                int id = Integer.parseInt(element.eq(i).select("p[class=id_course]").eq(0).text());
                coursesOfUserDB.insert(id, name);
            }
        } catch (Exception e) {
            //
        }
        return "";
    }
}