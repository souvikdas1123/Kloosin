package com.kloosin.utility.u;

public class URLHelper {

    private static final String LOG_TAG = URLHelper.class.getSimpleName();
    private static URLHelper mURLHelper = null;

    public final String HOST_NAME = "http://kloosin.ezoneindiaportal.com";

    public static synchronized URLHelper getInstance() {
        if (null == mURLHelper)
            mURLHelper = newInstance();
        return mURLHelper;
    }
    private static URLHelper newInstance() {
        return new URLHelper();
    }
}
