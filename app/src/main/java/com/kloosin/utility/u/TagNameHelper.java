package com.kloosin.utility.u;

public class TagNameHelper {

    private static TagNameHelper mInstance;

    public static synchronized TagNameHelper getInstance() {
        if ( null == mInstance ) mInstance = new TagNameHelper();
        return mInstance;
    }

    public String LOGIN_RESPONSE_KEY    =   "__LOGIN_RESPONSE__KEY__";
    public String IS_INVITATION_COMPLETED_KEY    =   "__INVITATION_COMPLETED__KEY__";
    public String NEED_TO_REBOOT_RETROFIT_SERVICE_KEY    =   "__NEED_TO_REBOOT_RETROFIT_SERVICE__";

}
