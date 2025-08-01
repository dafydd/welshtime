package net.dafydd.welshtime;

import java.time.LocalDateTime;

public class WelshTime {

    public static String date(LocalDateTime time) {
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();

        return monthNumberToWord[month] + " " + ordinals[day] + ". " + time(time);
    }

    public static String time(LocalDateTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String translation = "anniffinedig";

        String wordHour = numberToWord[hour];

        if (minute < 30) {
            translation = numberToWord[minute] + " munud wedi " + wordHour;
        }

        if (minute > 30) {
            translation = numberToWord[60 - minute] + " munud i " + softMutateForI(numberToWord[(hour + 1) % 12]);
        }

        if (minute == 0) {
            translation = wordHour + " o'r gloch";
        }

        if (minute == 30) {
            translation = "hanner awr wedi " + wordHour;
        }

        return translation;
    }

    public static String softMutateForI(String followOn) {
        if (followOn == null || followOn.isEmpty()) return "";
        String finalPart = followOn.substring(1);
        switch (followOn.charAt(0)) {
            case 'p':
                return "b" + finalPart;
            case 't':
                return "d" + finalPart;
            case 'c':
                return "g" + finalPart;
            case 'b':
                return "f" + finalPart;
            case 'd':
                return "dd" + finalPart;
            case 'g':
                return finalPart;
            case 'm':
                return "f" + finalPart;
            case 'r':
                if (finalPart.startsWith("h")) {
                    return "r" + finalPart.substring(1);
                }
            default:
                return followOn;
        }
    }

    private final static String[] numberToWord = {
            "dim",
            "un",
            "dau",
            "tri",
            "pedwar",
            "pump",
            "chwech",
            "saith",
            "wyth",
            "naw",
            "deg",
            "un ar ddeg",
            "deuddeg",
            "tair ar ddeg",
            "pedwar ar ddeg",
            "un deg pump",
            "un deg chwech",
            "un deg saith",
            "un deg wyth",
            "un deg naw",
            "dau ddeg",
            "dau ddeg un",
            "dau ddeg dau",
            "dau ddeg tri",
            "dau ddeg pedwar",
            "dau ddeg pump",
            "dau ddeg chwech",
            "dau ddeg saith",
            "dau ddeg wyth",
            "dau ddeg naw"
    };

    private final static String[] ordinals = {
            "",
            "y cyntaf",
            "yr ail",
            "y trydydd",
            "y pedwerydd",
            "y pumed",
            "y chweched",
            "y seithfed",
            "yr wythfed",
            "y nawfed",
            "y degfed",
            "yr unfed ar ddeg",
            "y deuddegfed",
            "y trydydd ar ddeg ",
            "y pedwerydd ar ddeg",
            "y pymthegfed",
            "yr unfed ar bymtheg",
            "yr ail ar bymtheg",
            "y deunawfed",
            "y pedwerydd ar bymtheg",
            "yr ugeinfed",
            "yr unfed ar hugain",
            "yr ail ar hugain",
            "y trydydd ar hugain",
            "y pedwerydd ar hugain",
            "y pumed ar hugain",
            "y chweched ar hugain",
            "y seithfed ar hugain",
            "yr wythfed ar hugain",
            "y nawfed ar hugain",
            "y degfed ar hugain",
            "yr unfed ar ddeg ar hugain"
    };

    private final static String[] monthNumberToWord = {
            "",
            "Ionawr",
            "Chwefror",
            "Mawrth",
            "Ebrill",
            "Mai",
            "Mehefin",
            "Gorffennaf",
            "Awst",
            "Medi",
            "Hydref",
            "Tachwedd",
            "Rhagfyr"
    };

}
