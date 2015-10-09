package hhg0104.barcodeprj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.listener.ServerConfigurationListener;
import hhg0104.barcodeprj.utils.ServerConfiguration;
import hhg0104.barcodeprj.utils.StringConstants;
import hhg0104.barcodeprj.utils.ToastMessage;

/**
 * Created by HGHAN on 2015-09-15.
 */
public class ServerConfigurationDialog extends Dialog implements View.OnClickListener, TextView.OnEditorActionListener {

    private ServerConfigurationListener listener;

    public ServerConfigurationDialog(Context context, ServerConfigurationListener listener) {
        super(context);
        setTitle("Server Configuration");
        setContentView(R.layout.dialog_server_config);

        this.listener = listener;

        // Host 입력창에 포커스
        final EditText hostInput = (EditText) findViewById(R.id.server_host_input);
        hostInput.requestFocus();

        EditText hostText = (EditText) findViewById(R.id.server_host_input);
        EditText portText = (EditText) findViewById(R.id.server_port_input);

        SharedPreferences preference = context.getSharedPreferences(ServerConfiguration.SERVER_PREFERENCE, Context.MODE_PRIVATE);

        String preHostText = preference.getString(ServerConfiguration.SERVER_HOST, StringConstants.EMPTY);
        String prePortText = String.valueOf(preference.getInt(ServerConfiguration.SERVER_PORT, 0));

        hostText.setText(preHostText);
        portText.setText(prePortText);

        hostText.setSelection(0, preHostText.length());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        TextView okBtn = (TextView) findViewById(R.id.input_server_ok);
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int btnID = v.getId();

        if (btnID == R.id.input_server_ok) {

            EditText hostInput = (EditText) findViewById(R.id.server_host_input);
            EditText portInput = (EditText) findViewById(R.id.server_port_input);

            String host = hostInput.getText().toString();
            String portStr = portInput.getText().toString();

            if (host.isEmpty() || portStr.isEmpty()) {
                ToastMessage.showError(getContext(), "서버 Host와 Port 정보를 입력해주십시오.", Toast.LENGTH_SHORT);
                return;
            }

            int port = 0;
            try {
                port = Integer.valueOf(portStr);
            } catch (NumberFormatException e) {
                ToastMessage.showError(getContext(), "Port 정보를 숫자로만 입력해주십시오.", Toast.LENGTH_SHORT);
                return;
            }

            listener.config(host, port);
            dismiss();
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.server_host_input: {
                if (event.getAction() == KeyEvent.KEYCODE_DEL) {
                    Log.i("TEST", "TEST");
                }
                break;
            }
        }
        return false;
    }
}
