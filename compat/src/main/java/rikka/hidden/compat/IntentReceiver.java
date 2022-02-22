package rikka.hidden.compat;

import android.content.IIntentReceiver;
import android.content.Intent;
import android.os.Bundle;

public class IntentReceiver extends IIntentReceiver.Stub {

    @Override
    public void performReceive(Intent intent, int resultCode, String data, Bundle extras,
                               boolean ordered, boolean sticky, int sendingUser) {
    }
}
