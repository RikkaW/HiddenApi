package rikka.hidden.compat;

import static rikka.hidden.compat.Services.appOps;

import android.annotation.SuppressLint;
import android.app.AppOpsManagerHidden;
import android.os.Binder;
import android.os.Build;
import android.os.RemoteCallback;
import android.os.RemoteException;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Preconditions;

import com.android.internal.app.IAppOpsActiveCallback;
import com.android.internal.app.IAppOpsNotedCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
public class AppOpsApis {

    public static List<AppOpsManagerHidden.PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        return appOps.get().getOpsForPackage(uid, packageName, ops);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<AppOpsManagerHidden.PackageOps> getUidOps(int uid, int[] ops) throws RemoteException {
        return appOps.get().getUidOps(uid, ops);
    }

    public static void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
        appOps.get().setMode(code, uid, packageName, mode);
    }

    public static void setUidMode(int code, int uid, int mode) throws RemoteException {
        appOps.get().setUidMode(code, uid, mode);
    }

    public static void resetAllModes(int userId, String packageName) throws RemoteException {
        appOps.get().resetAllModes(userId, packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void startWatchingActive(int[] ops, IAppOpsActiveCallback callback) throws RemoteException {
        appOps.get().startWatchingActive(ops, callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void stopWatchingActive(IAppOpsActiveCallback callback) throws RemoteException {
        appOps.get().stopWatchingActive(callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static boolean isOperationActive(int code, int uid, String packageName) throws RemoteException {
        return appOps.get().isOperationActive(code, uid, packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void startWatchingNoted(int[] ops, IAppOpsNotedCallback callback) throws RemoteException {
        appOps.get().startWatchingNoted(ops, callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void stopWatchingNoted(IAppOpsNotedCallback callback) throws RemoteException {
        appOps.get().stopWatchingNoted(callback);
    }

    /**
     * Flag: non proxy operations. These are operations
     * performed on behalf of the app itself and not on behalf of
     * another one.
     */
    public static final int OP_FLAG_SELF = 0x1;

    /**
     * Flag: trusted proxy operations. These are operations
     * performed on behalf of another app by a trusted app.
     * Which is work a trusted app blames on another app.
     */
    public static final int OP_FLAG_TRUSTED_PROXY = 0x2;

    /**
     * Flag: untrusted proxy operations. These are operations
     * performed on behalf of another app by an untrusted app.
     * Which is work an untrusted app blames on another app.
     */
    public static final int OP_FLAG_UNTRUSTED_PROXY = 0x4;

    /**
     * Flag: trusted proxied operations. These are operations
     * performed by a trusted other app on behalf of an app.
     * Which is work an app was blamed for by a trusted app.
     */
    public static final int OP_FLAG_TRUSTED_PROXIED = 0x8;

    /**
     * Flag: untrusted proxied operations. These are operations
     * performed by an untrusted other app on behalf of an app.
     * Which is work an app was blamed for by an untrusted app.
     */
    public static final int OP_FLAG_UNTRUSTED_PROXIED = 0x10;

    /**
     * Flags: all operations. These include operations matched
     * by {@link #OP_FLAG_SELF}, {@link #OP_FLAG_TRUSTED_PROXIED},
     * {@link #OP_FLAG_UNTRUSTED_PROXIED}, {@link #OP_FLAG_TRUSTED_PROXIED},
     * {@link #OP_FLAG_UNTRUSTED_PROXIED}.
     */
    public static final int OP_FLAGS_ALL =
            OP_FLAG_SELF
                    | OP_FLAG_TRUSTED_PROXY
                    | OP_FLAG_UNTRUSTED_PROXY
                    | OP_FLAG_TRUSTED_PROXIED
                    | OP_FLAG_UNTRUSTED_PROXIED;

    /**
     * Flags: all trusted operations which is ones either the app did {@link #OP_FLAG_SELF},
     * or it was blamed for by a trusted app {@link #OP_FLAG_TRUSTED_PROXIED}, or ones the
     * app if untrusted blamed on other apps {@link #OP_FLAG_UNTRUSTED_PROXY}.
     */
    public static final int OP_FLAGS_ALL_TRUSTED = OP_FLAG_SELF
            | OP_FLAG_UNTRUSTED_PROXY
            | OP_FLAG_TRUSTED_PROXIED;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {
            OP_FLAG_SELF,
            OP_FLAG_TRUSTED_PROXY,
            OP_FLAG_UNTRUSTED_PROXY,
            OP_FLAG_TRUSTED_PROXIED,
            OP_FLAG_UNTRUSTED_PROXIED
    })
    public @interface OpFlags {
    }


    /**
     * Flag for querying app op history: get only aggregate information (counts of events) and no
     * discret accesses information - specific accesses with timestamp.
     *
     * @see #getHistoricalOps(HistoricalOpsRequest, Executor, Consumer)
     */
    public static final int HISTORY_FLAG_AGGREGATE = 1 << 0;

    /**
     * Flag for querying app op history: get only discrete access information (only specific
     * accesses with timestamps) and no aggregate information (counts over time).
     *
     * @see #getHistoricalOps(HistoricalOpsRequest, Executor, Consumer)
     */
    public static final int HISTORY_FLAG_DISCRETE = 1 << 1;

    /**
     * Flag for querying app op history: assemble attribution chains, and attach the last visible
     * node in the chain to the start as a proxy info. This only applies to discrete accesses.
     */
    public static final int HISTORY_FLAG_GET_ATTRIBUTION_CHAINS = 1 << 2;

    /**
     * Flag for querying app op history: get all types of historical access information.
     *
     * @see #getHistoricalOps(HistoricalOpsRequest, Executor, Consumer)
     */
    public static final int HISTORY_FLAGS_ALL = HISTORY_FLAG_AGGREGATE
            | HISTORY_FLAG_DISCRETE;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {
            HISTORY_FLAG_AGGREGATE,
            HISTORY_FLAG_DISCRETE,
            HISTORY_FLAG_GET_ATTRIBUTION_CHAINS
    })
    public @interface OpHistoryFlags {
    }

    /**
     * Filter historical appop request by uid.
     */
    public static final int FILTER_BY_UID = 1 << 0;

    /**
     * Filter historical appop request by package name.
     */
    public static final int FILTER_BY_PACKAGE_NAME = 1 << 1;

    /**
     * Filter historical appop request by attribution tag.
     */
    public static final int FILTER_BY_ATTRIBUTION_TAG = 1 << 2;

    /**
     * Filter historical appop request by op names.
     */
    public static final int FILTER_BY_OP_NAMES = 1 << 3;

    /**
     * Specifies what parameters to filter historical appop requests for
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {
            FILTER_BY_UID,
            FILTER_BY_PACKAGE_NAME,
            FILTER_BY_ATTRIBUTION_TAG,
            FILTER_BY_OP_NAMES
    })
    public @interface HistoricalOpsRequestFilter {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static final class HistoricalOpsRequest {

        private final int mUid;
        @Nullable
        private final String mPackageName;
        @Nullable
        private final String mAttributionTag;
        @Nullable
        private final List<String> mOpNames;

        @OpHistoryFlags
        private final int mHistoryFlags;
        @HistoricalOpsRequestFilter
        private final int mFilter;
        private final long mBeginTimeMillis;
        private final long mEndTimeMillis;
        @OpFlags
        private final int mFlags;

        private HistoricalOpsRequest(int uid, @Nullable String packageName,
                                     @Nullable String attributionTag, @Nullable List<String> opNames,
                                     @OpHistoryFlags int historyFlags, @HistoricalOpsRequestFilter int filter,
                                     long beginTimeMillis, long endTimeMillis, @OpFlags int flags) {
            mUid = uid;
            mPackageName = packageName;
            mAttributionTag = attributionTag;
            mOpNames = opNames;
            mHistoryFlags = historyFlags;
            mFilter = filter;
            mBeginTimeMillis = beginTimeMillis;
            mEndTimeMillis = endTimeMillis;
            mFlags = flags;
        }

        @SuppressLint("RestrictedApi")
        public static final class Builder {
            private int mUid = -1;

            @Nullable
            private String mPackageName;

            @Nullable
            private String mAttributionTag;

            @Nullable
            private List<String> mOpNames;

            @OpHistoryFlags
            private int mHistoryFlags;

            @HistoricalOpsRequestFilter
            private int mFilter;
            private final long mBeginTimeMillis;
            private final long mEndTimeMillis;

            @OpFlags
            private int mFlags = OP_FLAGS_ALL;

            /**
             * Creates a new builder.
             *
             * @param beginTimeMillis The beginning of the interval in milliseconds since
             *                        epoch start (January 1, 1970 00:00:00.000 GMT - Gregorian). Must be non
             *                        negative.
             * @param endTimeMillis   The end of the interval in milliseconds since
             *                        epoch start (January 1, 1970 00:00:00.000 GMT - Gregorian). Must be after
             *                        {@code beginTimeMillis}. Pass {@link Long#MAX_VALUE} to get the most recent
             *                        history including ops that happen while this call is in flight.
             */

            public Builder(long beginTimeMillis, long endTimeMillis) {
                Preconditions.checkArgument(beginTimeMillis >= 0 && beginTimeMillis < endTimeMillis,
                        "beginTimeMillis must be non negative and lesser than endTimeMillis");
                mBeginTimeMillis = beginTimeMillis;
                mEndTimeMillis = endTimeMillis;
                mHistoryFlags = HISTORY_FLAG_AGGREGATE;
            }

            /**
             * Sets the UID to query for.
             *
             * @param uid The uid. Pass {@link android.os.Process#INVALID_UID} for any uid.
             * @return This builder.
             */
            @NonNull
            public Builder setUid(int uid) {
                Preconditions.checkArgument(uid == -1 || uid >= 0,
                        "uid must be -1 or non negative");
                mUid = uid;

                if (uid == -1) {
                    mFilter &= ~FILTER_BY_UID;
                } else {
                    mFilter |= FILTER_BY_UID;
                }

                return this;
            }

            /**
             * Sets the package to query for.
             *
             * @param packageName The package name. <code>Null</code> for any package.
             * @return This builder.
             */
            @NonNull
            public Builder setPackageName(@Nullable String packageName) {
                mPackageName = packageName;

                if (packageName == null) {
                    mFilter &= ~FILTER_BY_PACKAGE_NAME;
                } else {
                    mFilter |= FILTER_BY_PACKAGE_NAME;
                }

                return this;
            }

            /**
             * Sets the attribution tag to query for.
             *
             * @param attributionTag attribution tag
             * @return This builder.
             */
            @NonNull
            public Builder setAttributionTag(@Nullable String attributionTag) {
                mAttributionTag = attributionTag;
                mFilter |= FILTER_BY_ATTRIBUTION_TAG;

                return this;
            }

            /**
             * Sets the op names to query for.
             *
             * @param opNames The op names. <code>Null</code> for any op.
             * @return This builder.
             */
            @NonNull
            public Builder setOpNames(@Nullable List<String> opNames) {
                /*if (opNames != null) {
                    final int opCount = opNames.size();
                    for (int i = 0; i < opCount; i++) {
                        Preconditions.checkArgument(AppOpsManager.strOpToOp(
                                opNames.get(i)) != AppOpsManager.OP_NONE);
                    }
                }*/
                mOpNames = opNames;

                if (mOpNames == null) {
                    mFilter &= ~FILTER_BY_OP_NAMES;
                } else {
                    mFilter |= FILTER_BY_OP_NAMES;
                }

                return this;
            }

            /**
             * Sets the op flags to query for. The flags specify the type of
             * op data being queried.
             *
             * @param flags The flags which are any combination of
             *              {@link #OP_FLAG_SELF}, {@link #OP_FLAG_TRUSTED_PROXY},
             *              {@link #OP_FLAG_UNTRUSTED_PROXY}, {@link #OP_FLAG_TRUSTED_PROXIED},
             *              {@link #OP_FLAG_UNTRUSTED_PROXIED}. You can use {@link #OP_FLAGS_ALL}
             *              for any flag.
             * @return This builder.
             */
            @NonNull
            public Builder setFlags(@OpFlags int flags) {
                Preconditions.checkFlagsArgument(flags, OP_FLAGS_ALL);
                mFlags = flags;
                return this;
            }

            /**
             * Specifies what type of historical information to query.
             *
             * @param flags Flags for the historical types to fetch which are any
             *              combination of {@link #HISTORY_FLAG_AGGREGATE}, {@link #HISTORY_FLAG_DISCRETE},
             *              {@link #HISTORY_FLAGS_ALL}. The default is {@link #HISTORY_FLAG_AGGREGATE}.
             * @return This builder.
             */
            @NonNull
            public Builder setHistoryFlags(@OpHistoryFlags int flags) {
                Preconditions.checkFlagsArgument(flags,
                        HISTORY_FLAGS_ALL | HISTORY_FLAG_GET_ATTRIBUTION_CHAINS);
                mHistoryFlags = flags;
                return this;
            }

            /**
             * @return a new {@link HistoricalOpsRequest}.
             */
            @NonNull
            public HistoricalOpsRequest build() {
                int historyFlags = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if ((mHistoryFlags & HISTORY_FLAG_AGGREGATE) == HISTORY_FLAG_AGGREGATE) {
                        historyFlags |= AppOpsManagerHidden.HISTORY_FLAG_AGGREGATE;
                    }
                    if ((mHistoryFlags & HISTORY_FLAG_DISCRETE) == HISTORY_FLAG_DISCRETE) {
                        historyFlags |= AppOpsManagerHidden.HISTORY_FLAG_DISCRETE;
                    }
                    if ((mHistoryFlags & HISTORY_FLAG_GET_ATTRIBUTION_CHAINS) == HISTORY_FLAG_GET_ATTRIBUTION_CHAINS) {
                        historyFlags |= AppOpsManagerHidden.HISTORY_FLAG_GET_ATTRIBUTION_CHAINS;
                    }
                }

                int filter = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if ((mFilter & FILTER_BY_UID) == FILTER_BY_UID) {
                        filter |= AppOpsManagerHidden.FILTER_BY_UID;
                    }
                    if ((mFilter & FILTER_BY_PACKAGE_NAME) == FILTER_BY_PACKAGE_NAME) {
                        filter |= AppOpsManagerHidden.FILTER_BY_PACKAGE_NAME;
                    }
                    if ((mFilter & FILTER_BY_ATTRIBUTION_TAG) == FILTER_BY_ATTRIBUTION_TAG) {
                        filter |= AppOpsManagerHidden.FILTER_BY_ATTRIBUTION_TAG;
                    }
                    if ((mFilter & FILTER_BY_OP_NAMES) == FILTER_BY_OP_NAMES) {
                        filter |= AppOpsManagerHidden.FILTER_BY_OP_NAMES;
                    }
                }

                int flags = 0;
                if ((mFlags & OP_FLAG_SELF) == OP_FLAG_SELF) {
                    flags |= AppOpsManagerHidden.OP_FLAG_SELF;
                }
                if ((mFlags & OP_FLAG_TRUSTED_PROXY) == OP_FLAG_TRUSTED_PROXY) {
                    flags |= AppOpsManagerHidden.OP_FLAG_TRUSTED_PROXY;
                }
                if ((mFlags & OP_FLAG_UNTRUSTED_PROXY) == OP_FLAG_UNTRUSTED_PROXY) {
                    flags |= AppOpsManagerHidden.OP_FLAG_UNTRUSTED_PROXY;
                }
                if ((mFlags & OP_FLAG_TRUSTED_PROXIED) == OP_FLAG_TRUSTED_PROXIED) {
                    flags |= AppOpsManagerHidden.OP_FLAG_TRUSTED_PROXIED;
                }
                if ((mFlags & OP_FLAG_UNTRUSTED_PROXIED) == OP_FLAG_UNTRUSTED_PROXIED) {
                    flags |= AppOpsManagerHidden.OP_FLAG_UNTRUSTED_PROXIED;
                }

                return new HistoricalOpsRequest(mUid, mPackageName, mAttributionTag, mOpNames,
                        historyFlags, filter, mBeginTimeMillis, mEndTimeMillis, flags);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void getHistoricalOps(
            @NonNull HistoricalOpsRequest request,
            @NonNull Executor executor, @NonNull Consumer<AppOpsManagerHidden.HistoricalOps> callback) throws RemoteException {

        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(callback, "callback cannot be null");

        RemoteCallback remoteCallback = new RemoteCallback((result) -> {
            if (result == null) return;

            final AppOpsManagerHidden.HistoricalOps ops = result.getParcelable(AppOpsManagerHidden.KEY_HISTORICAL_OPS);

            final long identity = Binder.clearCallingIdentity();
            try {
                executor.execute(() -> callback.accept(ops));
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            appOps.get().getHistoricalOps(request.mUid, request.mPackageName, request.mAttributionTag, request.mOpNames, request.mHistoryFlags, request.mFilter, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, remoteCallback);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            appOps.get().getHistoricalOps(request.mUid, request.mPackageName, request.mAttributionTag, request.mOpNames, request.mFilter, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, remoteCallback);
        } else {
            appOps.get().getHistoricalOps(request.mUid, request.mPackageName, request.mOpNames, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, remoteCallback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void getHistoricalOpsFromDiskRaw(
            @NonNull HistoricalOpsRequest request,
            @NonNull Executor executor, @NonNull Consumer<AppOpsManagerHidden.HistoricalOps> callback) throws RemoteException {

        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(callback, "callback cannot be null");

        RemoteCallback remoteCallback = new RemoteCallback((result) -> {
            if (result == null) return;

            final AppOpsManagerHidden.HistoricalOps ops = result.getParcelable(AppOpsManagerHidden.KEY_HISTORICAL_OPS);

            final long identity = Binder.clearCallingIdentity();
            try {
                executor.execute(() -> callback.accept(ops));
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            appOps.get().getHistoricalOpsFromDiskRaw(request.mUid, request.mPackageName, request.mAttributionTag, request.mOpNames, request.mHistoryFlags, request.mFilter, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, remoteCallback);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            appOps.get().getHistoricalOpsFromDiskRaw(request.mUid, request.mPackageName, request.mAttributionTag, request.mOpNames, request.mFilter, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, remoteCallback);
        } else {
            appOps.get().getHistoricalOpsFromDiskRaw(request.mUid, request.mPackageName, request.mOpNames, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, remoteCallback);
        }
    }
}
