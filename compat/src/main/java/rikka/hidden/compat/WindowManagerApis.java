package rikka.hidden.compat;

import static rikka.hidden.compat.Services.windowManager;

import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import com.android.internal.policy.IKeyguardLockedStateListener;

public class WindowManagerApis {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public static void addKeyguardLockedStateListener(IKeyguardLockedStateListener listener) throws RemoteException {
        windowManager.get().addKeyguardLockedStateListener(listener);
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public static void removeKeyguardLockedStateListener(IKeyguardLockedStateListener listener) throws RemoteException {
        windowManager.get().removeKeyguardLockedStateListener(listener);
    }
}
