package de.opatut.flatman;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {
    public static final String ACCOUNT_TYPE = "de.opatut.flatman.ACCOUNT";

    private static AccountAuthenticator sAccountAuthenticator;

    public AccountAuthenticatorService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
            return getAccountAuthenticator().getIBinder();
        return null;
    }

    private AccountAuthenticator getAccountAuthenticator() {
        if(sAccountAuthenticator == null)
            sAccountAuthenticator = new AccountAuthenticator(this);
        return  sAccountAuthenticator;
    }

}
