package fa.training.fjb04.ims.util;

import org.springframework.stereotype.Component;

@Component
public class Common {
        public static boolean isBlankOrEmpty (String string) {
        if (string == null) {
            return  true;
        }
        if (string.trim() == "") {
            return true;
        }
        return false;
    }
}
