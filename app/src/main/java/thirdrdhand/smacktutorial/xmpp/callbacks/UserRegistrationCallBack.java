package thirdrdhand.smacktutorial.xmpp.callbacks;


/**
 * Created by pacit on 2017/09/07.
 */

public interface UserRegistrationCallBack {
    abstract void onRegisterOK();

    abstract void onRegisterFailed(String reason);
}
