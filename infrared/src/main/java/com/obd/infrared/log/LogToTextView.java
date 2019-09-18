package com.obd.infrared.log;

import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;

public class LogToTextView extends Logger {

    protected final EditText editText;
    protected final String lineSeparator;

    public LogToTextView(EditText editText, String tag) {
        super(tag);
        this.editText = editText;
        this.lineSeparator = System.getProperty("line.separator");

        runHandler();
    }

    private boolean wasDestroyed = false;

    public void destroy() {
        wasDestroyed = true;
    }

    private void runHandler() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean hasNew = false;
                while (!strings.isEmpty()) {
                    hasNew = true;
                    editText.append(strings.remove() +lineSeparator);
                }
                if (hasNew) {
                    //textView.setSelection(textView.getText().length());
                }
                if (!wasDestroyed) {
                    handler.postDelayed(this, 100);
                }
            }
        }, 0);
    }

    private final Queue<String> strings = new LinkedList<>();

    @Override
    public void log(String message) {
        strings.add("Pilot[" + tag + "]: " + message);
    }

    @Override
    public void warning(String message) {
        strings.add("WARNING [" + tag + "]: " + message);
    }

    @Override
    public void error(String description, Exception exception) {
        strings.add("Pilot[" + tag + "]: ERROR { Description: " + description + "; Exception: " + exception.getMessage() + " }");
    }
}
