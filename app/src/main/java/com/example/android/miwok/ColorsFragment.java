package com.example.android.miwok;


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


    public ColorsFragment() {
        // Required empty public constructor
    }

    private MediaPlayer mediaplayer;
    private MediaPlayer.OnCompletionListener mCompletelistener=  new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    private AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener mAudioFocusChange = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mediaplayer.pause();
                mediaplayer.seekTo(0);
            }
            else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                mediaplayer.start();
            }
            else if(focusChange==AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
    private void releaseMediaPlayer() {
        if (mediaplayer != null) {
            mediaplayer.release();
            mediaplayer = null;
            mAudioManager.abandonAudioFocus(mAudioFocusChange);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
        View rootView = inflater.inflate(R.layout.word_list, container, false);
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> num = new ArrayList<Word>();
        num.add(new Word("Red","Rojo",R.drawable.color_red,R.raw.red));
        num.add(new Word("Black","Nergo",R.drawable.color_black,R.raw.black));
        num.add(new Word("White","Blanco",R.drawable.color_white,R.raw.white));
        num.add(new Word("Yellow","Amarillo",R.drawable.color_mustard_yellow,R.raw.yellow));
        num.add(new Word("Purple","Morado",R.drawable.color_green,R.raw.purple));
        num.add(new Word("Orange","Naranja",R.drawable.color_dusty_yellow,R.raw.orange));
        num.add(new Word("Pink","Roja",R.drawable.color_red,R.raw.pink));
        num.add(new Word("Brown","Maroon",R.drawable.color_brown,R.raw.brown));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), num,R.color.category_colors);

        ListView listView = (ListView)rootView.findViewById(R.id.list);


        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(NumbersActivity.this,"Play",Toast.LENGTH_SHORT).show();
                Word word = num.get(position);

                releaseMediaPlayer();
                int result = mAudioManager.requestAudioFocus(mAudioFocusChange,
                        AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
//                    mAudioManager.registerMediaButtonEventReceiver(RemoteControl);

                    mediaplayer = MediaPlayer.create(getActivity(),word.getSound());
                    mediaplayer.start();
                    mediaplayer.setOnCompletionListener(mCompletelistener);
                }
            }
        });
        return rootView;
    }

}
