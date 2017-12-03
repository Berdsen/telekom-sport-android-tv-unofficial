package de.berdsen.telekomsport_unofficial.utils;

import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;

/**
 * Created by berthm on 03.12.2017.
 */

public class TrackSelectionHelper {
    private final MappingTrackSelector selector;
    private final TrackSelection.Factory adaptiveTrackSelectionFactory;

    public TrackSelectionHelper(MappingTrackSelector selector, TrackSelection.Factory adaptiveTrackSelectionFactory) {
        this.selector = selector;
        this.adaptiveTrackSelectionFactory = adaptiveTrackSelectionFactory;
    }
}
