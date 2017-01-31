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
public class NumbersFragment extends Fragment {


    public NumbersFragment() {
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
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.word_list);
//
//
//
//    }

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
        num.add(new Word("One","Uno",R.drawable.number_one,R.raw.one));
        num.add(new Word("Two","Dos",R.drawable.number_two,R.raw.two));
        num.add(new Word("Three","Tres",R.drawable.number_three,R.raw.three));
        num.add(new Word("Four","Cuatro",R.drawable.number_four,R.raw.four));
        num.add(new Word("Five","Cinco",R.drawable.number_five,R.raw.five));
        num.add(new Word("Six","Seis",R.drawable.number_six,R.raw.six));
        num.add(new Word("Seven","Siete",R.drawable.number_seven,R.raw.seven));
        num.add(new Word("Eight","Ocho",R.drawable.number_eight,R.raw.eight));
        num.add(new Word("Nine","Nueve",R.drawable.number_nine,R.raw.nine));
        num.add(new Word("Ten","Diez",R.drawable.number_ten,R.raw.ten));
        WordAdapter itemsAdapter = new WordAdapter(getActivity(), num, R.color.category_numbers);

        ListView listView = (ListView)rootView.findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = num.get(position);
                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mAudioFocusChange,
                        AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    mediaplayer = MediaPlayer.create(getActivity(),word.getSound());
                    mediaplayer.start();
                    mediaplayer.setOnCompletionListener(mCompletelistener);
                }


            }
        });
        return rootView;


    }

}
