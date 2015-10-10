package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.connector.ServerConnector;
import hhg0104.barcodeprj.listener.HttpListener;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.model.ResponseModel;
import hhg0104.barcodeprj.utils.Action;
import hhg0104.barcodeprj.utils.DrawableManager;
import hhg0104.barcodeprj.utils.HttpAction;
import hhg0104.barcodeprj.utils.InputMode;
import hhg0104.barcodeprj.utils.IntentExtraEntry;
import hhg0104.barcodeprj.utils.StringConstants;
import hhg0104.barcodeprj.utils.ToastMessage;

/**
 * Created by HGHAN on 2015-09-14.
 */
public class BookDetailEditActivity extends Activity {

    private Activity thisActivity;

    private String activityTitle = StringConstants.EMPTY;

    private String isbn = StringConstants.EMPTY;

    private String imagePath = StringConstants.EMPTY;

    private String mode = StringConstants.EMPTY;

    private String bookID = StringConstants.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_book_info);

        thisActivity = this;

        EditText callNoView = (EditText) findViewById(R.id.call_no);
        EditText regNoView = (EditText) findViewById(R.id.reg_no);
        EditText titleView = (EditText) findViewById(R.id.book_title);
        EditText authorView = (EditText) findViewById(R.id.book_author);
        EditText publisherView = (EditText) findViewById(R.id.book_publisher);
        EditText descView = (EditText) findViewById(R.id.book_description);
        EditText originLocationView = (EditText) findViewById(R.id.book_origin_location);
        EditText currentLocationView = (EditText) findViewById(R.id.book_current_location);

        // 기본 설정을 한다. 키보드 엔터 기능 제거, EditText 필드 Enable
        configDefaultSetting(callNoView, regNoView, titleView, authorView, publisherView, descView, originLocationView, currentLocationView);


        // Barcode, ISBN, 기본 정보 수정 시, 필요한 정보를 미리 입력한다.
        setValueToEditTextField(callNoView, regNoView, titleView, authorView, publisherView, descView, originLocationView, currentLocationView);
    }

    private void setValueToEditTextField(EditText callNoView, EditText regNoView, EditText titleView, EditText authorView, EditText publisherView, EditText descView, EditText originLocationView, EditText currentLocationView) {

        Intent detailIntent = getIntent();
        Bundle extras = detailIntent.getExtras();

        activityTitle = extras.getString(IntentExtraEntry.TITLE_KEYWORD);

        mode = extras.getString(IntentExtraEntry.MODE);

        if (InputMode.SELF.equals(mode)) {
            return;
        }

        if (InputMode.ISBN_INPUT.equals(mode) || InputMode.VIEW.equals(mode)) {

            BookInfo bookInfo = extras.getParcelable(IntentExtraEntry.BOOK_INFO);

            callNoView.setText(bookInfo.getCallNo());
            regNoView.setText(bookInfo.getRegNo());
            titleView.setText(bookInfo.getBookTitle());
            authorView.setText(bookInfo.getAuthor());
            publisherView.setText(bookInfo.getPublisher());
            descView.setText(bookInfo.getDescription());
            originLocationView.setText(bookInfo.getOriginLocation());
            currentLocationView.setText(bookInfo.getCurrentLocation());

            isbn = bookInfo.getIsbn();
            imagePath = bookInfo.getImagePath();
            bookID = bookInfo.getBookID();

            loadImage(bookInfo.getImagePath(), (ImageView) findViewById(R.id.book_image));
        }
    }

    private void loadImage(String imagePath, ImageView imageView) {

        DrawableManager imgManager = DrawableManager.getInstance();
        imgManager.fetchDrawableOnThread(imagePath, imageView);
    }

    private void configDefaultSetting(EditText callNoView, EditText regNoView, EditText titleView, EditText authorView, EditText publisherView, EditText descView, EditText originLocationView, EditText currentLocationView) {
        List<EditText> editTexts = new ArrayList<EditText>();

        editTexts.add(callNoView);
        editTexts.add(regNoView);
        editTexts.add(titleView);
        editTexts.add(authorView);
        editTexts.add(publisherView);
        editTexts.add(descView);
        editTexts.add(originLocationView);
        editTexts.add(currentLocationView);

        //키보드 엔터 기능 제거
        setKeyboardEnterDoNothing(editTexts);

        //EditText 필드 Enable
        setEditTextEnable(editTexts);
    }

    private void setKeyboardEnterDoNothing(List<EditText> editTexts) {
        for (final EditText editText : editTexts) {
            editText.setOnKeyListener(new View.OnKeyListener() {
                /**
                 * This listens for the user to press the enter button on
                 * the keyboard and then hides the virtual keyboard
                 */
                public boolean onKey(View arg0, int arg1, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (arg1 == KeyEvent.KEYCODE_ENTER)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void setEditTextEnable(List<EditText> editTexts) {

        for (EditText editText : editTexts) {
            editText.setEnabled(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
            editText.setCursorVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book_detail_edit, menu);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(activityTitle);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.book_edit_ok:

                String callNo = ((EditText) findViewById(R.id.call_no)).getText().toString();
                String regNo = ((EditText) findViewById(R.id.reg_no)).getText().toString();
                String title = ((EditText) findViewById(R.id.book_title)).getText().toString();
                String author = ((EditText) findViewById(R.id.book_author)).getText().toString();
                String publisher = ((EditText) findViewById(R.id.book_publisher)).getText().toString();
                String desc = ((EditText) findViewById(R.id.book_description)).getText().toString();
                String originLocation = ((EditText) findViewById(R.id.book_origin_location)).getText().toString();
                String currentLocation = ((EditText) findViewById(R.id.book_current_location)).getText().toString();

                if (callNo.isEmpty() || regNo.isEmpty() || title.isEmpty() || originLocation.isEmpty()) {
                    ToastMessage.showError(getApplicationContext(), "청구기호, 등록번호, 제목, 원래 위치는 필수 정보입니다.", Toast.LENGTH_LONG);
                    break;
                }

                ServerConnector conn = new ServerConnector(new HttpListener() {
                    @Override
                    public void getResult(ResponseModel response) {

                        if (response.getStatusCode() != 200) {
                            ToastMessage.showError(getApplicationContext(), response.getData(), Toast.LENGTH_LONG);
                            return;
                        }

                        if (InputMode.ISBN_INPUT.equals(mode) || InputMode.SELF.equals(mode)) {
                            ToastMessage.showDefault(getApplicationContext(), "책 정보 추가 완료", Toast.LENGTH_SHORT);
                        } else if (InputMode.VIEW.equals(mode)) {
                            ToastMessage.showDefault(getApplicationContext(), "책 정보 수정 완료", Toast.LENGTH_SHORT);
                        }

                        Intent mainIntent = new Intent(thisActivity, MainActivity.class);
                        mainIntent.putExtra(IntentExtraEntry.ACTION, Action.REFRESH);

                        startActivity(mainIntent);
                    }
                }, this);

                BookInfo bookInfo = new BookInfo(callNo, regNo,title, author, publisher, desc, originLocation, currentLocation, imagePath, isbn);

                if (InputMode.ISBN_INPUT.equals(mode) || InputMode.SELF.equals(mode)) {
                    conn.setHttpInfo(null, bookInfo.toJson(), null);
                    conn.execute(HttpAction.ADD_BOOK);
                } else if (InputMode.VIEW.equals(mode)) {
                    conn.setHttpInfo(bookID, bookInfo.toJson(), null);
                    conn.execute(HttpAction.EDIT_BOOK);
                }

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
        return super.onOptionsItemSelected(item);
    }
}
