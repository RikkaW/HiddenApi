package android.app;

import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Executor;

public class ActivityThread {

    public static native ActivityThread systemMain();

    public static native ActivityThread currentActivityThread();

    public static native String currentOpPackageName();

    public static native String currentPackageName();

    public static native String currentProcessName();

    @RequiresApi(Build.VERSION_CODES.S)
    public native Handler getHandler();

    public native IApplicationThread getApplicationThread();

    public native Instrumentation getInstrumentation();

    @RequiresApi(Build.VERSION_CODES.P)
    public native Executor getExecutor();

    public native Application getApplication();

    public native String getProcessName();

    public native ContextImpl getSystemContext();
}
