package ru.j4j.retrofit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 18.06.2019.
 */
public class DeleteDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String message = "blank";
        Bundle arg = getArguments();
        if (arg != null) {
            message = "" + arg.getInt("message");
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle("Response code (must be 200):")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (d, i) -> {})
                .create();
    }
}
