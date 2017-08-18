package thirdrdhand.smacktutorial.xmpp.constants;

/**
 * Created by pacit on 2017/08/17.
 */

public class KEYS {

    public static final String SHARED_PREFERENCES = "KEYS-SHARED_PREFERENCES";

    /**
     * BroadCast Receiver Constatants
     */

    public  static class BroadCast{

        public static final String UI_AUTHENTICATED = "KEYS-BROADCAST-UI_AUTHENTICATED";
        public static final String CONNECTION_FAILURE = "KEYS-BROADCAST-CONNECTION_FAILURE";
        public static final String BACKEND_CMD = "KEYS-BROADCAST-CONNECTION-BACKEND_CMD";
        public static final String RECEIVED_NEW_MESSAGE = "KEYS-BROADCAST-CONNECTION-RECEIVED_NEW_MESSAGE";
    }

    /**
     * Intent Extra
     */
    public static class EXTRA {

        public static class XMPP {
            public static class Message {
                public static final String FROM = "KEYS-EXTRA-MESSAGE-FROM";
                public static final String TO = "KEYS-EXTRA-MESSAGE-TO";
                public static final String TYPE = "KEYS-EXTRA-MESSAGE-TYPE";
                public static final String SEND_DATE_TIME = "KEYS-EXTRA-MESSAGE-SEND_DATE_TIME";
                public static final String BODY ="KEYS-EXTRA-MESSAGE-BODY" ;
            }


        }
    }

    /**
     * SHARED_PREFERENCE_KEYS
     */
    public  static class PREF{

        public static class Auth{

            public static final  String USERNAME="KEYS-PREF-AUTH-USERNAME";
            public static final  String PASSWORD="KEYS-PREF-AUTH-PASSWORD";
            public static final  String AUTO_LOGIN="KEYS-PREF-AUTH-AUTO_LOGIN";
        }
    }

}
