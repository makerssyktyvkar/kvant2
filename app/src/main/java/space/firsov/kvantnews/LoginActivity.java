package space.firsov.kvantnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

import space.firsov.kvantnews.ui.achievements.GetUserAchievements;
import space.firsov.kvantnews.ui.news.GetNews;
import space.firsov.kvantnews.ui.posts.GetCoursesNews;
import space.firsov.kvantnews.ui.support.GetUserSupports;
import space.firsov.kvantnews.ui.timetable.ChildrenDB;
import space.firsov.kvantnews.ui.timetable.GetChildren;
import space.firsov.kvantnews.ui.timetable.GetUserTimetable;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button registration_btn;
    Button login_btn;
    EditText login_et;
    EditText password_et;
    TextView tw1;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registration_btn = findViewById(R.id.registration_btn);
        login_btn = findViewById(R.id.btn_come_in);
        login_et = findViewById(R.id.login);
        password_et = findViewById(R.id.password);
        user = new User(this);
        if(getIntent().getIntExtra("type",0)==1){
            user.deleteAll();
        }
        ArrayList<Pair<String, Integer>> now = user.selectAll();
        if(now.size()!=0){
            startMain();
        }
        tw1 = findViewById(R.id.tw1);
        login_btn.setOnClickListener(this);
        registration_btn.setOnClickListener(this);
    }

    private void startMain(){
        Intent intent = new Intent(LoginActivity.this, StudentNavActivity.class);
        startActivity(intent);
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
        return false;
    }

    @Override
    public void onClick(View v){
        String login = login_et.getText().toString();
        String password = password_et.getText().toString();

        switch (v.getId()){
            case R.id.registration_btn:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_come_in:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                if(isOnline()) {
                    String type = "pass";
                    try {
                        type = new GetTypeOfUser(login, password).execute().get();
                    } catch (Exception e) {
                        //
                    }
                    int tp = Integer.parseInt(type);
                    if (tp == 0) tw1.setText(R.string.NoSuchUsers);
                    else {
                        user.insert(login,tp);
                        findAll(login);
                        switch(tp){
                            case 2:
                                findAllAboutStudent(login);
                                break;
                            case 3:
                                findAllAboutParent(login);
                                break;
                            case 4:
                                findAllAboutTeacher(login);
                                break;
                        }
                        startMain();
                    }
                }else{
                    tw1.setText(R.string.no_internet_connection);
                }
                break;
        }
    }
    private void findAll(String login){
        Toast.makeText(this, R.string.please_wait, Toast.LENGTH_LONG).show();
        try {
            new GetNews(this).execute().get();
        } catch (Exception e){
            //
        }
        try {
            new GetCoursesNews(login, this).execute().get();
        } catch (Exception e){
            //
        }
        try {
            new GetUserSupports(login, this).execute().get();
        } catch (Exception e){
            //
        }
    }
    private void findAllAboutStudent(String login){
        try {
            new GetCourses(login, this).execute().get();
        } catch (Exception e){
            //
        }
        try {
            new GetUserAchievements(login, this).execute().get();
        } catch (Exception e){
            //
        }
        try {
            new GetUserTimetable(login, this).execute().get();
        } catch (Exception e){
            //
        }
    }
    private void findAllAboutParent(String login){
        try{
            new GetChildren(login,this);
        }catch (Exception e){
            //
        }
        for(String childName : new ChildrenDB(this).selectAll()) {
            try {
                new GetUserTimetable(childName, this).execute().get();
            } catch (Exception e) {
                //
            }
        }
    }
    private void findAllAboutTeacher(String login){
        try {
            new GetCourses(login, this).execute().get();
        } catch (Exception e){
            //
        }
        try {
            new GetUserTimetable(login, this).execute().get();
        } catch (Exception e){
            //
        }
        try{
            new GetChildren(login,this);
        }catch (Exception e){
            //
        }
    }
}
