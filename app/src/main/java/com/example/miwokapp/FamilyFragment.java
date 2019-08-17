package com.example.miwokapp;


import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import java.util.ArrayList;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {


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

    public FamilyFragment() {
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
        words.add(new Word("lutti","GrandFather",R.drawable.family_grandfather,R.raw.family_grandfather));
        words.add(new Word("otiiko","GrandMother",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("tolooksu","Father",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("oyisi","Mother",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("masskki","Older Brother",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("temoka","Older Sister",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("kenakaku","Daughter",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("kawinta","Son",R.drawable.family_son,R.raw.family_son));


        WordAdapter adapter=new WordAdapter(getActivity(),words,R.color.category_family);

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


        //TextView textView = new TextView(getActivity());
       // textView.setText(R.string.hello_blank_fragment);
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
