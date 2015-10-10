package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.adapter.CustomAdapter;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.utils.BookDataManager;
import hhg0104.barcodeprj.utils.InputMode;
import hhg0104.barcodeprj.utils.IntentExtraEntry;

/**
 * Created by HGHAN on 2015-09-20.
 */
public class SearchResultActivity extends Activity implements AdapterView.OnItemClickListener {

    private List<BookInfo> searchedBooks = new ArrayList<BookInfo>();

    private Activity searchActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        searchActivity = this;

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            final ListView listView = (ListView) findViewById(R.id.searched_list);
            listView.setOnItemClickListener(this);

            String query = intent.getStringExtra(IntentExtraEntry.QUERY);

            searchedBooks.clear();
            searchedBooks.addAll(BookDataManager.getInstance().getSearchedData(query));

            CustomAdapter adapter = new CustomAdapter(this, searchedBooks);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu_main, menu);

        getActionBar().setTitle("검색 결과");

        return true;
    }

    private List<BookInfo> getTestBookData() {
        List<BookInfo> books = new ArrayList<BookInfo>();

        return books;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        BookInfo selectedBook = searchedBooks.get(position);

        Intent detailIntent = new Intent(searchActivity, BookDetailViewActivity.class);

        detailIntent.putExtra(IntentExtraEntry.TITLE_KEYWORD, "정보");
        detailIntent.putExtra(IntentExtraEntry.MODE, InputMode.VIEW);
        detailIntent.putExtra(IntentExtraEntry.BOOK_INFO, selectedBook);

        startActivity(detailIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
