package com.nitishweb.app12;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.nitishweb.app12.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<String> musicList;
    private LinkedHashMap<String, Music> musicMap;
    private Music nowPlaying;
    private MediaPlayer player;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        musicList = new ArrayList<>();
        musicMap = new LinkedHashMap<>();

        populateMusicList();

        ArrayAdapter<String> musicACTVAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item, R.id.list_item, musicList);
        binding.musicACTV.setAdapter(musicACTVAdapter);

        setListeners();
    }

    private void setListeners() {
        binding.musicACTV.setOnItemClickListener((parent, view, position, id) -> {
            nowPlaying = musicMap.get(musicList.get(position));
            Log.d(TAG, "onItemSelected: " + position);
            if (nowPlaying!=null) {
                binding.nowPlayingIV.setImageDrawable(ContextCompat.getDrawable(this, nowPlaying.imageId));
                binding.nowPlayingTV.setText(nowPlaying.name);
                if (player != null) player.stop();
                player = MediaPlayer.create(this, nowPlaying.resourceId);
            } else {
                binding.nowPlayingIV.setImageDrawable(null);
                binding.nowPlayingTV.setText("");
                Snackbar.make(binding.getRoot(), "Error! Music Not Found.", Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.ibPlay.setOnClickListener(v-> {
            if (nowPlaying == null || player == null) {
                Snackbar.make(binding.getRoot(), "No Music Selected!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            player.start();
            Snackbar.make(binding.getRoot(), "Now Playing: " + nowPlaying.name, Snackbar.LENGTH_SHORT).show();
        });

        binding.ibStop.setOnClickListener(v-> {
            if (nowPlaying == null || player == null) {
                Snackbar.make(binding.getRoot(), "No Music Selected!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            player.stop();
            player = MediaPlayer.create(this, nowPlaying.resourceId);
            Snackbar.make(binding.getRoot(), "Music Stopped", Snackbar.LENGTH_SHORT).show();
        });

        binding.ibPause.setOnClickListener(v-> {
            if (nowPlaying == null || player == null) {
                Snackbar.make(binding.getRoot(), "No Music Selected!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (player.isPlaying()) {
                player.pause();
                Snackbar.make(binding.getRoot(), "Music Paused", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void populateMusicList() {
        musicMap.put("Bohemian Rhapsody",
                new Music("Bohemian Rhapsody", R.raw.bohemian_rhapsody, R.drawable.bohemian_rhapsody));

        musicMap.put("Can't Take My Eyes Off You",
                new Music("Can't Take My Eyes Off You", R.raw.cant_take_my_eyes_off_you, R.drawable.cant_take_my_eyes_off_you));

        musicMap.put("Dancing in the Moonlight",
                new Music("Dancing in the Moonlight", R.raw.dancing_in_the_moonlight, R.drawable.dancing_in_the_moonlight));

        musicMap.put("Eye of the Tiger",
                new Music("Eye of the Tiger", R.raw.eye_of_the_tiger, R.drawable.eye_of_the_tiger));

        musicMap.put("Karma Chameleon",
                new Music("Karma Chameleon", R.raw.karma_chameleon, R.drawable.karma_chameleon));

        for (LinkedHashMap.Entry<String, Music> pair: musicMap.entrySet()) {
            musicList.add(pair.getKey());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) player.stop();
    }

    class Music {
        private String name;
        private int resourceId;
        private int imageId;

        public Music(String name, int resourceId, int imageId) {
            this.name = name;
            this.resourceId = resourceId;
            this.imageId = imageId;
        }
    }
}