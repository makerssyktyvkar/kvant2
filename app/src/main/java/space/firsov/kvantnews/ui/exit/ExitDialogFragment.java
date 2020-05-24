package space.firsov.kvantnews.ui.exit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import space.firsov.kvantnews.CoursesOfUserDB;
import space.firsov.kvantnews.LoginActivity;
import space.firsov.kvantnews.StudentNavActivity;
import space.firsov.kvantnews.User;
import space.firsov.kvantnews.ui.achievements.AchievementsDB;
import space.firsov.kvantnews.ui.posts.CoursesNewsOfUserDB;
import space.firsov.kvantnews.ui.support.SupportsDB;
import space.firsov.kvantnews.ui.timetable.ChildrenDB;
import space.firsov.kvantnews.ui.timetable.TimetableDB;

public class ExitDialogFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Вы хотите выйти?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "Вы вышли",
                        Toast.LENGTH_LONG).show();
                CoursesNewsOfUserDB coursesNewsOfUserDB = new CoursesNewsOfUserDB(getContext());
                coursesNewsOfUserDB.deleteAll();
                SupportsDB supportsDB = new SupportsDB(getContext());
                supportsDB.deleteAll();
                TimetableDB timetableDB = new TimetableDB(getContext());
                timetableDB.deleteAll();
                ChildrenDB childrenDB = new ChildrenDB(getContext());
                childrenDB.deleteAll();
                AchievementsDB achievementsDB = new AchievementsDB(getContext());
                achievementsDB.deleteAll();
                CoursesOfUserDB coursesOfUserDB = new CoursesOfUserDB(getContext());
                coursesNewsOfUserDB.deleteAll();
                User user = new User(getContext());
                user.deleteAll();
                startLoginActivity();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startMain();
                dialog.cancel();
            }
        });
        builder.setCancelable(false);

        return builder.create();
    }
    private void startLoginActivity(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
    private void startMain(){
        Intent intent = new Intent(getContext(), StudentNavActivity.class);
        startActivity(intent);
    }
}
