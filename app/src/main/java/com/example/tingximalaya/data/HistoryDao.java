package com.example.tingximalaya.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tingximalaya.base.BaseApplication;
import com.example.tingximalaya.interfaces.IHistoryDao;
import com.example.tingximalaya.interfaces.IHistoryDaoCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryDao implements IHistoryDao {

    private static final String TAG = "HistoryDao";
    private final XimalayaDbHelper mDbHelper;
    private IHistoryDaoCallBack mCallback = null;
    private Object mLock = new Object();

    public HistoryDao() {
        mDbHelper = new XimalayaDbHelper(BaseApplication.getAppContext());
    }



    @Override
    public void addHistory(Track track) {
        synchronized(mLock) {
            SQLiteDatabase db = null;
            boolean isSuccess = false;
            try {
                db = mDbHelper.getWritableDatabase();
                //先去删除
                int delResult = db.delete(Constants.HISTORY_TB_NAME,Constants.HISTORY_TRACK_ID + "=?",new String[]{track.getDataId() + ""});
                //删除以后再添加
                db.beginTransaction();
                ContentValues values = new ContentValues();
                //封装数据
                values.put(Constants.HISTORY_TRACK_ID,track.getDataId());
                values.put(Constants.HISTORY_TITLE,track.getTrackTitle());
                values.put(Constants.HISTORY_PLAY_COUNT,track.getPlayCount());
                values.put(Constants.HISTORY_DURATION,track.getDuration());
                values.put(Constants.HISTORY_UPDATE_TIME,track.getUpdatedAt());
                values.put(Constants.HISTORY_COVER,track.getCoverUrlLarge());
                values.put(Constants.HISTORY_AUTHOR,track.getAnnouncer().getNickname());
                //插入数据
                db.insert(Constants.HISTORY_TB_NAME,null,values);
                db.setTransactionSuccessful();
                isSuccess = true;
            } catch(Exception e) {
                isSuccess = false;
                e.printStackTrace();
            } finally {
                if(db != null) {
                    db.endTransaction();
                    db.close();
                }
                if(mCallback != null) {
                    mCallback.onHistoryAdd(isSuccess);
                }
            }
        }
    }

    @Override
    public void delHistory(Track track) {
        synchronized(mLock) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();
                int delete = db.delete(Constants.HISTORY_TB_NAME,Constants.HISTORY_TRACK_ID + "=?",new String[]{track.getDataId() + ""});

                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch(Exception e) {
                e.printStackTrace();
                isDeleteSuccess = false;
            } finally {
                if(db != null) {
                    db.endTransaction();
                    db.close();
                }
                if(mCallback != null) {
                    mCallback.onHistoryDel(isDeleteSuccess);
                }
            }
        }
    }

    @Override
    public void clearHistory() {
        synchronized(mLock) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();
                db.delete(Constants.HISTORY_TB_NAME,null,null);
                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch(Exception e) {
                e.printStackTrace();
                isDeleteSuccess = false;
            } finally {
                if(db != null) {
                    db.endTransaction();
                    db.close();
                }
                if(mCallback != null) {
                    mCallback.onHistoryClean(isDeleteSuccess);

                }
            }
        }
    }

    @Override
    public void listHistories() {
        synchronized(mLock) {
            //从数据表中查出所有的历史记录
            SQLiteDatabase db = null;
            List<Track> histories = new ArrayList<>();
            try {
                db = mDbHelper.getReadableDatabase();
                db.beginTransaction();
                Cursor cursor = db.query(Constants.HISTORY_TB_NAME,null,null,null,null,null,"_id desc");
                while(cursor.moveToNext()) {
                    Track track = new Track();
                    int trackId = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_TRACK_ID));
                    track.setDataId(trackId);
                    String title = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_TITLE));
                    track.setTrackTitle(title);
                    int playCount = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_PLAY_COUNT));
                    track.setPlayCount(playCount);
                    int duration = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_DURATION));
                    track.setDuration(duration);
                    long updateTime = cursor.getLong(cursor.getColumnIndex(Constants.HISTORY_UPDATE_TIME));
                    track.setUpdatedAt(updateTime);
                    String cover = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_COVER));
                    track.setCoverUrlLarge(cover);
                    track.setCoverUrlSmall(cover);
                    track.setCoverUrlMiddle(cover);
                    String author = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_AUTHOR));
                    Announcer announcer = new Announcer();
                    announcer.setNickname(author);
                    track.setAnnouncer(announcer);
                    histories.add(track);
                }
                db.setTransactionSuccessful();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(db != null) {
                    db.endTransaction();
                    db.close();
                }
                //通知出去
                if(mCallback != null) {
                    mCallback.onHistoryLoaded(histories);
                }
            }
        }
    }

    @Override
    public void CallBack(@NotNull IHistoryDaoCallBack callback) {
        this.mCallback = callback;

    }
}
