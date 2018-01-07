package io.github.daddytrap.adream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.github.daddytrap.adream.model.Comment;
import io.github.daddytrap.adream.model.Music;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.model.User;

/**
 * Created by 74187 on 2018/1/5.
 */

public class ADSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ADream.db";
    private static final String USER_TABLE_NAME = "User";
    private static final String PASSAGE_TABLE_NAME = "Passage";
    private static final String PRAISE_TABLE_NAME = "Praise";
    private static final String COMMENT_TABLE_NAME = "Comment";
    private static final String MUSIC_TABLE_NAME = "Music";
    private static final String RECOMMEND_TABLE_NAME = "Recommend";
    private static final int DB_VERSION = 1;

    private DateFormat dateFormat;

    public ADSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public ADSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableSql = "CREATE TABLE " + USER_TABLE_NAME +
                "(id integer primary key , " +
                "name text , " +
                "avatarBase64 text);";
        db.execSQL(createUserTableSql);

        String createPassageTableSql = "CREATE TABLE " + PASSAGE_TABLE_NAME +
                "(id integer primary key ," +
                "title text," +
                "author text," +
                "content text," +
                "date text," +
                "type text," +
                "avatarBase64 text"+ ");";
        db.execSQL(createPassageTableSql);

        String createCommentTableSql = "CREATE TABLE " + COMMENT_TABLE_NAME +
                "(id integer primary key," +
                "content text," +
                "date text," +
                "passageid integer," +
                "userid integer," +
                "FOREIGN KEY(passageid) REFERENCES " + PASSAGE_TABLE_NAME + "(id)," +
                "FOREIGN KEY(userid) REFERENCES " + USER_TABLE_NAME + "(id));";
        db.execSQL(createCommentTableSql);

        String createPraiseTableSql = "CREATE TABLE " + PRAISE_TABLE_NAME +
                "(id integer primary key," +
                "userid integer," +
                "passageid integer," +
                "FOREIGN KEY(userid) REFERENCES User(id)," +
                "FOREIGN KEY(passageid) REFERENCES Passage(id));";
        db.execSQL(createPraiseTableSql);

        String createMusicTableSql = "CREATE TABLE " + MUSIC_TABLE_NAME +
                "(id integer primary key," +
                "href text," +
                "title text," +
                "lyric text);";
        db.execSQL(createMusicTableSql);

        String createRecommendTableSql = "CREATE TABLE " + RECOMMEND_TABLE_NAME +
                "(id integer primary key," +
                "musicid integer," +
                "date text);";
        db.execSQL(createRecommendTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMiaoBi(Passage passage) {
        SQLiteDatabase db = getWritableDatabase();
        String insertMiaoBiSql = "INSERT INTO " + PASSAGE_TABLE_NAME +
                " VALUES (" + passage.getTitle() +
                ", " + passage.getAuthor() +
                ", " + passage.getContent() +
                ", " + String.valueOf(passage.getDate()) +
                ", miaobi);";
        db.execSQL(insertMiaoBiSql);
    }

    public void insertPraise(int userId, int passageId) {
        SQLiteDatabase db =getWritableDatabase();
        String insertPraiseSql = "INSERT INTO " + PRAISE_TABLE_NAME +
                " VALUES (" + userId +
                ", " + passageId + ");";
        db.execSQL(insertPraiseSql);
    }

    public void insertComment(Comment comment, int passageId) {
        SQLiteDatabase db = getWritableDatabase();
        String insertCommentSql = "INSERT INTO " + COMMENT_TABLE_NAME +
                " VALUES (" + comment.getContent() +
                ", " + String.valueOf(comment.getDate()) +
                ", " + passageId +
                ", " + comment.getUser().getId() + ");";
        db.execSQL(insertCommentSql);
    }

    public void insertRecommend(Date date, int musicId) {
        SQLiteDatabase db = getWritableDatabase();
        // Get date without time
        String dateStr = dateFormat.format(date);
        String insertRecommendSql = "INSERT INTO " + RECOMMEND_TABLE_NAME +
                " VALUES (" + musicId + ", " + dateStr + ");";
        db.execSQL(insertRecommendSql);
    }

    public List<Comment> getCommentByPassageId(int passageId) {
        List<Comment> result = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT Comment.id as commentid, Comment.content, Comment.date, User.id as userid, User.name, User.avatarBase64 FROM " + COMMENT_TABLE_NAME + ", " + USER_TABLE_NAME + " WHERE passageid = ? AND Comment.userid = User.id;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(passageId)});
        while (cursor.moveToNext()) {
            User user = new User(cursor.getInt(cursor.getColumnIndex("userid")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("avatarBase64")));
            Comment comment = new Comment(cursor.getInt(cursor.getColumnIndex("commentid")), user, cursor.getString(cursor.getColumnIndex("content")), new Date(cursor.getString(cursor.getColumnIndex("date"))));
            result.add(comment);
        }
        cursor.close();
        return result;
    }

    public List<Passage> getPassageByType(String type) {
        List<Passage> result = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + PASSAGE_TABLE_NAME + " WHERE type = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[] {type});
        while (cursor.moveToNext()) {
            Passage passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), new Date(cursor.getString(cursor.getColumnIndex("date"))), cursor.getString(cursor.getColumnIndex("avatarBase64")));
            result.add(passage);
        }
        cursor.close();
        return result;
    }

    public Passage getPassageById(int passageId) {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + PASSAGE_TABLE_NAME + " WHERE id = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(passageId)});
        if (cursor.moveToFirst()) {
            Passage passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), new Date(cursor.getString(cursor.getColumnIndex("date"))), cursor.getString(cursor.getColumnIndex("avatarBase64")));
            cursor.close();
            return passage;
        } else {
            return null;
        }
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + USER_TABLE_NAME + " WHERE id = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("avatarBase64")));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }

    public Music getMusicByDate(Date date) {
        SQLiteDatabase db = getReadableDatabase();
        // Get date without time
        String dateStr = dateFormat.format(date);
        String querySql = "SELECT * FROM " + MUSIC_TABLE_NAME + ", " + RECOMMEND_TABLE_NAME + " WHERE date = ? AND Recommend.musicid = Music.id;";
        Cursor cursor = db.rawQuery(querySql, new String[] {dateStr});
        if (cursor.moveToFirst()) {
            Music music = new Music(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("href")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("lyric")));
            cursor.close();
            return music;
        } else {
            return null;
        }
    }

    public List<Music> getMusics() {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + MUSIC_TABLE_NAME;
        Cursor cursor = db.rawQuery(querySql, null);
        List<Music> ret = new LinkedList<>();
        if (cursor.moveToFirst()) {
            do {
                ret.add(new Music(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("href")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("lyric"))));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return ret;
    }

    public List<Passage> getPraisedPassageByUserId(int userId) {
        List<Passage> result = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT Passage.id, Passage.title, Passage.author, Passage.content, Passage.date, Passage.avatarBase64 FROM " + PASSAGE_TABLE_NAME + ", " + PRAISE_TABLE_NAME + " WHERE Praise.userid = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(userId)});
        while (cursor.moveToNext()) {
            Passage passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), new Date(cursor.getString(cursor.getColumnIndex("date"))), cursor.getString(cursor.getColumnIndex("avatarBase64")));
            result.add(passage);
        }
        cursor.close();
        return result;
    }
}
