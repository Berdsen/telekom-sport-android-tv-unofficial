package de.berdsen.telekomsport_unofficial.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ProgressBar;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseActivity;
import de.berdsen.telekomsport_unofficial.ui.fragments.SportsOverviewFragment;

/**
 * Created by Berdsen on 28.09.2017.
 */

public class MainActivity extends AbstractBaseActivity {

    @Inject
    SessionService sessionService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.add(R.id.sportsOverviewContainer, new SportsOverviewFragment());

        // Commit the transaction
        transaction.commit();

        setContentView(R.layout.activity_sports_overview);

        app.initializeSpinner((ProgressBar)findViewById(R.id.appSpinner));

        app.doLogin(sessionService, context);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        final FragmentActivity mainActivity = this;

        if (fragmentManager.getBackStackEntryCount() == 0) {
            AlertDialog dialog = new AlertDialog.Builder(mainActivity)
                    .setTitle(R.string.exit_question_title)
                    .setMessage(R.string.exit_question_message)
                    .setPositiveButton(R.string.globalYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mainActivity.finish();
                        }
                    })
                    .setNegativeButton(R.string.globalNo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Do nothing
                        }
                    })
                    .create();

            dialog.show();

        } else {
            super.onBackPressed();
        }
    }
}
