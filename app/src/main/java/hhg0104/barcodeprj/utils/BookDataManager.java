package hhg0104.barcodeprj.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hhg0104.barcodeprj.model.BookInfo;

/**
 * Created by HGHAN on 2015-10-03.
 */
public class BookDataManager {

    private static BookDataManager instance;

    private List<BookInfo> bookData = new ArrayList<BookInfo>();

    public static BookDataManager getInstance() {
        if (instance == null) {
            instance = new BookDataManager();
        }

        return instance;
    }

    private BookDataManager() {
    }

    public List<BookInfo> getSearchedData(String keyword){
        if (keyword == null || keyword.isEmpty()) {
            return Collections.emptyList();
        }

        keyword = keyword.toLowerCase();

        List<BookInfo> searchedBookData = new ArrayList<BookInfo>();

        for (BookInfo bookInfo : bookData) {
            String callNo = bookInfo.getCallNo();
            String regNo = bookInfo.getRegNo();
            String title = bookInfo.getBookTitle();

            if(callNo == null || callNo.isEmpty()){
                continue;
            }
            if(regNo == null || regNo.isEmpty()){
                continue;
            }
            if(title == null || title.isEmpty()){
                continue;
            }

            callNo = callNo.toLowerCase();
            regNo = regNo.toLowerCase();
            title = title.toLowerCase();

            if(title.contains(keyword) || callNo.equals(keyword) || regNo.equals(keyword)){
                searchedBookData.add(bookInfo);
            }
        }
        return searchedBookData;
    }

    public void save(List<BookInfo> bookData) {
        if (bookData != null) {
            this.bookData.clear();
            this.bookData.addAll(bookData);
            Collections.sort(bookData);
        }
    }

    public List<BookInfo> getData(){
        return bookData;
    }

    public void add(BookInfo bookData) {
        if (bookData != null) {
            this.bookData.add(bookData);
        }
    }

    public BookInfo get(int index) {
        return bookData.get(index);
    }

    public BookInfo get(String bookID) {

        if (bookID == null || bookID.isEmpty()) {
            return new BookInfo();
        }

        for (BookInfo bookInfo : bookData) {
            String id = bookInfo.getBookID();

            if (id == null || id.isEmpty()) {
                return new BookInfo();
            }

            if (bookID.equals(id)) {
                return bookInfo;
            }
        }

        return new BookInfo();
    }
}
