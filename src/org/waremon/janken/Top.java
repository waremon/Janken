package org.waremon.janken;

import java.io.IOException;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Top extends ExtActivity implements OnClickListener {
	
	ImageButton handButton[] = new ImageButton[3];
	ImageButton soundButton;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_top);
        
        handButton[0] = (ImageButton) findViewById(R.id.top1_2_left);
        handButton[0].setOnClickListener(this);
        handButton[1] = (ImageButton) findViewById(R.id.top1_2_center);
        handButton[1].setOnClickListener(this);
        handButton[2] = (ImageButton) findViewById(R.id.top1_2_right);
        handButton[2].setOnClickListener(this);
        
        soundButton = (ImageButton) findViewById(R.id.sound);
        if(Global.isSoundOn == 1) {
        	soundButton.setImageResource(getResources().getIdentifier("soundon", "drawable", getPackageName()));
        } else {
        	soundButton.setImageResource(getResources().getIdentifier("soundoff", "drawable", getPackageName()));
        }
        soundButton.setOnClickListener(this);
        
		// create adView
		AdView adView = new AdView(this, AdSize.BANNER, "a150e963fb8884a");
		// get View for ad
		LinearLayout layout = (LinearLayout)findViewById(R.id.adlayout);
		// add adView
		layout.addView(adView);
		// request ad
		AdRequest request = new AdRequest();
		adView.loadAd(request);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if(hasFocus) {
    		home.start();
    	} else {
    		home.stop();
    		try {
    			home.prepare();
    		} catch (IllegalStateException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }

	@Override
	public void onClick(View v) {
		if (v == handButton[0]) {
			showMyData();
		} else if (v == handButton[1]) {
			showCoins();
		} else if (v == handButton[2]) {
			startGame();
		} else if (v == soundButton) {
			if(Global.isSoundOn == 1) {
				soundButton.setImageResource(getResources().getIdentifier("soundoff", "drawable", getPackageName()));
				Global.isSoundOn = 0;
				home.setVolume(0, 0);
				game.setVolume(0, 0);
				win.setVolume(0, 0);
				lose1.setVolume(0, 0);
				lose2.setVolume(0, 0);
			} else {
				soundButton.setImageResource(getResources().getIdentifier("soundon", "drawable", getPackageName()));
				Global.isSoundOn = 1;
				home.setVolume(1, 1);
				game.setVolume(1, 1);
				win.setVolume(1, 1);
				lose1.setVolume(1, 1);
				lose2.setVolume(1, 1);
			}
		}
	}
	
	public void startGame() {
		Intent intent = new Intent(this, Janken.class);
		startActivity(intent);
	}
	
	public void showMyData() {
		Intent intent = new Intent(this, Mydata.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		} else {
			this.finish();
			this.moveTaskToBack(true);
			return false;
		}
	}
		
	public void showCoins() {
		Intent intent = new Intent(this, Coins.class);
		startActivity(intent);
	}
}
