package hhg0104.barcodeprj.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import hhg0104.barcodeprj.utils.StringConstants;

/**
 * Created by HGHAN on 2015-09-24.
 */
public class BookInfo implements Parcelable, Comparable {

    private String id = StringConstants.EMPTY;

    private String title = StringConstants.EMPTY;

    private String author = StringConstants.EMPTY;

    private String publisher = StringConstants.EMPTY;

    private String description = StringConstants.EMPTY;

    private String location = StringConstants.EMPTY;

    private String imageUrl = StringConstants.EMPTY;

    private String isbn = StringConstants.EMPTY;

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static BookInfo fromJson(String bookJson){
        return new Gson().fromJson(bookJson, BookInfo.class);
    }

    public BookInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookInfo(String title, String author, String publisher, String description, String location, String imageUrl, String isbn) {

        setTitle(title);
        setAuthor(author);
        setPublisher(publisher);
        setDescription(description);
        setLocation(location);
        setImageUrl(imageUrl);
        setIsbn(isbn);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author != null) {
            this.author = author;
        }
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        if (publisher != null) {
            this.publisher = publisher;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(publisher);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(imageUrl);
        dest.writeString(isbn);
    }

    public static final Parcelable.Creator<BookInfo> CREATOR = new Parcelable.Creator<BookInfo>() {

        @Override
        public BookInfo createFromParcel(Parcel source) {

            BookInfo bookInfo = new BookInfo();

            bookInfo.setId(source.readString());
            bookInfo.setTitle(source.readString());
            bookInfo.setAuthor(source.readString());
            bookInfo.setPublisher(source.readString());
            bookInfo.setDescription(source.readString());
            bookInfo.setLocation(source.readString());
            bookInfo.setImageUrl(source.readString());
            bookInfo.setIsbn(source.readString());
            return bookInfo;
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };

    @Override
    public int compareTo(Object another) {

        if((another instanceof BookInfo) == false){
            return 0;
        }

        BookInfo otherInfo = (BookInfo) another;

        return this.title.compareTo(otherInfo.getTitle());
    }
}
