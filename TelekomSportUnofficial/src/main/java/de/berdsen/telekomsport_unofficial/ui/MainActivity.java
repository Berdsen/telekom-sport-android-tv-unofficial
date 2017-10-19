package de.berdsen.telekomsport_unofficial.ui;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.LoginFinishedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseActivity;
import de.berdsen.telekomsport_unofficial.ui.sportsOverviewView.SportsOverviewFragment;

/**
 * Created by berthm on 28.09.2017.
 */

public class MainActivity extends AbstractBaseActivity {

    @Inject
    SessionService sessionService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.add(R.id.sportsOverviewContainer, new SportsOverviewFragment());

        // Commit the transaction
        transaction.commit();

        setContentView(R.layout.activity_sports_overview);

        //doLogin();
    }

    private void doLogin() {
        sessionService.loginAsync(new LoginFinishedHandler() {
            @Override
            public void loginFailed() {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void loginSucceeded() {
                Toast.makeText(context, "Login Succeeded", Toast.LENGTH_LONG).show();
            }
        }, false);
    }

}