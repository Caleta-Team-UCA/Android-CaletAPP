package com.caletateam.caletapp.app.md.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.utils.Functions;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link streamming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class streamming extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView streaming;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // creating a variable for exoplayerview.
    PlayerView playerView;

    // creating a variable for exoplayer
    SimpleExoPlayer exoPlayer;

    // url of video which we are loading.
    String videoURL = "rtsp://vai.uca.es:1935/mystream";

    public void setImage(byte[] buffer){
        Bitmap bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        streaming.setImageBitmap(bmp);
    }

    public streamming() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment streamming.
     */
    // TODO: Rename and change types and number of parameters
    public static streamming newInstance(String param1, String param2) {
        streamming fragment = new streamming();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        initRTSPlayer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_streamming, container, false);
        //webview = v.findViewById(R.id.webview);
       // streaming = v.findViewById(R.id.textstreaming);
        playerView = v.findViewById(R.id.simple_player);
        //webview.loadUrl(Functions.HOST_URL+"oak");
        //String data ="<!doctype html> <html lang='en'> <head> <meta charset='utf-8'> <meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'> <title>Caleta Live Streaming</title> </head> <body> <div style='width:100%; height:100%'> <img src='"+Functions.HOST_URL+"video_feed' width='100%'> </div> </body> </html>";
        //webview.loadData(data, "text/html", null);

        return v;
    }

    public void initRTSPlayer(){


        /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

       // PlayerView playerView = findViewById(R.id.simple_player);

        playerView.setPlayer(player);

        // Create RTMP Data Source
        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();

        MediaSource videoSource = new ExtractorMediaSource
                .Factory(rtmpDataSourceFactory)
                .createMediaSource(Uri.parse(videoURL));

        player.prepare(videoSource);
        player.setPlayWhenReady(true);*/

        MediaSource mediaSource =
                new RtspMediaSource.Factory()
                        .createMediaSource(MediaItem.fromUri(videoURL));
// Create a player instance.
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(getActivity()).build();
// Set the media source to be played.
        player.setMediaSource(mediaSource);
        //PlayerView playerView = findViewById(R.id.simple_player);

        playerView.setPlayer(player);
// Prepare the player.
        player.prepare();
        // Create RTMP Data Source
        //RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();

        /*MediaSource mediaSource =
                new RtspMediaSource.Factory()
                        .createMediaSource(MediaItem.fromUri("rtsp://192.168.0.17:8554/mystream"));
// Create a player instance.
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
// Set the media source to be played.
        player.setMediaSource(mediaSource);
// Prepare the player.
        player.prepare();*/





        player.setPlayWhenReady(true);

    }
}