package thirdrdhand.smacktutorial.xmpp.callbacks;


/**
 * Created by pacit on 2017/09/07.
 */

public interface UserLoginCallBack {
    abstract void onLoginOK();

    abstract void onLoginFailed(String reason);
}
