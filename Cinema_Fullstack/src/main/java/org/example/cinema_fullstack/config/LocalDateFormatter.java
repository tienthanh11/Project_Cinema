package org.example.cinema_fullstack.config;

import org.springframework.format.Formatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String text, Locale locale) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return LocalDate.parse(text, DATE_FORMATTER);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        if (object == null) {
            return "";
        }
        return DATE_FORMATTER.format(object);
    }
}
