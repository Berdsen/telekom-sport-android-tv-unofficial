package de.berdsen.telekomsport_unofficial.ui.preferenceView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;

import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseGuidedStepFragment;

/**
 * Created by berthm on 09.10.2017.
 */

public class PreferenceFragment extends AbstractBaseGuidedStepFragment
{
    private final int ACTION_SAVE = 0;
    private final int ACTION_CANCEL = 1;
    private final int ACTION_RESET = 2;

    private final int ACTION_SET_USERNAME = 10;
    private final int ACTION_SET_PASSWORD = 11;

    private static final String PREFERENCES_LOGIN_USERNAME = "__USERNAME__";
    private static final String PREFERENCES_LOGIN_PASSWORD = "__PASSWORD__";

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.settingsTitle);
        String breadcrumb = getString(R.string.settingsBreadcrumb);
        String description = getString(R.string.settingsDescription);

        return new GuidanceStylist.Guidance(title, description, breadcrumb, getActivity().getDrawable(R.drawable.app_banner));
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        String username = sharedPreferences.getString(PREFERENCES_LOGIN_USERNAME, getString(R.string.emptyString));
        String password = sharedPreferences.getString(PREFERENCES_LOGIN_PASSWORD, getString(R.string.emptyString));

        addEditableAction(actions, ACTION_SET_USERNAME, getString(R.string.settingsUsername), username, InputType.TYPE_CLASS_TEXT);
        addEditableDescriptionAction(actions, ACTION_SET_PASSWORD, getString(R.string.settingsPassword), getString(R.string.settingsPassword), password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void onCreateButtonActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        super.onCreateButtonActions(actions,savedInstanceState);

        addAction(actions, ACTION_SAVE, getString(R.string.settingsSave), getString(R.string.emptyString));
        addAction(actions, ACTION_CANCEL, getString(R.string.settingsCancel), getString(R.string.emptyString));
        addAction(actions, ACTION_RESET, getString(R.string.settingsReset), getString(R.string.emptyString));
    }

    private void addAction(List<GuidedAction> actions, long id, String title, String description) {
        actions.add(new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .description(description)
                .build());
    }

    private void addEditableAction(List<GuidedAction> actions, long id, String title, String description, int inputType) {
        actions.add(new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .descriptionEditable(true)
                .description(description)
                .descriptionInputType(inputType)
                .build());
    }

    private void addEditableDescriptionAction(List<GuidedAction> actions, long id, String title, String desc, String editDescription, int descriptionEditInputType) {
        actions.add(new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .description(desc)
                .editDescription(editDescription)
                .descriptionEditInputType(descriptionEditInputType)
                .descriptionEditable(true)
                .build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        int actionId = (int)action.getId();

        switch (actionId) {
            case ACTION_SAVE:
                saveSettings();
                closeView();
                break;
            case ACTION_CANCEL:
                closeView();
                break;
            case ACTION_RESET:
                //TODO: ask user - create an GuidedStepFragment and replace active one
                resetSettings();
                closeView();
                break;
        }

    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        GuidedAction usernameAction = findActionById(ACTION_SET_USERNAME);
        GuidedAction passwordAction = findActionById(ACTION_SET_PASSWORD);

        CharSequence username = usernameAction.getDescription();
        CharSequence password = passwordAction.getEditDescription();

        editor.putString(PREFERENCES_LOGIN_USERNAME, username.toString());
        editor.putString(PREFERENCES_LOGIN_PASSWORD, password.toString());

        editor.commit();
    }

    private void resetSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor = sharedPreferences.edit();
        editor.remove(PREFERENCES_LOGIN_USERNAME);
        editor.remove(PREFERENCES_LOGIN_PASSWORD);
        editor.commit();
    }

    private void closeView() {
        this.getActivity().finish();
    }
}
