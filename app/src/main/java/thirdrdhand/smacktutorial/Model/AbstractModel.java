package thirdrdhand.smacktutorial.Model;

import android.content.ContentValues;
import android.os.Parcelable;

/**
 * Created by pacit on 2017/08/24.
 */

public abstract class AbstractModel implements Parcelable {
    public abstract ContentValues getContentValues();

    public abstract String getTableName();

}
