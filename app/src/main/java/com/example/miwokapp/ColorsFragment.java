package com.example.miwokapp;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private MediaPlayer mediaPlayer;

    private AudioManager maudioManager;

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        releaseMediaPlayer();

                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||focusChange== AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);

                    }else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        mediaPlayer.start();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompleteListner=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };



    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.word_list, container, false);

        maudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words=new ArrayList<Word>();

        //words.add("one");
        words.add(new Word("lutti","Black",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("otiiko","Brown",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("tolooksu","Gray",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("oyisi","Dusty Yellow",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("masskki","Green",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("temoka","Musterd Yello",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        words.add(new Word("kenakaku","Red",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("kawinta","White",R.drawable.color_white,R.raw.color_white));


        WordAdapter adapter=new WordAdapter(getActivity(),words,R.color.category_colours);

        ListView listView=(ListView)rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word=words.get(position);
                releaseMediaPlayer();

                //Request audio focus for playback
                int result = maudioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback


                    mediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourseId());
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mCompleteListner);

                }
            }
        });


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //When the activity is stopped,release the media player resource because we won't be playing any more
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            maudioManager.abandonAudioFocus(afChangeListener);
        }
    }


}
