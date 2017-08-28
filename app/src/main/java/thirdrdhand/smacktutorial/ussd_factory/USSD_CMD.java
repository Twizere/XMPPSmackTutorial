package thirdrdhand.smacktutorial.ussd_factory;

/**
 * Created by pacit on 2017/08/28.
 */

public class USSD_CMD {

    public String MTN_NUMBER = "0788485377";
    public String TIGO_NUMBER = "0728485377";

    public static class MTN {

        public static class MOBILE_MONEY {


            //Mobile Money
            private static String MONEY_BALANCE = "*182#";
            private static String DEPOSIT_MONEY = "*182#";
            private static String SEND_MONEY = "*182#";
            private static String WITHDRAW_MONEY = "*182#";
            private static String BUY_AIR_TIME = "*182#";
            private static String BUY_CASH_POWER = "*182#";
            private static String PAY_WATER = "*182#";
            private static String PAY_FUEL = "*182#";
            private static String PAY_SCHOOL_FEES = "*182#";


        }

        public static class SERVICES {
            private static String AIRTIME_BALANCE = "*110#";


        }


    }

    public static class TIGO {

        public static class TIGO_CASH {
            //Tigo Cash
            private static String MONEY_BALANCE = "*500#";
            private static String DEPOSIT_MONEY = "*500#";
            private static String SEND_MONEY = "*500#";
            private static String WITHDRAW_MONEY = "*500#";
            private static String BUY_AIR_TIME = "*500#";
            private static String BUY_CASH_POWER = "*500#";
            private static String PAY_WATER = "*500#";
            private static String PAY_FUEL = "*500#";
            private static String PAY_SCHOOL_FEES = "*500#";

            public static String getMoneyBalanceUssd() {

                //this needs to be changed
                return MONEY_BALANCE;
            }

            public static String depositMoneyUssd(String phoneNumber, String amount) {

                //this needs to be changed
                return DEPOSIT_MONEY;
            }
        }

        public static class SERVICES {
            private static String AIRTIME_BALANCE = "*110#";

        }


    }

    public static class AIRTEL {

        public static class AIRTEL_MONEY {
            //Airtel Money
            private static String MONEY_BALANCE = "*182#";
            private static String DEPOSIT_MONEY = "*182#";
            private static String SEND_MONEY = "*182#";
            private static String WITHDRAW_MONEY = "*182#";
            private static String BUY_AIR_TIME = "*182#";
            private static String BUY_CASH_POWER = "*182#";
            private static String PAY_WATER = "*182#";
            private static String PAY_FUEL = "*182#";
            private static String PAY_SCHOOL_FEES = "*182#";
        }

        public static class SERVICES {

            private static String AIRTIME_BALANCE = "*110#";
        }


    }

}
