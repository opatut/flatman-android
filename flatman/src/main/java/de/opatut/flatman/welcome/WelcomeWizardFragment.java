package de.opatut.flatman.welcome;

import android.support.v4.app.Fragment;

import de.opatut.flatman.WelcomeWizardActivity;

public abstract class WelcomeWizardFragment extends Fragment {
    public abstract String getTitle();

    public int getBackPage() {
        return WelcomeWizardActivity.PAGE_CANCEL;
    };
}
