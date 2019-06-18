package ru.j4j.retrofit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.j4j.R;

/**
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 18.06.2019.
 */
public class SendPostDialog extends DialogFragment {

    private DataDialogReceiver context;
    View view;

    public interface DataDialogReceiver {
        void onDataCollected(Post post);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (DataDialogReceiver) context;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        this.view = inflater.inflate(R.layout.submit_dialog_layout, null);
        builder.setView(view)
                .setPositiveButton("Apply", this::onApplyClick)
                .setNegativeButton("Cancel", (dialog, id) -> {
                });
        return builder.create();
    }

    private void onApplyClick(DialogInterface dialog, int id) {
        EditText userId = view.findViewById(R.id.dialog_userId);
        EditText title = view.findViewById(R.id.dialog_title);
        EditText text = view.findViewById(R.id.dialog_text);
        int user;
        try {
            user = Integer.parseInt(userId.getText().toString());
        } catch (NumberFormatException | NullPointerException e) {
            user = 101;
        }
        String caption = title.getText().toString();
        String body = text.getText().toString();
        Post post = new Post(user, caption, body);
        context.onDataCollected(post);
    }

    @Override
    public void onDetach() {
        context = null;
        super.onDetach();
    }
}