package com.ycw.blu.videoaudio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    // UI component
    private VideoView myVideoView;
    private Button btnPlayVideo, playMusic, pauseMusic;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private SeekBar volumeSeekbar, moveBackAndForth;
    private AudioManager audioManager;

    private Timer timer;
    @Override
    public void onClick(View buttonView) {
        switch (buttonView.getId()) {
            case R.id.btnPlayVideo:
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_1280x720_1mb);

                myVideoView.setVideoURI(videoUri);

                myVideoView.setMediaController(mediaController);
                mediaController.setAnchorView(myVideoView);

                myVideoView.start();
                break;
            case R.id.playMusic:
                mediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        moveBackAndForth.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }, 0, 1000);
                break;
            case R.id.pauseMusic:
                mediaPlayer.pause();
                timer.cancel();
                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
//            Toast.makeText(this,""+Integer.toString(progress),Toast.LENGTH_LONG).show();
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        Toast.makeText(this,"Music Stop",Toast.LENGTH_LONG).show();
        timer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);

        btnPlayVideo.setOnClickListener(MainActivity.this);
        mediaController = new MediaController(MainActivity.this);

        playMusic = findViewById(R.id.playMusic);
        pauseMusic = findViewById(R.id.pauseMusic);

        playMusic.setOnClickListener(MainActivity.this);
        pauseMusic.setOnClickListener(MainActivity.this);

        mediaPlayer = MediaPlayer.create(this, R.raw.catsound);

        // volume
        volumeSeekbar = findViewById(R.id.seekBarVolume);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maximumVolumeOfUserDevice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumeOfUserDevice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekbar.setMax(maximumVolumeOfUserDevice);
        volumeSeekbar.setProgress(currentVolumeOfUserDevice);

        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
//                    Toast.makeText(MainActivity.this, Integer.toString(progress), Toast.LENGTH_SHORT).show();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // move back and forth
        moveBackAndForth = findViewById(R.id.seekBarMoveBackForth);
        moveBackAndForth.setOnSeekBarChangeListener(MainActivity.this);
        moveBackAndForth.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(MainActivity.this);

    }
}
