package rikka.hidden.compat.adapter;

import android.hardware.display.IDisplayManagerCallback;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public class DisplayManagerCallbackAdapter extends IDisplayManagerCallback.Stub {

    @Override
    public void onDisplayEvent(int displayId, int event) {

    }

    @Override
    public boolean onTransact(int code, @NonNull Parcel data, Parcel reply, int flags) throws RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (Throwable tr) {
            return true;
        }
    }
}
