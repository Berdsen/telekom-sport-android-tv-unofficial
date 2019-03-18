package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseDetailsFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.AboutDescriptionLogoPresenter;
import de.berdsen.telekomsport_unofficial.ui.presenter.AboutDescriptionPresenter;

public class AboutFragment extends AbstractBaseDetailsFragment {

    private DetailsOverviewRow mRow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createLayout();
    }

    private void createLayout() {

        setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.app_banner));
        setTitle("Leanback Sample App");

        mRow = new DetailsOverviewRow(new Object());

        FullWidthDetailsOverviewRowPresenter detailsPresenter = new FullWidthDetailsOverviewRowPresenter(new AboutDescriptionPresenter(getContext()), new AboutDescriptionLogoPresenter());
        detailsPresenter.setBackgroundColor(Color.BLACK);
        detailsPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_FULL);

        ClassPresenterSelector mPresenterSelector = new ClassPresenterSelector();
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        ArrayObjectAdapter adapter = new ArrayObjectAdapter( mPresenterSelector );
        adapter.add(mRow);
        setAdapter(adapter);

        loadBackgroundImage();
    }
}
