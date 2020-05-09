package com.monk.reader.constant;


import androidx.annotation.StringDef;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Constant {
    String CHANNEL = "channel";
    String BOOK_NAME = "book_name";
    String BOOK_INFO = "book_info";
    String BOOK_CHAPTER = "book_chapter";
    String CURRENT_CHAPTER = "current_chapter";
    String BOOK_ID = "book_id";

    String ISNIGHT = "isNight";

    String SUFFIX_TXT = ".txt";
    String SUFFIX_PDF = ".pdf";
    String SUFFIX_EPUB = ".epub";
    String SUFFIX_ZIP = ".zip";
    String SUFFIX_CHM = ".chm";

    String BOOK_SHELF_SORT_KEY = "BOOK_SHELF_SORT_KEY";

    @StringDef({
            Gender.MALE,
            Gender.FEMALE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Gender {
        String MALE = "male";

        String FEMALE = "female";
    }
}
