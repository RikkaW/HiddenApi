package rikka.hidden.compat;

import static rikka.hidden.compat.Services.activityManager;
import static rikka.hidden.compat.Services.userManager;

import android.annotation.SuppressLint;
import android.app.ActivityManagerHidden;
import android.content.pm.UserInfo;
import android.os.Build;
import android.os.IUserManager;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class UserManagerApis {

    @SuppressLint("NewApi")
    @NonNull
    public static List<UserInfo> getUsers(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) throws RemoteException {
        IUserManager um = userManager.get();
        List<UserInfo> list;
        if (Build.VERSION.SDK_INT >= 30) {
            list = um.getUsers(excludePartial, excludeDying, excludePreCreated);
        } else {
            try {
                list = um.getUsers(excludeDying);
            } catch (NoSuchMethodError e) {
                list = um.getUsers(excludePartial, excludeDying, excludePreCreated);
            }
        }
        return list;
    }

    public static List<UserInfo> getUsersNoThrow(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) {
        List<UserInfo> result = new ArrayList<>();
        try {
            result.addAll(getUsers(excludePartial, excludeDying, excludePreCreated));
        } catch (Throwable ignored) {
        }
        return result;
    }

    public static boolean isUserUnlocked(int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return userManager.get().isUserUnlocked(userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return activityManager.get().isUserRunning(userId, ActivityManagerHidden.FLAG_AND_UNLOCKED);
        } else {
            return true;
        }
    }

    public static boolean isUserStorageAvailable(int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return userManager.get().isUserRunning(userId)
                    && userManager.get().isUserUnlocked(userId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return activityManager.get().isUserRunning(userId, ActivityManagerHidden.FLAG_AND_UNLOCKED);
        } else {
            return true;
        }
    }

    @NonNull
    public static UserInfo getUserInfo(int userId) {
        IUserManager um = userManager.get();
        return um.getUserInfo(userId);
    }

    @NonNull
    public static Collection<Integer> getUserIdsNoThrow() {
        return getUserIdsNoThrow(true, true, true);
    }

    public static Collection<Integer> getUserIdsNoThrow(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) {
        Set<Integer> result = new ArraySet<>();
        try {
            for (UserInfo it : getUsers(excludePartial, excludeDying, excludePreCreated)) {
                result.add(it.id);
            }
        } catch (Throwable ignored) {
            result.add(0);
        }
        return result;
    }
}
