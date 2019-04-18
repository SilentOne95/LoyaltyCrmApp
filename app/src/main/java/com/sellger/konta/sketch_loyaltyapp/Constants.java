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

    // MapFragment
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int REQUEST_CHECK_SETTINGS = 100;

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
}
