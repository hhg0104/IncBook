package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String TEXT_NOT_RIGHT_BARCODE = "Not valid barcode.";
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
            ToastMessage.showError(this, "You need to config server info first.", Toast.LENGTH_LONG);

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
        }, getApplicationContext());

        conn.execute(HttpAction.GET_BOOKS);
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

        ToastMessage.showDefault(mainActivity, "Refreshed.", Toast.LENGTH_SHORT);
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

                Exception error = bookInfo.getError();
                if(bookInfo.getError() != null) {
                    ToastMessage.showError(mainActivity, "[ISBN search connection error]" + error.getMessage(), Toast.LENGTH_LONG);
                    return;
                }

                if(bookInfo.isEmpty()) {
                    String messageFormat = "Couldn't find the book info from the ISBN [%s]";
                    ToastMessage.showError(mainActivity, String.format(messageFormat, isbn), Toast.LENGTH_LONG);
                    return;
                }

                Intent isbnIntent = new Intent(mainActivity, BookDetailEditActivity.class);

                bookInfo.setIsbn(isbn);

                isbnIntent.putExtra(IntentExtraEntry.MODE, InputMode.ISBN_INPUT);
                isbnIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "Add");
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

        searchView.setQueryHint("search...");

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        getActionBar().setTitle("Book List");
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
            ToastMessage.showDefault(this, "Push again \"back button\" will close the app.", Toast.LENGTH_SHORT);
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
                        ISBNDialog isbnDialog = new ISBNDialog(mainActivity, books);
                        isbnDialog.setListener(new ISBNDialogListener() {
                            @Override
                            public void setSelectedValue(String isbn) {
                                executeIsbnAction(isbn);
                            }
                        });
                        isbnDialog.show();
                        break;

                    case SELF:
                        Intent selfIntent = new Intent(mainActivity, BookDetailEditActivity.class);
                        selfIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "Add");
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

        detailIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "Info");

        detailIntent.putExtra(IntentExtraEntry.MODE, InputMode.VIEW);
        detailIntent.putExtra(IntentExtraEntry.BOOK_INFO, selectedBook);

        startActivity(detailIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

//    private List<BookInfo> getTestBookData() {
//        List<BookInfo> books = new ArrayList<BookInfo>();
//        books.add(new BookInfo("일탈", "게일 루빈", "현실문화연구", "당신이 무지했던 성적 자유의 영역을 놀랍게도 확장시킬 역저성 인류학의 선구자 게일루빈이...", "창고", "http://bookthumb.phinf.naver.net/cover/001/296/00129645.jpg?type=m1&udate=20130629"));
//        books.add(new BookInfo("나의 문화유산답사기", "유홍준", "창비", "서울에서 2시간, 가을에 만나는 남한강의 매력!", "연구실", "http://bookthumb.phinf.naver.net/cover/001/296/00129645.jpg?type=m1&udate=20130629"));
//        books.add(new BookInfo("청진기가 사라진 이후", "에릭 토폴", "청년의사", "\"의료의 미래가 당신의 손 안에 들어와 있다.\"", "휴게실", StringConstants.EMPTY));
//        books.add(new BookInfo("사랑을 더 풍성하게 하라", "화종부", "두란노", "예수를 믿고 달라진 게 아무것도 없어도 확실할 수 있는 것은...", "회의실", "http://bookthumb.phinf.naver.net/cover/001/296/00129645.jpg?type=m1&udate=20130629"));
//        books.add(new BookInfo("성격심리학", "김완일,김옥란", "학지사", "이 책은 성격의 대표적인 이록들이 출현하게 된 배경을 이해하는데 초점을 두었으며...", "창고", "http://bookthumb.phinf.naver.net/cover/001/296/00129645.jpg?type=m1&udate=20130629"));
//        books.add(new BookInfo("현실에의 철학적 접근", "김형효", "소나무", "서양과 동양과 한국, 고대와 중세와 현대를 넘아들었던 그의 철학 순례길을 따라가다 보면...", "연구실", StringConstants.EMPTY));
//        books.add(new BookInfo("골목길 근대사", "최석호,박종인,이길용", "시루", "골목길에서 만나는 근대사는 어떤 모습으로 변해 있을까?", "회의실", "http://bookthumb.phinf.naver.net/cover/001/296/00129645.jpg?type=m1&udate=20130629"));
//        books.add(new BookInfo("직장인 팔로워십", "김해원", "책과나무", "어떻게 팔로워십을 발휘하는 것이 가장 이상적인가?", "휴게실", "http://bookthumb.phinf.naver.net/cover/001/296/00129645.jpg?type=m1&udate=20130629"));
//        books.add(new BookInfo("꼬마 영화감독 샬롯", "프랭크 비바", "주니어RHK", "너만의 예술적 재능을 마음껏 펼쳐보렴!", "한형근", "http://bookthumb.phinf.naver.net/cover/001/296/00129645.jpg?type=m1&udate=20130629"));
//        books.add(new BookInfo("그래픽디자이너들", "유정미", "홍디자인", "엘 리시츠키부터 데이비드 카슨까지, 세기의 디자이너 20인의 발자취를 쫓다.", "연구실", StringConstants.EMPTY));
//
//        return books;
//    }
}
