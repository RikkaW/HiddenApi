package android.app;

import androidx.annotation.RequiresApi;

import dev.rikka.tools.refine.RefineAs;

@RefineAs(ActivityManager.class)
public class ActivityManagerHidden {

    public static int UID_OBSERVER_ACTIVE;

    @RequiresApi(24)
    public static int FLAG_AND_UNLOCKED;

    public static int PROCESS_STATE_UNKNOWN;

    public static int PROCESS_STATE_TOP;

    public static class RunningAppProcessInfo {

        public static int procStateToImportance(int procState) {
            throw new RuntimeException("STUB");
        }
    }
}
