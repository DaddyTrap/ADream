package io.github.daddytrap.adream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.github.daddytrap.adream.model.Comment;
import io.github.daddytrap.adream.model.Music;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.model.Qian;
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
    private static final String QIAN_TABLE_NAME = "Qian";
    private static final String DATE_QIAN_TABLE_NAME = "DateQian";
    private static final int DB_VERSION = 1;

    private DateFormat dateFormat;

    public ADSQLiteOpenHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public ADSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMiaoBi(Passage passage) {
        SQLiteDatabase db = getWritableDatabase();
        String insertMiaoBiSql = "INSERT INTO " + PASSAGE_TABLE_NAME +
                " (title, author, content, date, type, avatarBase64)" +
                " VALUES (\"" + passage.getTitle() +
                "\", \"文/" + passage.getAuthor() +
                "\", \"" + passage.getContent() +
                "\", \"" + dateFormat.format(passage.getDate()) +
                "\", \"miaobi\"" +
                ",\"" + passage.getAvatarBase64() + "\");";
        db.execSQL(insertMiaoBiSql);
    }

    public void insertPraise(int userId, int passageId) {
        SQLiteDatabase db = getWritableDatabase();
        String insertPraiseSql = "INSERT INTO " + PRAISE_TABLE_NAME +
                " (userid, passageid) VALUES (" + userId +
                ", " + passageId + ");";
        db.execSQL(insertPraiseSql);
    }

    public void insertDateQian(int qianId, int userId, Date date) {
        SQLiteDatabase db = getWritableDatabase();
        String insertDateQianSql = "INSERT INTO " + DATE_QIAN_TABLE_NAME +
                " (qianid, userid, date) VALUES (" + qianId +
                ", " + userId +
                ", \"" + dateFormat.format(date) + "\");";
        db.execSQL(insertDateQianSql);
    }

    public void deletePraise(int userId, int passageId) {
        SQLiteDatabase db = getWritableDatabase();
        String deletePraiseSql = "DELETE FROM " + PRAISE_TABLE_NAME +
                " WHERE userid = " + userId +
                " AND passageid = " + passageId +
                ";";
        db.execSQL(deletePraiseSql);
    }

    public void insertComment(Comment comment, int passageId) {
        SQLiteDatabase db = getWritableDatabase();
        String insertCommentSql = "INSERT INTO " + COMMENT_TABLE_NAME +
                "(content, date, passageid, userid)" +
                " VALUES (\"" + comment.getContent() +
                "\", \"" + dateFormat.format(comment.getDate()) +
                "\", " + passageId +
                ", " + comment.getUser().getId() + ");";
        db.execSQL(insertCommentSql);
    }

    public void insertRecommend(Date date, int musicId) {
        SQLiteDatabase db = getWritableDatabase();
        // Get date without time
        String dateStr = dateFormat.format(date);
        String insertRecommendSql = "INSERT INTO " + RECOMMEND_TABLE_NAME +
                " (musicid, date) VALUES (" + musicId + ", " + dateStr + ");";
        db.execSQL(insertRecommendSql);
    }

    public List<Comment> getCommentByPassageId(int passageId) {
        List<Comment> result = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT Comment.id as commentid, Comment.content, Comment.date, User.id as userid, User.name, User.avatarBase64 FROM " + COMMENT_TABLE_NAME + ", " + USER_TABLE_NAME + " WHERE passageid = ? AND Comment.userid = User.id;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(passageId)});
        try {
            while (cursor.moveToNext()) {
                User user = new User(cursor.getInt(cursor.getColumnIndex("userid")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("avatarBase64")));
                Comment comment = new Comment(cursor.getInt(cursor.getColumnIndex("commentid")), user, cursor.getString(cursor.getColumnIndex("content")), dateFormat.parse(cursor.getString(cursor.getColumnIndex("date"))));
                result.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        return result;
    }

    public List<Passage> getPassageByType(String type) {
        List<Passage> result = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + PASSAGE_TABLE_NAME + " WHERE type = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[] {type});
        try {
            if (cursor.moveToFirst()) {
                do {
                    Passage passage;
                    if (type.equals("miaobi")) {
                        passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), dateFormat.parse(cursor.getString(cursor.getColumnIndex("date"))), cursor.getString(cursor.getColumnIndex("avatarBase64")));
                    } else {
                        passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), new Date(), cursor.getString(cursor.getColumnIndex("avatarBase64")));
                    }
                    result.add(passage);
                } while (cursor.moveToNext());
            } else {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Passage getPassageById(int passageId) {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + PASSAGE_TABLE_NAME + " WHERE id = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(passageId)});
        Passage passage = null;
        if (cursor.moveToFirst()) {
            try {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                if (type.equals("miaobi")) {
                    passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), dateFormat.parse(cursor.getString(cursor.getColumnIndex("date"))), cursor.getString(cursor.getColumnIndex("avatarBase64")));
                } else {
                    passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), new Date(), cursor.getString(cursor.getColumnIndex("avatarBase64")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            cursor.close();
            return passage;
        } else {
            cursor.close();
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
        String querySql = "SELECT * FROM " + MUSIC_TABLE_NAME + ";";
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

    public List<Passage> getPraisedPassagesByUserId(int userId) {
        List<Passage> result = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT Passage.id, Passage.title, Passage.author, Passage.content, Passage.date, Passage.avatarBase64, Passage.type FROM " + PASSAGE_TABLE_NAME + ", " + PRAISE_TABLE_NAME + " WHERE Praise.userid = ? AND Praise.passageid = Passage.id;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(userId)});
        Passage passage;
        try {
            while (cursor.moveToNext()) {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                if (type.equals("miaobi")) {
                    passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), dateFormat.parse(cursor.getString(cursor.getColumnIndex("date"))), cursor.getString(cursor.getColumnIndex("avatarBase64")));
                } else {
                    passage = new Passage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("author")), cursor.getString(cursor.getColumnIndex("content")), new Date(), cursor.getString(cursor.getColumnIndex("avatarBase64")));
                }
                result.add(passage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        return result;
    }

    public boolean getPraiseByUserIdAndPassageId(int userId, int passageId) {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT COUNT(*) FROM " + PRAISE_TABLE_NAME +
                " WHERE userid = ?" +
                " AND passageid = ?" +
                ";";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(userId), String.valueOf(passageId)});
        if (cursor.moveToFirst()) {
            if (cursor.getInt(0) == 0) {
                cursor.close();
                return false;
            } else {
                cursor.close();
                return true;
            }
        } else {
            cursor.close();
            return false;
        }
    }

    public Qian getQianById(int qianId) {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + QIAN_TABLE_NAME +
                " WHERE id = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(qianId)});
        Qian qian = null;
        if (cursor.moveToFirst()) {
            qian = new Qian(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("content")));
        }
        cursor.close();
        return qian;
    }

    public List<Qian> getQians() {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT * FROM " + QIAN_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(querySql, null);

        List<Qian> ret = new LinkedList<>();

        if (cursor.moveToFirst()) {
            do {
                ret.add(new Qian(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("content"))));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        return ret;
    }

    public Qian getQianByUserIdAndDate(int userId, Date date) {
        SQLiteDatabase db = getReadableDatabase();
        String querySql = "SELECT Qian.id, Qian.title, Qian.content FROM " +
                QIAN_TABLE_NAME + ", " + DATE_QIAN_TABLE_NAME +
                " WHERE DateQian.userid = ? AND DateQian.date = ? AND DateQian.qianid = Qian.id;";
        String dateStr = dateFormat.format(date);
        Cursor cursor = db.rawQuery(querySql, new String[] {String.valueOf(userId), dateStr});
        Qian qian = null;
        if (cursor.moveToFirst()) {
            qian = new Qian(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("content")));
        }
        cursor.close();
        return qian;
    }
}
