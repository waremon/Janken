package org.waremon.janken;

import java.io.IOException;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Mydata extends ExtActivity implements OnClickListener {
	
	ImageButton handButton[] = new ImageButton[3];
	TextView myTextData[] = new TextView[10];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mydata);
		
        handButton[0] = (ImageButton) findViewById(R.id.top1_2_left);
        handButton[0].setOnClickListener(this);
        handButton[1] = (ImageButton) findViewById(R.id.top1_2_center);
        handButton[1].setOnClickListener(this);
        handButton[2] = (ImageButton) findViewById(R.id.top1_2_right);
        handButton[2].setOnClickListener(this);
        
        localSQLManager dbManager = new localSQLManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor c = db.query("mydata", new String[] {"gn", "cn", "pn", "gw", "cw", "pw", "tw", "cnt", "score"}, null, null, null, null, null);
        c.moveToFirst();
        int myData[] = new int[9];
        for (int i=0; i<9; i++) {
        	myData[i] = Integer.valueOf(c.getString(i));
        }
        c.close();
        db.close();
        
        myTextData[0] = (TextView) findViewById(R.id.textCon);
        myTextData[0].setText(String.valueOf(myData[8]));
        
        int totalRatio = 0;
        if (myData[7] != 0) {
        	totalRatio = ((myData[6]*100)/myData[7]);
        }
        myTextData[1] = (TextView) findViewById(R.id.textR);
        myTextData[1].setText(String.valueOf(totalRatio)+"%");
        
        myTextData[2] = (TextView) findViewById(R.id.textWin);
        myTextData[2].setText(String.valueOf(myData[6]));
        
        myTextData[3] = (TextView) findViewById(R.id.textSum);
        myTextData[3].setText(String.valueOf(myData[7]));
        
        myTextData[4] = (TextView) findViewById(R.id.textGsum);
        myTextData[4].setText(String.valueOf(myData[3]));

        int guRatio = 0;
        if (myData[0] != 0) {
        	guRatio = ((myData[3]*100)/myData[0]);
        }
        myTextData[5] = (TextView) findViewById(R.id.textGR);
        myTextData[5].setText(String.valueOf(guRatio)+"%");
        
        myTextData[6] = (TextView) findViewById(R.id.textCsum);
        myTextData[6].setText(String.valueOf(myData[4]));
        
        int chRatio = 0;
        if (myData[1] != 0) {
        	chRatio = ((myData[4]*100)/myData[1]);
        }
        myTextData[7] = (TextView) findViewById(R.id.textCR);
        myTextData[7].setText(String.valueOf(chRatio)+"%");
        
        myTextData[8] = (TextView) findViewById(R.id.textPsum);
        myTextData[8].setText(String.valueOf(myData[5]));
        
        int paRatio = 0;
        if (myData[2] != 0) {
        	paRatio = ((myData[5]*100)/myData[2]);
        }
        myTextData[9] = (TextView) findViewById(R.id.textPR);
        myTextData[9].setText(String.valueOf(paRatio)+"%");
        
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
	public void onClick(View v) {
		if (v == handButton[0]) {
		} else if (v == handButton[1]) {
			showCoins();
		} else if (v == handButton[2]) {
			startGame();
		}
	}
	
	public void startGame() {
		Intent intent = new Intent(this, Janken.class);
		startActivity(intent);
	}
	
	public void showCoins() {
		Intent intent = new Intent(this, Coins.class);
		startActivity(intent);
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
    
	public void goTop(){
		Intent intent = new Intent(this, Top.class);
		startActivity(intent);
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		} else {
			goTop();
			return false;
		}
	}
}
