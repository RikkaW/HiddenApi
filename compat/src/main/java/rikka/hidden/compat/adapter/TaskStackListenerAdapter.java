package rikka.hidden.compat.adapter;

import android.app.ITaskStackListener;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public class TaskStackListenerAdapter extends ITaskStackListener.Stub {

    @Override
    public void onTaskStackChanged() {

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
