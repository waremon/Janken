package org.waremon.janken;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;


public class ExtActivity extends Activity {
	
	static protected MediaPlayer home;
	static protected MediaPlayer game;
	static protected MediaPlayer win;
	static protected MediaPlayer lose1;
	static protected MediaPlayer lose2;
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		if(home == null) {
			home = MediaPlayer.create(this, R.raw.home);
			home.setLooping(true);
		}
		if(game == null) {
			game = MediaPlayer.create(this, R.raw.janken);
			game.setLooping(true);
		}
		if(win == null) {
			win = MediaPlayer.create(this, R.raw.fanfare);
		}
		if(lose1 == null) {
			lose1 = MediaPlayer.create(this, R.raw.laugh1);
		}
		if(lose2 == null) {
			lose2 = MediaPlayer.create(this, R.raw.laugh2);
		}
	}
}
