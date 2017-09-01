package thirdrdhand.smacktutorial.constants;

/**
 * Created by pacit on 2017/08/17.
 */

public class CREDENTIALS {


    public static class Server {
        public static WORKING_MODE workingMode = WORKING_MODE.INTERNET;
        public static String ServiceName;
        public static String Host;
        public static int Port;
        public static String WebsiteUser;

        public static void generateServerConfig() {

            if (workingMode == WORKING_MODE.LOCAL) {
                ServiceName = "localhost";
                Host = "192.168.1.200";
                Port = 5222;
                WebsiteUser = "pc";
            } else if (workingMode == WORKING_MODE.INTERNET) {
                ServiceName = "sido";
                Host = "198.181.32.10";
                Port = 5222;
                WebsiteUser = "pacifique";
            }
        }

        public enum WORKING_MODE {
            INTERNET, LOCAL;

        }
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
