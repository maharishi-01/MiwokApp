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
public class PhrasesFragment extends Fragment {

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



    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        maudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        //final AudioManager.OnAudioFocusChangeListener afChangeListener;


        final ArrayList<Word> words=new ArrayList<Word>();

        //words.add("one");
        words.add(new Word("lutti","one",R.raw.phrase_are_you_coming));
        words.add(new Word("otiiko","two",R.raw.phrase_come_here));
        words.add(new Word("tolooksu","three",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("oyisi","four",R.raw.phrase_im_coming));
        words.add(new Word("masskki","five",R.raw.phrase_im_feeling_good));
        words.add(new Word("temoka","six",R.raw.phrase_lets_go));
        words.add(new Word("kenakaku","seven",R.raw.phrase_my_name_is));
        words.add(new Word("kawinta","eight",R.raw.phrase_what_is_your_name));




        WordAdapter adapter=new WordAdapter(getActivity(),words,R.color.category_Phrases);

        ListView listView=(ListView)rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Set a click listener to play the audio when the list item is clicked on listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);

                //release the media player if it currently exists because we are about to play a different sound file
                releaseMediaPlayer();

                //Request audio focus for playback
                int result = maudioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback


                    // Create and setup the {@link MediaPlayer} for the audio resource associated with the current word
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourseId());

                    // Start the audio file
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
