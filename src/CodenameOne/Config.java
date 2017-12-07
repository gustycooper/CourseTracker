package zzwierko.coursetracker;

final class Config {
    static final String DIVIDER = new String(new char[100]).replace('\0', '-');
    static final int WHITE = 0xffffff;

    static final String BASE_URL = ""; // Add DB URL here.
    static final String ASSIGNMENTS_URL = BASE_URL + "assignments";
    static final String COURSES_URL = BASE_URL + "courses";
    static final String USERS_URL = BASE_URL + "users";

    static final String DATA_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static final String DISPLAY_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
}