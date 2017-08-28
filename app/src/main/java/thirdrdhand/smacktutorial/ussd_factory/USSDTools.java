package thirdrdhand.smacktutorial.ussd_factory;

import android.net.Uri;

/**
 * Created by pacit on 2017/08/28.
 */

public class USSDTools {


    public static Uri packageForCall(String ussd) {

        if (ussd.contains("#")) {

            /**
             * Example I have   *444*0987#8989898#90#     I want to Encode Hash tags
             *
             */
            String[] segmentsByHash = ussd.split("#");
            /**
             * This will give me this list:
             * segmentsByHash[0]="*444*0987"
             * segmentsByHash[1]="8989898"
             * segmentsByHash[2]="90"
             */

            Uri result = Uri.parse("tel:" + segmentsByHash[0]);
            /**
             * result= *444*0987
             */


            for (int i = 1; i <= segmentsByHash.length; i++) {

                if (i < segmentsByHash.length)
                    result = Uri.parse(result + Uri.encode("#") + Uri.parse(segmentsByHash[1]));
                /**
                 * when i=1;
                 * *444*0987#8989898
                 *
                 * when i=2
                 *  *444*0987#8989898#90
                 */
                    //Finnaly
                else if (i >= segmentsByHash.length) {
                    result = Uri.parse(result + Uri.encode("#"));
                }
                /**
                 *  finnaly add #
                 *  *444*0987#8989898#90#
                 */
            }


            return result;
        } else
            return Uri.parse("tel:" + ussd);
    }

}
