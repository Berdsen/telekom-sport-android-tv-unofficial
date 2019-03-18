package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.AndroidApplication;
import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseGuidedStepFragment;
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;

/**
 * Created by Berdsen on 09.10.2017.
 */

public class SettingsFragment extends AbstractBaseGuidedStepFragment
{
    private final int ACTION_SAVE = 0;
    private final int ACTION_CANCEL = 1;
    private final int ACTION_RESET = 2;

    private final int ACTION_SET_USERNAME = 10;
    private final int ACTION_SET_PASSWORD = 11;
    private final int ACTION_SET_LANGUAGE = 12;

    private final int ACTION_SET_LANGUAGE_GERMAN = 20;
    private final int ACTION_SET_LANGUAGE_ENGLISH = 21;

    private final String GERMAN_LOCALE = "de";
    private final String ENGLISH_LOCALE = "en";

    private final Map<String, String> LANGUAGES = new HashMap<String, String>() {
        {
            put(GERMAN_LOCALE, "German");
            put(ENGLISH_LOCALE, "English");
        }
    };

    private GuidedAction languageAction = null;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    AndroidApplication app;

    private String currentLanguage;

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
        String username = sharedPreferences.getString(ApplicationConstants.PREFERENCES_LOGIN_USERNAME, getString(R.string.emptyString));
        String password = sharedPreferences.getString(ApplicationConstants.PREFERENCES_LOGIN_PASSWORD, getString(R.string.emptyString));
        currentLanguage = sharedPreferences.getString(ApplicationConstants.PREFERENCES_LANGUAGE, ENGLISH_LOCALE);

        addEditableAction(actions, ACTION_SET_USERNAME, getString(R.string.settingsUsername), username, InputType.TYPE_CLASS_TEXT);
        addEditableDescriptionAction(actions, ACTION_SET_PASSWORD, getString(R.string.settingsPassword), getString(R.string.settingsPassword), password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        List<GuidedAction> languageActions = new ArrayList<>();

        addAction(languageActions, ACTION_SET_LANGUAGE_GERMAN, LANGUAGES.get(GERMAN_LOCALE),"");
        addAction(languageActions, ACTION_SET_LANGUAGE_ENGLISH, LANGUAGES.get(ENGLISH_LOCALE),"");
        addDropDownAction(actions, ACTION_SET_LANGUAGE,getString(R.string.settings_LanguageTitle), LANGUAGES.containsKey(currentLanguage) ? LANGUAGES.get(currentLanguage) : LANGUAGES.get(ENGLISH_LOCALE), languageActions);
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

    private void addDropDownAction(List<GuidedAction> actions,long id, String title, String desc,List<GuidedAction> selectionActions){
        languageAction = new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .description(desc)
                .subActions(selectionActions)
                .build();
        actions.add(languageAction);
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

    @Override
    public boolean onSubGuidedActionClicked(GuidedAction action) {
        int idx = findActionPositionById(ACTION_SET_LANGUAGE);

        switch ((int)action.getId())
        {
            case ACTION_SET_LANGUAGE_ENGLISH:
                currentLanguage = ENGLISH_LOCALE;
                break;
            case ACTION_SET_LANGUAGE_GERMAN:
                currentLanguage = GERMAN_LOCALE;
                break;
            default:
                currentLanguage = ENGLISH_LOCALE;
                break;
        }

        languageAction.setDescription(LANGUAGES.get(currentLanguage));
        notifyActionChanged(idx);
        return super.onSubGuidedActionClicked(action);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        GuidedAction usernameAction = findActionById(ACTION_SET_USERNAME);
        GuidedAction passwordAction = findActionById(ACTION_SET_PASSWORD);

        CharSequence username = usernameAction.getDescription();
        CharSequence password = passwordAction.getEditDescription();

        CharSequence language = sharedPreferences.getString(ApplicationConstants.PREFERENCES_LANGUAGE, ENGLISH_LOCALE);

        editor.putString(ApplicationConstants.PREFERENCES_LOGIN_USERNAME, username.toString());
        editor.putString(ApplicationConstants.PREFERENCES_LOGIN_PASSWORD, password.toString());
        editor.putString(ApplicationConstants.PREFERENCES_LANGUAGE, currentLanguage);

        editor.commit();

        if (currentLanguage != language) {
            app.restartApplication(getActivity());
        }

    }

    private void resetSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(ApplicationConstants.PREFERENCES_LOGIN_USERNAME);
        editor.remove(ApplicationConstants.PREFERENCES_LOGIN_PASSWORD);
        editor.remove(ApplicationConstants.PREFERENCES_LANGUAGE);

        editor.commit();
    }

    private void closeView() {
        getFragmentManager().popBackStack();
    }
}
