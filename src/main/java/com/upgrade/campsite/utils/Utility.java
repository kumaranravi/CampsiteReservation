package com.upgrade.campsite.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmailId(String emailStr) {
        Matcher matcher = EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
