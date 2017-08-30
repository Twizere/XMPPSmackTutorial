package thirdrdhand.smacktutorial.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import thirdrdhand.smacktutorial.Model.AbstractModel;
import thirdrdhand.smacktutorial.Model.ReceivedMessage;
import thirdrdhand.smacktutorial.activities.transactions.Transaction;

/**
 * Created by pacit on 2017/08/24.
 */

public class BackEndDB {
    private static final String TAG = "RECEIVED_MSG_STORAGE";
    private static final DatabaseErrorHandler errorHandler = new DatabaseErrorHandler() {
        @Override
        public void onCorruption(SQLiteDatabase sqLiteDatabase) {
            Log.e(TAG, "Database Corrupted");
        }
    };
    private static BackEndDB instance = null;
    private static SQLiteDatabase db;
    private SqlLiteHelper helper;


    public BackEndDB(Context context) {

        helper = new SqlLiteHelper(context);
        db = helper.getWritableDatabase();


    }

    public static BackEndDB getInstance(Context context) {
        if (instance == null) {
            instance = new BackEndDB(context);
        }
        return instance;
    }

    public void closeDb() {
        db.close();
    }


    //Saving Received Messages
    public void save(AbstractModel receivedMessage) {
        ContentValues vals = receivedMessage.getContentValues();
        String table = receivedMessage.getTableName();

        db.insert(table, null, vals);
    }

    public void updateMsg(ReceivedMessage msg) {
        ContentValues vals = msg.getContentValues();
        String table = msg.getTableName();
        db.update(table, vals, "MSG_ID = ? and TIME_STAMP = ?", new String[]{msg.msgId, msg.TimeStamp + ""});
    }

    public List<Transaction> getTransactionList() {

        List<Transaction> transList = new ArrayList<Transaction>();
        Transaction trans = new Transaction();

        //   String sql="SELECT * FROM "+ReceivedMessage.TABLE_NAME+
        //           "WHERE isDelete=0";
        Cursor cursor = db.query(ReceivedMessage.TABLE_NAME,
                new String[]{"id", ReceivedMessage.MSG_ID,
                        ReceivedMessage.USERNAME,
                        ReceivedMessage.MSG_TYPE,
                        ReceivedMessage.MSG_STATUS}, ReceivedMessage.IS_DELETE + " =? ", new String[]{"0"}, null, null, null);

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                trans = new Transaction();
                trans.setId(cursor.getString(cursor.getColumnIndex("id")));
                trans.setTitle(cursor.getString(cursor.getColumnIndex(ReceivedMessage.USERNAME)));
                trans.setType(cursor.getString(cursor.getColumnIndex(ReceivedMessage.MSG_TYPE)));
                trans.setStatatus(cursor.getString(cursor.getColumnIndex(ReceivedMessage.MSG_STATUS)));
                transList.add(trans);
            }
        }
        return transList;
    }


    private class SqlLiteHelper extends SQLiteOpenHelper {

        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "receivedMsgDb";

        public SqlLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            String CREATE_RECEIVED_MSG_TABLE = ReceivedMessage.getCreateSql();
            db.execSQL(CREATE_RECEIVED_MSG_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }


        private void dropTable(SQLiteDatabase db) {
            String sql = "DROP TABLE IF EXISTS " + DB_NAME;
            db.execSQL(sql);
        }

    }


}
