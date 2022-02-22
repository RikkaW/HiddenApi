package rikka.hidden.compat.adapter;

import android.content.pm.IPackageInstallerCallback;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public class PackageInstallerCallbackAdapter extends IPackageInstallerCallback.Stub {

    @Override
    public void onSessionCreated(int sessionId) throws RemoteException {

    }

    @Override
    public void onSessionBadgingChanged(int sessionId) throws RemoteException {

    }

    @Override
    public void onSessionActiveChanged(int sessionId, boolean active) throws RemoteException {

    }

    @Override
    public void onSessionProgressChanged(int sessionId, float progress) throws RemoteException {

    }

    @Override
    public void onSessionFinished(int sessionId, boolean success) throws RemoteException {

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
