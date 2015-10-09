package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.utils.ServerConfiguration;
import hhg0104.barcodeprj.utils.ToastMessage;

/**
 * Created by HGHAN on 2015-09-25.
 */
public class ServerConfigActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_config);

        // Host 입력창에 포커스
        final EditText hostInput = (EditText) findViewById(R.id.server_host_input_activity);
        hostInput.requestFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//        hostInput.addTextChangedListener(new TextWatcher() {
//
//            public String beforeText;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                beforeText = hostInput.getText().toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String text = s.toString();
//                int textLength = text.length();
//
//                if (textLength < 3) {
//                    return;
//                }
//
//                if (beforeText.length() > textLength) {
//                    return;
//                }
//
//                int[] periodIndex = {3, 7, 9};
//
//                for (int index : periodIndex) {
//                    if (textLength == index) {
//                        hostInput.setText(text + StringConstants.PERIOD);
//                        hostInput.setSelection(textLength + 1);
//                    }
//                }
//            }
//        });

        TextView okBtn = (TextView) findViewById(R.id.input_server_ok_activity);
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText hostInput = (EditText) findViewById(R.id.server_host_input_activity);
        EditText portInput = (EditText) findViewById(R.id.server_port_input_activity);

        String host = hostInput.getText().toString();
        String port = portInput.getText().toString();

        if(host.isEmpty() || port.isEmpty()){
            ToastMessage.showError(this, "서버 정보를 정확하게 입력해주십시오.", Toast.LENGTH_LONG);
            return;
        }

        SharedPreferences preference = getSharedPreferences(ServerConfiguration.SERVER_PREFERENCE, MODE_PRIVATE);
        ServerConfiguration.setServerPreferences(preference, host, Integer.valueOf(port));

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }


}
