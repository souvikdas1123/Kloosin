package com.kloosin.utility.u;

import android.content.Context;

import com.kloosin.R;
import com.kloosin.service.model.UserRegistration;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class RequestHelper {

    private static RequestHelper mInstance;

    public synchronized static RequestHelper getInstance() {
        if (null == mInstance)
            mInstance = new RequestHelper();
        return (mInstance);
    }

    private RequestHelper() {}


    public void handleErrorResponse(Context context, int code, ResponseBody responseBody) {
        try {
            AlertInfoHelper.getInstance().alertIt(context, context.getString(R.string.error_dialog_heading), responseBody.string().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleFailedRequest(Call<?> call, Throwable t) {
        if ( !call.isCanceled() ) call.cancel();
    }
}
