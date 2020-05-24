package space.firsov.kvantnews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;


public class RegistrationActivity extends AppCompatActivity {
    boolean is_ready = false;
    int person = 0;
    Button person_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final EditText login = findViewById(R.id.login);
        final EditText password = findViewById(R.id.password);
        final EditText password2 = findViewById(R.id.repeated_password);
        person_btn = findViewById(R.id.personalities);
        Button register_btn = findViewById(R.id.btn_registration);
        Button back_btn = findViewById(R.id.btn_back_to_login);
        final TextView tw1 = findViewById(R.id.tw1);
        person_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnline()) {
                    tw1.setText(R.string.no_internet_connection);
                } else {
                    String name = login.getText().toString();
                    String pas1 = password.getText().toString();
                    String pas2 = password2.getText().toString();
                    if (!is_ready) tw1.setText(R.string.fill_in_register);
                    else if (name.length() < 5) tw1.setText(R.string.min_length_login_5);
                    else if (pas1.length() < 5) tw1.setText(R.string.min_length_pass_5);
                    else if (!pas1.equals(pas2)) tw1.setText(R.string.pass_should_equal);
                    else {
                        try {
                            boolean is_clear_name = new addNewUser(name, pas1, String.valueOf(person)).execute().get();
                            if (is_clear_name) tw1.setText(R.string.you_successfully_register);
                            else {
                                tw1.setText(R.string.this_name_busy);
                            }
                        } catch (Exception e) {
                            tw1.setText(R.string.network_problems);
                        }
                    }
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @SuppressLint("ResourceType")
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.inflate(R.layout.popup_menu1);
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                is_ready = true;
                                person = 2;
                                person_btn.setText(R.string.student);
                                person_btn.setTextColor(R.color.light_gray);
                                return true;
                            case R.id.menu2:
                                is_ready = true;
                                person = 3;
                                person_btn.setText(R.string.parent);
                                person_btn.setTextColor(R.color.light_gray);
                                return true;
                            case R.id.menu3:
                                is_ready = true;
                                person = 4;
                                person_btn.setText(R.string.teacher);
                                person_btn.setTextColor(R.color.light_gray);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        popupMenu.show();
    }
}
