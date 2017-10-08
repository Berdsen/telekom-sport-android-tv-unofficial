package de.berdsen.telekomsport_unofficial.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.VerticalGridPresenter;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by Berdsen on 08.10.2017.
 */

public abstract class AbstractBaseVerticalGridFragment extends VerticalGridFragment {

    @Inject
    protected Context context;

    public final int NUM_COLUMNS;

    protected VerticalGridPresenter presenter;

    public AbstractBaseVerticalGridFragment() {
        this(4);
    }

    public AbstractBaseVerticalGridFragment(int numColumns) {
        NUM_COLUMNS = numColumns;

        presenter = new VerticalGridPresenter();
        presenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
