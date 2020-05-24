package space.firsov.kvantnews.ui.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import space.firsov.kvantnews.R;
import space.firsov.kvantnews.User;

public class SupportFragment extends Fragment  implements View.OnClickListener {
    private ArrayList<Support> listSupports = new ArrayList<>();
    private SupportsDB supportsDB;
    private SupportAdapter adapter;
    private ListView lv;
    private String login;
    private EditText user_question;
    private Button submit_question_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_support, container, false);
        login = new User(getActivity()).getLogin();
        user_question = root.findViewById(R.id.user_question);
        submit_question_btn = root.findViewById(R.id.submit_question_btn);
        lv =  (ListView) root.findViewById(R.id.list_container);
        supportsDB = new SupportsDB(root.getContext());
        listSupports = supportsDB.selectAll();
        if(listSupports.size()!=0){
            adapter = new SupportAdapter(getContext(), drawThreadNews());
            lv.setAdapter(adapter);
        }else{
            reloadPressed();
        }
        ImageButton reload_btn = (ImageButton)root.findViewById(R.id.reload_btn);
        reload_btn.setOnClickListener(this);
        submit_question_btn.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String id2 = ((TextView) itemClicked.findViewById(R.id.id)).getText().toString(); // получаем текст нажатого элемента
                try {
                    new DeleteSupportByID(Long.parseLong(id2), getContext()).execute().get();
                } catch (Exception e){
                    //
                }
                reloadPressed();
            }
        });
        return root;
    }

    private Support[] drawThreadNews (){
        Support[] supports = new Support[listSupports.size()];
        for(int i=0;i<listSupports.size();i++){
            supports[i] = listSupports.get(i);
        }
        return supports;
    }

    private void reloadPressed() {
        if(isOnline()) {
            try {
                new GetUserSupports(login, getContext()).execute().get();
            }catch (Exception e){
                //
            }
            listSupports = supportsDB.selectAll();
            adapter = new SupportAdapter(getContext(), drawThreadNews());
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
            case R.id.submit_question_btn:
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                try {
                    new SubmitSupport(login, user_question.getText().toString(),getContext()).execute().get();
                } catch (Exception e){
                    //
                }
                reloadPressed();
                user_question.setText("");
                break;
        }
    }
}
