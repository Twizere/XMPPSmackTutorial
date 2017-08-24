package thirdrdhand.smacktutorial.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import thirdrdhand.smacktutorial.Model.AbstractModel;
import thirdrdhand.smacktutorial.Model.ReceivedMessage;

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
