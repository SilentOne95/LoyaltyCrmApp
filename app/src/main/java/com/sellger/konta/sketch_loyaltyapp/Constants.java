package com.sellger.konta.sketch_loyaltyapp;

public final class Constants {
    private Constants() {}

    // General
    public static final String BASE_URL = "https://my-loyalty-project.herokuapp.com/";
    public static final String BASE_URL_IMAGES = "http://10.0.2.2:5000";

    // MainActivity
    public static final int RC_SIGN_IN = 9001;
    public static final int NAV_VIEW_FIRST_GROUP_ID = 0;
    public static final int NAV_VIEW_SECOND_GROUP_ID = 1;
    public static final int NAV_VIEW_THIRD_GROUP_ID = 2;
    public static final int NAV_VIEW_ORDER = 0;

    // MainActivityPresenter
    public static final String NAV_DRAWER_TYPE_MENU = "menu";
    public static final String NAV_DRAWER_TYPE_SUBMENU = "submenu";

    // MapFragment
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int REQUEST_CHECK_SETTINGS = 100;

    public static final int UPDATE_INTERVAL = 30 * 1000;
    public static final int FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    public static final int MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

    // Map related static fields
    public static final String MONDAY_STRING = "monday";
    public static final String TUESDAY_STRING = "tuesday";
    public static final String WEDNESDAY_STRING = "wednesday";
    public static final String THURSDAY_STRING = "thursday";
    public static final String FRIDAY_STRING = "friday";
    public static final String SATURDAY_STRING = "saturday";
    public static final String SUNDAY_STRING = "sunday";

    public static final String DEFAULT_UNABLE_STRING = "Unable to display";
    public static final String TODAY_OPEN_STRING = "Today open:";
    public static final String ALL_DAY_STRING = "All day";

    public static final String CLOSED_STRING = "Closed";
    public static final String OPEN_STRING = "Open 24/7";

    public static final String ERROR_NONE_STRING = "None";

    // MyAccountFragment
    public static final int BARCODE_WIDTH = 600;
    public static final int BARCODE_HEIGHT = 300;
    public static final int BARCODE_COUPON_HEIGHT = 150;

    // ScanResultFragment
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 201;

    // Adapter
    public static final int DEFAULT_NUM_OF_COLUMNS = 1;

    public static final int BITMAP_CORNER_RADIUS = 15;

    public static final int BITMAP_WIDTH_ONE_COLUMN = 1200;
    public static final int BITMAP_HEIGHT_ONE_COLUMN = 720;
    public static final int BITMAP_WIDTH_TWO_COLUMNS = 600;
    public static final int BITMAP_HEIGHT_TWO_COLUMNS = 360;

    public static final String DEFAULT_STRING = "Not available";

    // Additional data while changing fragments
    public static final String ANONYMOUS_REGISTRATION = "anonymous";
    public static final String NOT_ANONYMOUS_REGISTRATION = "not anonymous";

    // Type of registration method
    public static final String REGISTRATION_NORMAL = "normal";
    public static final String REGISTRATION_ANONYMOUS = "anonymous";
    public static final String REGISTRATION_CONVERSION = "conversion";

    // Subscription topics - push notifications
    public static final String FIRST_TOPIC_NAME = "news";
    public static final String SECOND_TOPIC_NAME = "offers";
    public static final String THIRD_TOPIC_NAME = "discounts";
    public static final String ANONYMOUS_TOPIC_NAME = "anonymous";

    // Layout types
    public static final String LAYOUT_TYPE_HOME = "home";
    public static final String LAYOUT_TYPE_PRODUCTS = "products";
    public static final String LAYOUT_TYPE_COUPONS = "coupons";
    public static final String LAYOUT_TYPE_MAP = "map";
    public static final String LAYOUT_TYPE_URL = "url";
    public static final String LAYOUT_TYPE_TERMS = "terms";
    public static final String LAYOUT_TYPE_CONTACT = "contact";
    public static final String LAYOUT_TYPE_SCANNER = "scanner";
    public static final String LAYOUT_TYPE_CAMERA = "camera";
    public static final String LAYOUT_TYPE_ACCOUNT = "account";
    public static final String LAYOUT_TYPE_SETTINGS = "settings";
    public static final String LAYOUT_TYPE_LOGIN = "login";
    public static final String LAYOUT_TYPE_PHONE = "phone";
    public static final String LAYOUT_TYPE_CODE = "code";

    public static final String LAYOUT_DATA_EMPTY_STRING = "";

    // Settings - SharedPref file name
    public static final String SHARED_PREF_FILE_NAME = "switchState";

    // Repository - fetched data type
    public static final String TYPE_MENU = "menu";
    public static final String TYPE_PRODUCT = "product";
    public static final String TYPE_COUPON = "coupon";
    public static final String TYPE_MARKER = "marker";
    public static final String TYPE_OPEN_HOUR = "open_hour";
    public static final String TYPE_PAGE = "page";

    // Toast messages
    public static final String TOAST_DATA_ERROR_MESSAGE = "Oops.. something went wrong.\nCheck your " +
            "internet connection or try again later";

    // Delay timer in milliseconds
    public static final int DELAY_DRAWER_ACTION = 200;
    public static final int DELAY_BOTTOMSHEET_ACTION = 200;
    public static final int DELAY_FETCHING_DATA = 1000;
    public static final int DELAY_PHONE_AUTH = 1000;
    public static final int DELAY_PHONE_AUTH_DISMISS_ERROR = 2000;
    public static final int DELAY_SET_SMS_CODE = 3000;
    public static final int DELAY_LOGIN_SWITCH_LAYOUT = 2000;
}
