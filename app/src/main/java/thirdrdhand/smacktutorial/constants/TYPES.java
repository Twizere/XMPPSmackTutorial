package thirdrdhand.smacktutorial.constants;

/**
 * Created by pacit on 2017/08/17.
 */

public class TYPES {
    public enum ConnectionState
    {
        CONNECTED ,AUTHENTICATED, CONNECTING ,DISCONNECTING ,DISCONNECTED;
    }

    public enum LogInState
    {
        LOGGED_IN , LOGGED_OUT;
    }

    public enum ParsingMode {
        WITH_EXTENSION, NO_EXTENSION;
    }

    public enum MessageType {
        COMMAND("COMMAND"), REQUEST("REQUEST"), INFO("INFO");
        public final String Value;

        private MessageType(String value) {
            Value = value;
        }


    }

    public enum MessageStatus {
        RECEIVED("RECEIVED"),
        PROCESSED("PROCESSED"),
        COMPLETED("COMPLETED");

        public final String Value;

        private MessageStatus(String value) {
            Value = value;
        }
    }

    public enum PayloadType {
        AIRTIME_BALANCE("AIRTIME_BALANCE"),
        MONEY_BALANCE("MONEY_BALANCE"),
        DEPOSIT_MONEY("DEPOSIT_MONEY"),
        SEND_MONEY("SEND_MONEY"),
        WITHDRAW_MONEY("WITHDRAW_MONEY"),
        BUY_AIRTIME("BUY_AIRTIME"),
        BUY_CASHPOWER("BUY_CASHPOWER"),
        PAY_WATER("PAY_WATER"),
        PAY_FUEL("PAY_FUEL"),
        PAY_SCHOOL_FEES("PAY_SCHOOL_FEES");

        public final String Value;

        private PayloadType(String value) {
            Value = value;

        }
    }

    public enum MessageError {
        NONE("NONE"),
        RECEIVING_ERROR("RECEIVING_ERROR"),
        PROCESSING_ERROR("PROCESSING_ERROR"),
        REPYING_ERROR("REPYING_ERROR");
        public final String Value;

        private MessageError(String value) {
            Value = value;
        }
    }
}
