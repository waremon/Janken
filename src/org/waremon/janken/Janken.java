package org.waremon.janken;

import java.io.IOException;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Janken extends ExtActivity implements OnClickListener {

	AnimationDrawable animation;
	ImageButton handButton[] = new ImageButton[3];
	ImageButton soundButton;
	Boolean anim_flag=true;
	ImageView koushin;
	ImageView image;
	ImageView stars;
	ImageView which;
	int animNumber;
	int myNumber;
	String myResult;
	int conWin = 0;
	int koushinFlag = 0;
	int myData[] = new int[9];
	int isPushed = 0;
	String[] hand = {"gu", "ch", "pa"};
	Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_janken);

		localSQLManager dbManager = new localSQLManager(this);
		SQLiteDatabase db = dbManager.getReadableDatabase();
		Cursor c = db.query("mydata", new String[] {"gn", "cn", "pn", "gw", "cw", "pw", "tw", "cnt", "score"}, null, null, null, null, null);
		c.moveToFirst();
		for (int i=0; i<9; i++) {
			myData[i] = Integer.valueOf(c.getString(i));
		}
		c.close();
		db.close();

		image = (ImageView) findViewById(R.id.janken_animation);
		image.setBackgroundResource(R.drawable.janken_amin);
		animation = (AnimationDrawable) image.getBackground();

		stars = (ImageView) findViewById(R.id.stars);
		stars.setImageResource(getResources().getIdentifier("star"+conWin, "drawable", getPackageName()));

		which = (ImageView) findViewById(R.id.which);
		which.setImageResource(getResources().getIdentifier("star0", "drawable", getPackageName()));

		koushin = (ImageView) findViewById(R.id.koushin);
		koushin.setImageResource(getResources().getIdentifier("star0", "drawable", getPackageName()));

		handButton[0] = (ImageButton) findViewById(R.id.main1_2_left);
		handButton[0].setOnClickListener(this);
		handButton[1] = (ImageButton) findViewById(R.id.main1_2_center);
		handButton[1].setOnClickListener(this);
		handButton[2] = (ImageButton) findViewById(R.id.main1_2_right);
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
		super.onWindowFocusChanged(hasFocus);
		image.setBackgroundResource(R.drawable.janken_amin);
		animation = (AnimationDrawable) image.getBackground();
		animation.start();
		if(hasFocus) {
			home.stop();
			try {
				home.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			home.seekTo(0);
			game.seekTo(0);
			game.start();
		} else {
			game.stop();
			try {
				game.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == soundButton) {
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
		} else {

			if (isPushed == 0) {
				isPushed = 1;
				for (int i=0; i<3; i++) {
					if (v == handButton[i]) {
						myNumber = i;
					}
				}

				if (myNumber == 0) {
					handButton[0].setImageResource(R.drawable.gubutton_r);
				} else if (myNumber == 1) {
					handButton[1].setImageResource(R.drawable.chbutton_r);
				} else if (myNumber == 2) {
					handButton[2].setImageResource(R.drawable.pabutton_r);
				}

				animNumber = (int) (Math.random() * 2.9999);
				getWinorNot(animNumber, myNumber);
				koushin.setImageResource(getResources().getIdentifier("star0", "drawable", getPackageName()));
				if (myData[8] < conWin) {
					koushinFlag = 1;
				} else if (conWin == 0) {
					koushinFlag = 0;
				}
				if (koushinFlag == 1) {
					koushin.setImageResource(getResources().getIdentifier("koushin", "drawable", getPackageName()));
				}

				//db uppdate
				//which hand
				//win?
				updatedb(myNumber, myResult, conWin, koushinFlag);

				animation.stop();
				if (animNumber == 0) {
					image.setBackgroundResource(R.drawable.gu);
				} else if (animNumber == 1) {
					image.setBackgroundResource(R.drawable.ch);
				} else {
					image.setBackgroundResource(R.drawable.pa);
				}

				which.setImageResource(getResources().getIdentifier(myResult, "drawable", getPackageName()));

				handler.postDelayed(new Runnable(){
					public void run(){
						handButton[0].setImageResource(R.drawable.gubutton);
						handButton[1].setImageResource(R.drawable.chbutton);
						handButton[2].setImageResource(R.drawable.pabutton);
						which.setImageResource(getResources().getIdentifier("star0", "drawable", getPackageName()));
						image.setBackgroundResource(R.drawable.janken_amin);
						animation = (AnimationDrawable) image.getBackground();
						animation.start();
						isPushed = 0;
					}
				}, 1000);
			}
		}
	}

	public void resetButtons() {
		handButton[0].setImageResource(R.drawable.gubutton);
		handButton[1].setImageResource(R.drawable.chbutton);
		handButton[2].setImageResource(R.drawable.pabutton);
		handButton[0].setEnabled(true);
		handButton[1].setEnabled(true);
		handButton[2].setEnabled(true);
	}

	public void getAnimNumber() {
		Drawable currentAnim = animation.getCurrent();
		for (int i=0; i<3; i++) {
			if (currentAnim == animation.getFrame(i)) {
				animNumber = i;
				break;
			}
		}
	}

	public void getWinorNot(int anim, int my) {
		if (anim == my) {
			myResult = "aiko";
		} else {
			anim--;
			if (anim == -1) {
				anim = 2;
			}
			if (anim == my) {
				conWin ++;
				myResult = "kachi";
				win.start();
				if(conWin%20 == 0) {
					stars.setImageResource(getResources().getIdentifier("star20", "drawable", getPackageName()));
				} else {
					stars.setImageResource(getResources().getIdentifier("star"+String.valueOf(conWin%20), "drawable", getPackageName()));
				}
			} else {
				conWin = 0;
				myResult = "make";
				if(Math.random() > 0.05) {
					lose1.start();
				} else {
					lose2.start();
				}
				stars.setImageResource(getResources().getIdentifier("star"+conWin, "drawable", getPackageName()));
			}
		}
	}

	public void updatedb(int myNumber, String myResult, int conWin, int koushinFlag) {
		localSQLManager dbManager = new localSQLManager(this);
		SQLiteDatabase db = dbManager.getReadableDatabase();

		db.execSQL("update mydata set cnt = cnt+1;");
		if (myResult == "kachi") {
			db.execSQL("update mydata set tw = tw+1;");
			if (myNumber == 0) {
				db.execSQL("update mydata set gn = gn+1;");
				db.execSQL("update mydata set gw = gw+1;");
			} else if (myNumber == 1) {
				db.execSQL("update mydata set cn = cn+1;");
				db.execSQL("update mydata set cw = cw+1;");
			} else if (myNumber == 2) {
				db.execSQL("update mydata set pn = pn+1;");
				db.execSQL("update mydata set pw = pw+1;");
			}
		} else {
			if (myNumber == 0) {
				db.execSQL("update mydata set gn = gn+1;");
			} else if (myNumber == 1) {
				db.execSQL("update mydata set cn = cn+1;");
			} else if (myNumber == 2) {
				db.execSQL("update mydata set pn = pn+1;");
			}
		}

		if (koushinFlag == 1) {
			db.execSQL("update mydata set score = ?;",new Object[]{String.valueOf(conWin)});
			myData[8] = conWin;
		}
		db.close();
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
