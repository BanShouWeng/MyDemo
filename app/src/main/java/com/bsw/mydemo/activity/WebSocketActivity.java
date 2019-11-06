package com.bsw.mydemo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.websocket.WebSocketConnection;
import com.bsw.mydemo.utils.websocket.WebSocketConnectionHandler;
import com.bsw.mydemo.utils.websocket.exceptions.WebSocketException;
import com.bsw.mydemo.utils.websocket.interfaces.IWebSocket;
import com.bsw.mydemo.utils.websocket.types.WebSocketOptions;

public class WebSocketActivity extends BaseActivity {
    private final IWebSocket mConnection = new WebSocketConnection();
    private static final String PREFS_NAME = "AutobahnAndroidEcho";

    private EditText mHostname;
    private EditText mPort;
    private TextView mStatusline;
    private Button mStart;
    private EditText mMessage;
    private Button mSendMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_socket;
    }

    @Override
    protected void findViews() {
        mHostname = findViewById(R.id.hostname);
        mPort = findViewById(R.id.port);
        mStatusline = findViewById(R.id.statusline);
        mStart = findViewById(R.id.start);
        mMessage = findViewById(R.id.msg);
        mSendMessage = findViewById(R.id.sendMsg);
    }

    @Override
    protected void formatViews() {
        mSettings = getSharedPreferences(PREFS_NAME, 0);
        loadPrefs();

        setButtonConnect();
        mSendMessage.setEnabled(false);
        mMessage.setEnabled(false);

        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.sendMessage(mMessage.getText().toString());
            }
        });
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    private SharedPreferences mSettings;

    private void alert(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void loadPrefs() {
        mHostname.setText(mSettings.getString("hostname", "wss://192.168.32.133"));
        mPort.setText(mSettings.getString("port", "443"));
    }

    private void savePrefs() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("hostname", mHostname.getText().toString());
        editor.putString("port", mPort.getText().toString());
        editor.commit();
    }

    private void setButtonConnect() {
        mHostname.setEnabled(true);
        mPort.setEnabled(true);
        mStart.setText("Connect");
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    private void setButtonDisconnect() {
        mHostname.setEnabled(false);
        mPort.setEnabled(false);
        mStart.setText("Disconnect");
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.sendClose();
            }
        });
    }

    private void start() {
        String hostname = mHostname.getText().toString();
        if (!hostname.startsWith("ws://") && !hostname.startsWith("wss://")) {
            hostname = "ws://" + hostname;
        }
        String port = mPort.getText().toString();

        String wsuri;
        if (!port.isEmpty()) {
            wsuri = hostname + ":" + port;
        } else {
            wsuri = hostname;
        }
        wsuri = wsuri + "/WebSocketForApp/AdapterDeviceWebSocket/01_WF_16091402010100011078";

        mStatusline.setText("Status: Connecting to " + wsuri + " ..");

        setButtonDisconnect();
        WebSocketOptions connectOptions = new WebSocketOptions();
        connectOptions.setReconnectInterval(5000);

        try {
            final String finalWsuri = wsuri;
            mConnection.connect(wsuri, new WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    mStatusline.setText("Status: Connected to " + finalWsuri);
                    savePrefs();
                    mSendMessage.setEnabled(true);
                    mMessage.setEnabled(true);
                }

                @Override
                public void onMessage(String payload) {
                    alert("Got echo: " + payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    alert("Connection lost.");
                    mStatusline.setText("Status: Ready.");
                    setButtonConnect();
                    mSendMessage.setEnabled(false);
                    mMessage.setEnabled(false);
                }
            }, connectOptions);
        } catch (WebSocketException e) {
            Log.d(getName(), e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection.isConnected()) {
            mConnection.sendClose();
        }
    }
}
