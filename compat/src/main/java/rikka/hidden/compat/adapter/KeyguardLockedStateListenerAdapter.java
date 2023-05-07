package rikka.hidden.compat.adapter;

import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.android.internal.policy.IKeyguardLockedStateListener;

public class KeyguardLockedStateListenerAdapter extends IKeyguardLockedStateListener.Stub {

    @Override
    public void onKeyguardLockedStateChanged(boolean isKeyguardLocked) {

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
