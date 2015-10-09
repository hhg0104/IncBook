package hhg0104.barcodeprj.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import hhg0104.barcodeprj.utils.StringConstants;

/**
 * Created by HGHAN on 2015-09-24.
 */
public class BookInfo implements Parcelable, Comparable {

    private String callNo = StringConstants.EMPTY;

    private String regNo = StringConstants.EMPTY;

    private String bookID = StringConstants.EMPTY;

    private String bookTitle = StringConstants.EMPTY;

    private String author = StringConstants.EMPTY;

    private String publisher = StringConstants.EMPTY;

    private String description = StringConstants.EMPTY;

    private String originLocation = StringConstants.EMPTY;

    private String currentLocation = StringConstants.EMPTY;

    private String imagePath = StringConstants.EMPTY;

    private String isbn = StringConstants.EMPTY;

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public static BookInfo fromJson(String bookJson) {
        return new Gson().fromJson(bookJson, BookInfo.class);
    }

    public BookInfo() {
    }

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public BookInfo(String callNo, String regNo, String title, String author, String publisher, String description, String originLocation, String currentLocation, String imagePath, String isbn) {

        setCallNo(callNo);
        setRegNo(regNo);
        setBookTitle(title);
        setAuthor(author);
        setPublisher(publisher);
        setDescription(description);
        setOriginLocation(originLocation);
        setCurrentLocation(currentLocation);
        setImagePath(imagePath);
        setIsbn(isbn);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        if (imagePath != null) {
            this.imagePath = imagePath;
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookID);
        dest.writeString(callNo);
        dest.writeString(regNo);
        dest.writeString(bookTitle);
        dest.writeString(author);
        dest.writeString(publisher);
        dest.writeString(description);
        dest.writeString(originLocation);
        dest.writeString(currentLocation);
        dest.writeString(imagePath);
        dest.writeString(isbn);
    }

    public static final Parcelable.Creator<BookInfo> CREATOR = new Parcelable.Creator<BookInfo>() {

        @Override
        public BookInfo createFromParcel(Parcel source) {

            BookInfo bookInfo = new BookInfo();

            bookInfo.setBookID(source.readString());
            bookInfo.setCallNo(source.readString());
            bookInfo.setRegNo(source.readString());
            bookInfo.setBookTitle(source.readString());
            bookInfo.setAuthor(source.readString());
            bookInfo.setPublisher(source.readString());
            bookInfo.setDescription(source.readString());
            bookInfo.setOriginLocation(source.readString());
            bookInfo.setCurrentLocation(source.readString());
            bookInfo.setImagePath(source.readString());
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

        if ((another instanceof BookInfo) == false) {
            return 0;
        }

        BookInfo otherInfo = (BookInfo) another;

        return this.bookTitle.compareTo(otherInfo.getBookTitle());
    }
}
