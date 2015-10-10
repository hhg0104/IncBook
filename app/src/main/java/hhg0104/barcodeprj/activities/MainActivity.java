package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.adapter.CustomAdapter;
import hhg0104.barcodeprj.connector.ResponseListener;
import hhg0104.barcodeprj.connector.ServerConnector;
import hhg0104.barcodeprj.dialog.ISBNDialog;
import hhg0104.barcodeprj.dialog.OptionDialog;
import hhg0104.barcodeprj.dialog.ServerConfigurationDialog;
import hhg0104.barcodeprj.listener.HttpListener;
import hhg0104.barcodeprj.listener.ISBNDialogListener;
import hhg0104.barcodeprj.listener.OptionDialogListener;
import hhg0104.barcodeprj.listener.ServerConfigurationListener;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.model.ResponseModel;
import hhg0104.barcodeprj.task.IsbnReadTask;
import hhg0104.barcodeprj.utils.BookDataManager;
import hhg0104.barcodeprj.utils.HttpAction;
import hhg0104.barcodeprj.utils.InputMode;
import hhg0104.barcodeprj.utils.IntentExtraEntry;
import hhg0104.barcodeprj.utils.RegisterOption;
import hhg0104.barcodeprj.utils.ServerConfiguration;
import hhg0104.barcodeprj.utils.StringConstants;
import hhg0104.barcodeprj.utils.ToastMessage;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";
    public static final String TEXT_NOT_RIGHT_BARCODE = "정상적인 바코드가 아닙니다.";
    public static final String EAN_13 = "EAN_13";
    private int backButtonCount;

    private Activity mainActivity;

    private List<BookInfo> books = new ArrayList<BookInfo>();

    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainActivity = this;

        boolean hasServerInfo = ServerConfiguration.hasServerConfiguration(getSharedPreferences(ServerConfiguration.SERVER_PREFERENCE, MODE_PRIVATE));

        if (hasServerInfo == false) {
            ToastMessage.showError(this, "서버 정보를 다시 설정해주십시오.", Toast.LENGTH_LONG);

        } else {
            initServerConfiguration();
            loadBookData();
        }
    }

    private void loadBookData() {
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        ServerConnector conn = new ServerConnector(new HttpListener() {
            @Override
            public void getResult(ResponseModel response) {

                if (response.getStatusCode() != 200) {
                    ToastMessage.showError(mainActivity, response.getData(), Toast.LENGTH_LONG);
                    return;
                }

                List<BookInfo> allBooks = new Gson().fromJson(response.getData(), new TypeToken<List<BookInfo>>() {
                }.getType());

                books.clear();
                books.addAll(allBooks);

                adapter = new CustomAdapter(mainActivity, books);
                listView.setAdapter(adapter);

                BookDataManager.getInstance().save(books);

            }
        }, this);

        if (Build.VERSION.SDK_INT >= 11) {
            conn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, HttpAction.GET_BOOKS);
        } else {
            conn.execute(HttpAction.GET_BOOKS);
        }
    }

    private void initServerConfiguration() {
        ServerConfiguration serverConfig = ServerConfiguration.getInstance();

        SharedPreferences serverPrefer = getSharedPreferences(ServerConfiguration.SERVER_PREFERENCE, MODE_PRIVATE);
        String host = serverPrefer.getString(ServerConfiguration.SERVER_HOST, StringConstants.EMPTY);
        int port = serverPrefer.getInt(ServerConfiguration.SERVER_PORT, 0);

        serverConfig.init(host, port);
    }

    private void openServerConfigDialog() {
        ServerConfigurationDialog serverDialog = new ServerConfigurationDialog(mainActivity, new ServerConfigurationListener() {
            @Override
            public void config(String host, int port) {

                SharedPreferences preference = mainActivity.getSharedPreferences(ServerConfiguration.SERVER_PREFERENCE, MODE_PRIVATE);
                ServerConfiguration.setServerPreferences(preference, host, port);

                refresh();
            }
        });

        serverDialog.show();
    }

    private boolean hasInfo(String host, int port) {
        if (host == null || port == 0) {
            return false;
        }
        return true;
    }

    private void refresh() {

        finish();
        startActivity(getIntent());

        ToastMessage.showDefault(mainActivity, "데이터 갱신 완료", Toast.LENGTH_SHORT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            // 성공
            if (resultCode == Activity.RESULT_OK) {
                // 인식한 QR Code의 데이터를 String 변수에 저장
                // SCAN_RESULT : 결과값, URL과 같은 QR Code에 저장된 주된 내용(데이터)
                // SCAN_RESULT_FORMAT : 결과값 형식 (QR Code인 경우 qr_code라는 값을 응답)
                final String isbn = intent.getStringExtra(SCAN_RESULT);
                String format = intent.getStringExtra(SCAN_RESULT_FORMAT);

                try {
                    Long.valueOf(isbn);
                } catch (NumberFormatException e) {
                    ToastMessage.showError(mainActivity, TEXT_NOT_RIGHT_BARCODE, Toast.LENGTH_SHORT);
                    return;
                }

                if (isbn.length() != 13 || EAN_13.equals(format) == false) {
                    ToastMessage.showError(mainActivity, TEXT_NOT_RIGHT_BARCODE, Toast.LENGTH_SHORT);
                    return;
                }

                executeIsbnAction(isbn);

            } else if (resultCode == RESULT_CANCELED) {
                ToastMessage.showError(mainActivity, TEXT_NOT_RIGHT_BARCODE, Toast.LENGTH_SHORT);
            }
        }
    }

    private void executeIsbnAction(final String isbn) {
        IsbnReadTask readTask = new IsbnReadTask(new ResponseListener() {
            @Override
            public void postResponse(BookInfo bookInfo) {

                Intent isbnIntent = new Intent(mainActivity, BookDetailEditActivity.class);

                bookInfo.setIsbn(isbn);

                isbnIntent.putExtra(IntentExtraEntry.MODE, InputMode.ISBN_INPUT);
                isbnIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "등록");
                isbnIntent.putExtra(IntentExtraEntry.BOOK_INFO, bookInfo);

                startActivity(isbnIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        readTask.execute(Long.valueOf(isbn), null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setQueryHint("제목, 청구기호, 등록번호 검색");

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        getActionBar().setTitle("도서 목록");
        return true;
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            ToastMessage.showDefault(this, "\"뒤로\" 버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            backButtonCount++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                handleAddOptions(id);
                break;
            case R.id.refresh_data:
                refresh();
                break;
            case R.id.server_config:
                openServerConfigDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleAddOptions(int id) {

        OptionDialog optionDialog = new OptionDialog(this);

        optionDialog.setListener(new OptionDialogListener() {
            @Override
            public void setSelectedValue(RegisterOption option) {
                switch (option) {
                    case BARCODE:
                        // QR code 인식을 위한 화면 호출
                        Intent barCodeIntent = new Intent("com.google.zxing.client.android.SCAN");
                        barCodeIntent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                        startActivityForResult(barCodeIntent, 0);
                        break;

                    case ISBN:
                        ISBNDialog isbnDialog = new ISBNDialog(mainActivity);
                        isbnDialog.setListener(new ISBNDialogListener() {
                            @Override
                            public void setSelectedValue(String isbn) {
                                executeIsbnAction(isbn);
                            }
                        });
                        isbnDialog.show();
                        break;

                    case SELF:
                        ToastMessage.showDefault(getApplicationContext(), "직접입력 선택", Toast.LENGTH_SHORT);
                        Intent selfIntent = new Intent(mainActivity, BookDetailEditActivity.class);
                        selfIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "등록");
                        selfIntent.putExtra(IntentExtraEntry.MODE, InputMode.SELF);

                        startActivity(selfIntent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;

                    default: //do nothing.
                }
            }
        });
        optionDialog.show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        BookInfo selectedBook = books.get(position);

        Intent detailIntent = new Intent(mainActivity, BookDetailViewActivity.class);

        detailIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "정보");

        detailIntent.putExtra(IntentExtraEntry.MODE, InputMode.VIEW);
        detailIntent.putExtra(IntentExtraEntry.BOOK_INFO, selectedBook);

        startActivity(detailIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

}
