package space.firsov.kvantnews.ui.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import space.firsov.kvantnews.R;
import space.firsov.kvantnews.User;

public class AchievementsFragment extends Fragment  implements View.OnClickListener {
    private ArrayList<Achievement> listAchievements = new ArrayList<>();
    private AchievementsDB achievementsDB;
    private AchievementAdapter adapter;
    private ListView lv;
    private String login;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievements, container, false);
        login = new User(getActivity()).getLogin();
        lv =  (ListView) root.findViewById(R.id.list_container);
        achievementsDB = new AchievementsDB(root.getContext());
        listAchievements = achievementsDB.selectAll();
        if(listAchievements.size()!=0){
            adapter = new AchievementAdapter(getContext(), drawThreadNews());
            lv.setAdapter(adapter);
        }else{
            reloadPressed();
        }
        ImageButton reload_btn = (ImageButton)root.findViewById(R.id.reload_btn);
        reload_btn.setOnClickListener(this);
        return root;
    }

    private Achievement[] drawThreadNews (){
        Achievement[] achievements = new Achievement[listAchievements.size()];
        for(int i=0;i<listAchievements.size();i++){
            achievements[i] = listAchievements.get(i);
        }
        return achievements;
    }

    private void reloadPressed() {
        if(isOnline()) {
            try {
                new GetUserAchievements(login, getContext()).execute().get();
            }catch (Exception e) {
                //
            }
            listAchievements = achievementsDB.selectAll();
            adapter = new AchievementAdapter(getContext(), drawThreadNews());
            lv.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reload_btn:
                reloadPressed();
                break;
        }
    }
}
