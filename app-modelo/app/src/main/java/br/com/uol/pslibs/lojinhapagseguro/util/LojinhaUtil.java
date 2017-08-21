package br.com.uol.pslibs.lojinhapagseguro.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LojinhaUtil {

    public static String setComplementDecimal(String value){
        return new BigDecimal(value).setScale(2, RoundingMode.CEILING).toString();
    }

    public static String formatBilletNumber(String value){
        String partLine1 = value.substring(0, 5);
        String partLine2 = value.substring(5, 10);
        String partLine3 = value.substring(10, 15);
        String partLine4 = value.substring(15, 21);
        String partLine5 = value.substring(21, 26);
        String partLine6 = value.substring(26, 32);
        String partLine7 = value.substring(32, 33);
        String partLine8 = value.substring(33, 47);

        String result = String.format("%s.%s %s.%s %s.%s %s %s",
                partLine1,partLine2,partLine3,partLine4,partLine5,partLine6,partLine7,partLine8);
        return result;
    }
}
