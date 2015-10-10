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

import java.util.ArrayList;
import java.util.List;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.utils.DrawableManager;
import hhg0104.barcodeprj.utils.InputMode;
import hhg0104.barcodeprj.utils.IntentExtraEntry;

/**
 * Created by HGHAN on 2015-09-14.
 */
public class BookDetailViewActivity extends Activity{

    private BookInfo bookInfo;

    private String activityTitle;

    private Activity thisActivity;

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
        ImageView changePhotoImageView = (ImageView) findViewById(R.id.book_change_image);

        configDefaultSetting(callNoView, regNoView, titleView, authorView, publisherView, descView, originLocationView, currentLocationView, changePhotoImageView);

        Intent detailIntent = getIntent();
        Bundle extras = detailIntent.getExtras();
        bookInfo = extras.getParcelable(IntentExtraEntry.BOOK_INFO);
        activityTitle = extras.getString(IntentExtraEntry.TITLE_KEYWORD);

        callNoView.setText(bookInfo.getCallNo());
        regNoView.setText(bookInfo.getRegNo());
        titleView.setText(bookInfo.getBookTitle());
        authorView.setText(bookInfo.getAuthor());
        publisherView.setText(bookInfo.getPublisher());
        descView.setText(bookInfo.getDescription());
        originLocationView.setText(bookInfo.getOriginLocation());
        currentLocationView.setText(bookInfo.getCurrentLocation());

        loadImage(bookInfo.getImagePath(), (ImageView) findViewById(R.id.book_image));

        setImageClickEvent(bookInfo.getImagePath());
    }

    private void setImageClickEvent(final String imagePath) {

        final ImageView imageView = (ImageView) findViewById(R.id.book_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullSizeIntent = new Intent(thisActivity, ImageFullSizeActivity.class);
                fullSizeIntent.putExtra("imagePath", imagePath);
                startActivity(fullSizeIntent);

            }
        });

    }

    private void loadImage(String imagePath, ImageView imageView) {

        DrawableManager imgManager = DrawableManager.getInstance();
        imgManager.fetchDrawableOnThread(imagePath, imageView);
    }

    private void configDefaultSetting(EditText callNoView, EditText regNoView, EditText titleView, EditText authorView,
                                      EditText publisherView, EditText descView, EditText originLocationView,
                                      EditText currentLocationView, ImageView changePhotoImageView) {
        List<EditText> editTexts = new ArrayList<EditText>();

        editTexts.add(callNoView);
        editTexts.add(regNoView);
        editTexts.add(titleView);
        editTexts.add(authorView);
        editTexts.add(publisherView);
        editTexts.add(descView);
        editTexts.add(originLocationView);
        editTexts.add(currentLocationView);

        changePhotoImageView.setVisibility(ImageView.INVISIBLE);

        setKeyboardEnterDoNothing(editTexts);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.book_detail_view, menu);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(activityTitle);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.book_edit_do:

                Intent editIntent = new Intent(this, BookDetailEditActivity.class);

                editIntent.putExtra(IntentExtraEntry.MODE, InputMode.VIEW);
                editIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "수정");
                editIntent.putExtra(IntentExtraEntry.BOOK_INFO, bookInfo);

                startActivity(editIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
