package hhg0104.barcodeprj.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.utils.DrawableManager;

/**
 * Created by HGHAN on 2015-09-09.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;

    List<BookInfo> books;

    public CustomAdapter(Context context, List<BookInfo> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int i) {

        return books.get(i);
    }

    @Override
    public long getItemId(int i) {

        return books.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        view = mInflater.inflate(R.layout.book_element, null);
        holder = new ViewHolder();

        holder.title = (TextView) view.findViewById(R.id.book_element_title);
        holder.author = (TextView) view.findViewById(R.id.book_element_author);
        holder.publisher = (TextView) view.findViewById(R.id.book_element_publisher);
        holder.location = (TextView) view.findViewById(R.id.book_element_location);
        holder.image = (ImageView) view.findViewById(R.id.book_element_image);

        BookInfo book = books.get(i);

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.publisher.setText(book.getPublisher());
        holder.location.setText(book.getLocation());

        DrawableManager imgManager = DrawableManager.getInstance();
        imgManager.fetchDrawableOnThread(book.getImagePath(), holder.image);

        view.setTag(holder);

        return view;
    }

    private class ViewHolder {
        TextView title;
        TextView author;
        TextView publisher;
        TextView location;
        ImageView image;
    }
}
