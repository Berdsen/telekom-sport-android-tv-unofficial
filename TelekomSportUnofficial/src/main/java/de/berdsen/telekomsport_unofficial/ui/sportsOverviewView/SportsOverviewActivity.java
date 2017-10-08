package de.berdsen.telekomsport_unofficial.ui.sportsOverviewView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.services.interfaces.LoginFinishedHandler;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseActivity;

/**
 * Created by berthm on 28.09.2017.
 */

public class SportsOverviewActivity extends AbstractBaseActivity {

    @Inject
    SessionService sessionService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_overview);

        sessionService.loginAsync(new LoginFinishedHandler() {
            @Override
            public void loginFailed() {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void loginSucceeded() {
                Toast.makeText(context, "Login Succeeded", Toast.LENGTH_LONG).show();
            }
        });
    }

}
