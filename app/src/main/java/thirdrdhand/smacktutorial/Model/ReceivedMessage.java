package thirdrdhand.smacktutorial.Model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import thirdrdhand.smacktutorial.constants.TYPES;

/**
 * Created by pacit on 2017/08/24.
 */

public class ReceivedMessage extends AbstractModel {
    public static final String MSG_ID = "MSG_ID";
    public static final String XMPP_TYPE = "XMPP_TYPE";
    public static final String FROM_DOMAIN = "FROM_DOMAIN";
    public static final String USERNAME = "USERNAME";
    public static final String MSG_TYPE = "MSG_TYPE";
    public static final String PAYLOAD = "PAYLOAD";
    public static final String CONTENT = "CONTENT";
    public static final String TIME_STAMP = "TIME_STAMP";
    public static final String MSG_STATUS = "MSG_STATUS";
    public static final String MSG_ERROR = "MSG_ERROR";
    public static final String IS_DELETE = "IS_DELETE";
    public static final Parcelable.Creator<ReceivedMessage> CREATOR = new Parcelable.Creator<ReceivedMessage>() {

        @Override
        public ReceivedMessage createFromParcel(Parcel parcel) {
            return new ReceivedMessage(parcel);
        }

        @Override
        public ReceivedMessage[] newArray(int i) {
            return new ReceivedMessage[0];
        }
    };
    public static String TABLE_NAME = "TB_RECEIVED_MSG";
    public String msgId;  // id From the Server
    public int XmppType;   // 0 chat  1 groupChat
    public String FromDomain;  // The domain name like  @....
    public String Username;  // Username which sent it From my Server or Not
    public TYPES.MessageType mType; // [command][request][info]
    public String Payload;
    public String Content;
    public long TimeStamp;
    public TYPES.MessageStatus mStatus;
    public TYPES.MessageError mError;
    public int isDelete;

    public ReceivedMessage() {

    }


    public ReceivedMessage(Parcel parcel) {
        msgId = parcel.readString();
        XmppType = parcel.readInt();
        FromDomain = parcel.readString();
        Username = parcel.readString();
        mType = TYPES.MessageType.valueOf(parcel.readString());
        Payload = parcel.readString();
        Content = parcel.readString();
        TimeStamp = parcel.readLong();
        mStatus = TYPES.MessageStatus.valueOf(parcel.readString());
        mError = TYPES.MessageError.valueOf(parcel.readString());
        isDelete = parcel.readInt();
    }

    public static String getCreateSql() {
        String sql = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME
                + "( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MSG_ID + " INTEGER,"
                + XMPP_TYPE + " INTEGER,"
                + FROM_DOMAIN + " text,"
                + USERNAME + " text,"
                + MSG_TYPE + " text,"
                + PAYLOAD + " text,"
                + CONTENT + " text,"
                + TIME_STAMP + " NUMBER,"
                + MSG_STATUS + " text,"
                + MSG_ERROR + " text,"
                + IS_DELETE + " NUMBER"

                + ");";
        return sql;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues vals = new ContentValues();
        vals.put(MSG_ID, msgId);
        vals.put(XMPP_TYPE, XmppType);
        vals.put(FROM_DOMAIN, FromDomain);
        vals.put(USERNAME, Username);
        vals.put(MSG_TYPE, mType.Value);
        vals.put(PAYLOAD, Payload);
        vals.put(CONTENT, Content);
        vals.put(TIME_STAMP, TimeStamp);
        vals.put(MSG_STATUS, mStatus.Value);
        vals.put(MSG_ERROR, mError.Value);
        vals.put(IS_DELETE, isDelete);

        return vals;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(msgId);
        parcel.writeInt(XmppType);
        parcel.writeString(FromDomain);
        parcel.writeString(Username);
        parcel.writeString(mType.Value);
        parcel.writeString(Payload);
        parcel.writeString(Content);
        parcel.writeLong(TimeStamp);
        parcel.writeString(mStatus.Value);
        parcel.writeString(mError.Value);
        parcel.writeInt(isDelete);
    }
}
