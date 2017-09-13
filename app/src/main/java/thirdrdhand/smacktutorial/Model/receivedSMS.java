package thirdrdhand.smacktutorial.Model;


/**
 * Created by pacit on 2017/09/13.
 */

public class receivedSMS {
    public static final String TABLE_NAME = "RECEIVED_SMS";
    public static final String MSG_ID = "MSG_ID";
    public static final String MSG_SENDER = "SENDER";
    public static final String MSG_RECEIVER = "RECEIVER";
    public static final String RECEIVED_TIME = "RECEIVED_TIME";
    public static final String AMOUNT = "AMOUNT";
    public static final String CONTENT = "CONTENT";
    public static final String IS_DELETE = "IS_DELETE";


    public String msgId;  // id From the Server
    public String sender;  // The domain name like  @....
    public String receiver;  // Username which sent it From my Server or Not
    public String receivedTime;
    public String amount;
    public String msg_content;
    public int isDelete;

    public static String getCreateSql() {
        String sql = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME
                + "( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MSG_ID + " INTEGER,"
                + MSG_SENDER + " INTEGER,"
                + MSG_RECEIVER + " text,"
                + RECEIVED_TIME + " text,"
                + AMOUNT + " text,"
                + CONTENT + " text,"
                + IS_DELETE + " NUMBER"

                + ");";
        return sql;
    }
}
