package thirdrdhand.smacktutorial.constants;

/**
 * Created by pacit on 2017/08/17.
 */

public class CREDENTIALS {
    public static class Server {
        public static final String ServiceName = "localhost";
        public static final String Host = "192.168.1.200";
        public static final int Port = 5222;
        public static final String WebsiteUser = "pc";

//        public static final String ServiceName = "sido";
//        public static final String Host = "198.181.32.10";
//        public static final int Port = 5222;

    }

    public static class Auth {

        public static String Username;
        public static String Password;
        public static boolean AutoLogin;

        public static boolean isLoggedIn() {
            return !(Username == null || Password == null);

        }
    }

    public static class Modes {

        public static TYPES.ParsingMode ParsingMode = TYPES.ParsingMode.NO_EXTENSION;
    }
}
