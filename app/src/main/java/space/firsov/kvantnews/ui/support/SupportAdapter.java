package space.firsov.kvantnews.ui.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import space.firsov.kvantnews.R;

public class SupportAdapter extends ArrayAdapter<Support> {

    public SupportAdapter(Context context, Support[] arr) {
        super(context, R.layout.support_adapter, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Support support = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.support_adapter, null);
        }
        assert support != null;
        if(!support.answer.equals(""))((TextView) convertView.findViewById(R.id.answer)).setText(support.answer);
        ((TextView) convertView.findViewById(R.id.question)).setText(support.question);
        ((TextView) convertView.findViewById(R.id.id)).setText(String.valueOf(support.id));
        return convertView;
    }
}