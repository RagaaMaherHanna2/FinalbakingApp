package com.example.marian.finalbakingapp.fragment;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.activity.StepDetailActivity;
import com.example.marian.finalbakingapp.activity.StepListActivity;
import com.example.marian.finalbakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.marian.finalbakingapp.activity.StepListActivity.PANES;
import static com.example.marian.finalbakingapp.activity.StepListActivity.POSITION;
import static com.example.marian.finalbakingapp.activity.StepListActivity.STEPS;

/**
 * A fragment representing a single step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class stepDetailFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener
{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    //@BindView(R.id.step_detail) TextView StepDetail;
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
//    private DummyContent.DummyItem mItem;

    private ArrayList<Step> steps;
    private boolean isTab;

    private long playbackPosition;
    private int currentWindow;
    private boolean autoPlay = true;

    public static final String PLAYBACK_POSITION = "playback_position";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String AUTOPLAY = "autoplay";


    @BindView(R.id.player)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.step_description)
    TextView stepDescription;
    @BindView(R.id.iv_thumbnail)
    ImageView thumbnail;
    @BindView(R.id.next_btn)
    Button next;
    @BindView(R.id.previous_btn)
    Button previous;

    private int position;
    private int index;

    private SimpleExoPlayer simpleExoPlayer;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private TrackSelector trackSelector;

    private static long Position = 0;




    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public stepDetailFragment()
    {
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//
//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            steps = savedInstanceState.getParcelableArrayList(STEPS);
            isTab = savedInstanceState.getBoolean(PANES);

            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION,0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX,0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, true);

        }

        View rootView = inflater.inflate(R.layout.step_detail_fragment, container, false);
        ButterKnife.bind(this,rootView);

        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        position = getArguments().getInt(POSITION);
        index = position;
        isTab = getArguments().getBoolean(PANES);


        return rootView;
    }

    private void CheckPlayer(long position, boolean playWhenReady)
    {
        this.Position = position;
        simpleExoPlayer.seekTo(position);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
    }

    private void initPlayer(Uri videoURI)
    {
        simpleExoPlayer = null;

        DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(getActivity(),
               null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF );

        TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(factory);
        LoadControl loadControl = new DefaultLoadControl();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(defaultRenderersFactory, trackSelector, loadControl);

        simpleExoPlayer.addListener(this);

        exoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.seekTo(currentWindow, playbackPosition);

        String userAgent = Util.getUserAgent(getActivity(), "Baking App");

        MediaSource mediaSource = new ExtractorMediaSource(videoURI,
                new DefaultDataSourceFactory(getActivity(), BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER)),
                new DefaultExtractorsFactory(), null, null);

        simpleExoPlayer.prepare(mediaSource);
        CheckPlayer(Position, false);

    }

    private void playingVideos(int index)
    {
        exoPlayerView.setVisibility(View.VISIBLE);
        exoPlayerView.requestFocus();

        String videoURL = steps.get(index).getVideoUrl();
        String thumbNailURL = steps.get(index).getThumbnailUrl();

        if (!videoURL.isEmpty())
        {
            initPlayer(Uri.parse(videoURL));
        }
        else if (!thumbNailURL.isEmpty())
        {
            Picasso.with(getContext())
                    .load(Uri.parse(thumbNailURL))
                    .error(R.mipmap.ic_launcher).into(thumbnail);
            thumbnail.setVisibility(View.VISIBLE);
        }
        else
        {
            exoPlayerView.setVisibility(View.GONE);

        }

    }
    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.next_btn:
                if(index < steps.size()-1)
                {
                    int nextIndex = ++index;
                    stepDescription.setText(steps.get(nextIndex).getDescription());

                    playingVideos(nextIndex);

                }
                else
                {
                    Toast.makeText(getActivity(), R.string.end, Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.previous_btn:
                if (index > 0) {
                    int previousIndex = --index;
                    stepDescription.setText(steps.get(index).getDescription());
                    // CheckPlayer(0, false);
                    playingVideos(index);
                } else {
                    Toast.makeText(getActivity(), R.string.back, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
    {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady)
        {
            Toast.makeText(getActivity(), "It's Working :D", Toast.LENGTH_LONG).show();
        }
        else if ((playbackState == ExoPlayer.STATE_READY))
        {}

        if (playbackState == PlaybackStateCompat.STATE_PLAYING)
        {
            Position = simpleExoPlayer.getCurrentPosition();
        }

    }

    @Override
    public void onPlayerError(ExoPlaybackException error)
    {
        String errorMsg = null;

        if (error.type == ExoPlaybackException.TYPE_RENDERER)
        {
            Exception cause = error.getRendererException();

            if (cause instanceof MediaCodecRenderer.DecoderInitializationException)
            {
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;

                if (decoderInitializationException.decoderName == null)
                {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException)
                    {
                        errorMsg = getString(R.string.error_querying_decoders);
                    }
                    else if (decoderInitializationException.secureDecoderRequired)
                    {
                        errorMsg = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    }
                    else
                    {
                        errorMsg = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                }
                else
                {
                    errorMsg = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorMsg != null) {
            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private void ItIsTablet()
    {
        exoPlayerView.setVisibility(View.VISIBLE);
        stepDescription.setVisibility(View.VISIBLE);
        steps = getArguments().getParcelableArrayList(STEPS);
        assert steps!=null;
        playingVideos(index);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi()
    {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    void inLandscapeMode()
    {
        if (getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE)
            hideSystemUi();
    }

    private void ItIsPhone()
    {
        ItIsTablet();
        inLandscapeMode();
        previous.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (isTab)
        {ItIsTablet();}
        else
        {ItIsPhone();}
    }

    private void ItWorks()
    {
        if (simpleExoPlayer != null)
        {

           simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
        ItWorks();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        ItWorks();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ItWorks();
    }
}

