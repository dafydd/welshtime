package net.dafydd.welshtime;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class WelshTimeTest {

    private LocalDateTime t(int year, int month, int day, int hour, int min) {
        return LocalDateTime.of(year, month, day, hour, min);
    }

    @Test
    void oClock() {
        String words = WelshTime.time(t(2019, 5, 3, 9, 0));
        assertEquals("naw o'r gloch", words);
    }

    @Test
    void halfPast() {
        String words = WelshTime.time(t(2019, 5, 3, 9, 30));
        assertEquals("hanner awr wedi naw", words);
    }

    @Test
    void minutesPast() {
        String words = WelshTime.time(t(2019, 5, 3, 9, 17));
        assertEquals("un deg saith munud wedi naw", words);
    }

    @Test
    void minutesPastShouldNotSoftMutateTheFirstPart() {
        String words = WelshTime.time(t(2019, 5, 3, 9, 20));
        assertNotEquals("ddau ddeg munud wedi naw", words);
        assertEquals("dau ddeg munud wedi naw", words);
    }

    @Test
    void minutesTo() {
        String words = WelshTime.time(t(2019, 5, 3, 9, 41));
        assertEquals("un deg naw munud i ddeg", words);
    }

    @Test
    void date() {
        assertEquals("Ionawr y cyntaf. naw o'r gloch", WelshTime.date(t(2019, 1, 1, 9, 0)));
        assertEquals("Mai y trydydd. naw o'r gloch", WelshTime.date(t(2019, 5, 3, 9, 0)));
        assertEquals("Rhagfyr yr unfed ar ddeg ar hugain. naw o'r gloch", WelshTime.date(t(2019, 12, 31, 9, 0)));
    }

    @Test
    void mutate() {
        assertEquals("ddau", WelshTime.softMutateForI("dau"));
        assertEquals("dri", WelshTime.softMutateForI("tri"));
        assertEquals("bedwar", WelshTime.softMutateForI("pedwar"));
        assertEquals("bump", WelshTime.softMutateForI("pump"));
        assertEquals("rywfaint", WelshTime.softMutateForI("rhywfaint"));
        assertEquals("xnotthis", WelshTime.softMutateForI("xnotthis"));
        assertEquals("", WelshTime.softMutateForI(""));
        assertEquals("", WelshTime.softMutateForI(null));
    }
}
