package thirdrdhand.smacktutorial.constants;

/**
 * Created by pacit on 2017/08/17.
 */

public class CREDENTIALS {


    public static class Server {
        public static WORKING_MODE workingMode = WORKING_MODE.INTERNET;
        public static boolean initialised = false;
        private static String Domain;
        private static String Host;
        private static int Port;
        private static String WebsiteUser;

        public static void generateServerConfig() {

            if (workingMode == WORKING_MODE.LOCAL) {
                Domain = "localhost";
                Host = "192.168.1.200";
                Port = 5222;
                WebsiteUser = "pc";
                initialised = true;
            } else if (workingMode == WORKING_MODE.INTERNET) {
                Domain = "sido";
                Host = "198.181.32.10";
                Port = 5222;
                WebsiteUser = "pacifique";
                initialised = true;
            }
        }

        public static String getDomain() {
            if (initialised)
                return Domain;
            else return null;
        }

        public static String getHost() {
            if (initialised)
                return Host;
            else return null;
        }

        public static int getPort() { //Default 5222
            if (initialised)
                return Port;
            else return 5222;
        }

        public static String getWebsiteUser() {
            if (initialised)
                return WebsiteUser;
            else return null;
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
