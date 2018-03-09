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
import hhg0104.barcodeprj.dialog.ConfirmDialog;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.utils.DrawableManager;
import hhg0104.barcodeprj.utils.InputMode;
import hhg0104.barcodeprj.utils.IntentExtraEntry;

/**
 * Created by HGHAN on 2015-09-14.
 */
public class BookDetailViewActivity extends Activity {

    private BookInfo bookInfo;

    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_book_info);

        final EditText titleView = (EditText) findViewById(R.id.book_title);
        EditText authorView = (EditText) findViewById(R.id.book_author);
        EditText publisherView = (EditText) findViewById(R.id.book_publisher);
        EditText descView = (EditText) findViewById(R.id.book_description);
        EditText locationView = (EditText) findViewById(R.id.book_location);

        configDefaultSetting(titleView, authorView, publisherView, descView, locationView);

        Intent detailIntent = getIntent();
        Bundle extras = detailIntent.getExtras();
        bookInfo = extras.getParcelable(IntentExtraEntry.BOOK_INFO);
        activityTitle = extras.getString(IntentExtraEntry.TITLE_KEYWORD);

        titleView.setText(bookInfo.getTitle());
        authorView.setText(bookInfo.getAuthor());
        publisherView.setText(bookInfo.getPublisher());
        descView.setText(bookInfo.getDescription());
        locationView.setText(bookInfo.getLocation());

        loadImage(bookInfo.getImageUrl(), (ImageView) findViewById(R.id.book_image));
    }

    private void loadImage(String imagePath, ImageView imageView) {

        DrawableManager imgManager = DrawableManager.getInstance();
        imgManager.fetchDrawableOnThread(imagePath, imageView);
    }

    private void configDefaultSetting(EditText titleView, EditText authorView, EditText publisherView, EditText descView, EditText locationView) {
        List<EditText> editTexts = new ArrayList<EditText>();

        editTexts.add(titleView);
        editTexts.add(authorView);
        editTexts.add(publisherView);
        editTexts.add(descView);
        editTexts.add(locationView);

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
                editIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "Edit");
                editIntent.putExtra(IntentExtraEntry.BOOK_INFO, bookInfo);

                startActivity(editIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.book_delete_do:

                String bookTitle = bookInfo.getTitle();
                String bookId = bookInfo.getId();
                String messageFormat = getResources().getString(R.string.delete_message_format);
                String message = String.format(messageFormat, bookTitle);

                ConfirmDialog dialog = new ConfirmDialog(this);
                DeleteBookClickListener listener = new DeleteBookClickListener(this, bookId);

                dialog.showConfirmDialog(message, listener);
                break;

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
