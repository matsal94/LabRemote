package com.obd.infrared.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class PopupDialog extends AppCompatDialogFragment {

    private EditText login;
    private PopupDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = login.getText().toString();
                        listener.applyTexts(username);
                    }
                });

        login = view.findViewById(R.id.loginET);

        return builder.create();
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            listener = (PopupDialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "Trzeba zaimplementować PopupDialogListener");
        }
    }

    public interface PopupDialogListener
    {
        void applyTexts(String login);
    }
}
