package org.waremon.janken;

import java.io.IOException;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Coins extends ExtActivity implements OnClickListener {
	
    public static RequestToken _req = null;
    public static OAuthAuthorization _oauth = null;
    
	ImageButton handButton[] = new ImageButton[3];
	ImageButton coinButton[] = new ImageButton[9];
	int isDialog = 0;
	String mMedals[] = {
			"�O�[���_��",
			"�`���L���_��",
			"�p�[���_��",
			"10�A�����_��",
			"20�A�����_��",
			"30�A�����_��",
			"�ʎZ100�����_��",
			"�ʎZ200�����_��",
			"�ʎZ300�����_��",
			};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coins);
		
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
		
		for(int i=0; i<9; i++) {
			int j=i+1;
			coinButton[i] = (ImageButton) findViewById(getResources().getIdentifier("coin"+j, "id", getPackageName()));
		}
		if (myData[3]>=100) {
			coinButton[0].setImageResource(getResources().getIdentifier("coin1", "drawable", getPackageName()));
			coinButton[0].setOnClickListener(this);
		}
		if (myData[4]>=100) {
			coinButton[1].setImageResource(getResources().getIdentifier("coin2", "drawable", getPackageName()));
			coinButton[1].setOnClickListener(this);
		}
		if (myData[5]>=100) {
			coinButton[2].setImageResource(getResources().getIdentifier("coin3", "drawable", getPackageName()));
			coinButton[2].setOnClickListener(this);
		}
		if (myData[8]>=10) {
			coinButton[3].setImageResource(getResources().getIdentifier("coin4", "drawable", getPackageName()));
			coinButton[3].setOnClickListener(this);
		}
		if (myData[8]>=20) {
			coinButton[4].setImageResource(getResources().getIdentifier("coin5", "drawable", getPackageName()));
			coinButton[4].setOnClickListener(this);
		}
		if (myData[8]>=30) {
			coinButton[5].setImageResource(getResources().getIdentifier("coin6", "drawable", getPackageName()));
			coinButton[5].setOnClickListener(this);
		}
		if (myData[6]>=100) {
			coinButton[6].setImageResource(getResources().getIdentifier("coin7", "drawable", getPackageName()));
			coinButton[6].setOnClickListener(this);
		} 
		if (myData[6]>=200) {
			coinButton[7].setImageResource(getResources().getIdentifier("coin8", "drawable", getPackageName()));
			coinButton[7].setOnClickListener(this);
		} 
		if (myData[6]>=300) {
			coinButton[8].setImageResource(getResources().getIdentifier("coin9", "drawable", getPackageName()));
			coinButton[8].setOnClickListener(this);
		}
		
        handButton[0] = (ImageButton) findViewById(R.id.top1_2_left);
        handButton[0].setOnClickListener(this);
        handButton[1] = (ImageButton) findViewById(R.id.top1_2_center);
        handButton[1].setOnClickListener(this);
        handButton[2] = (ImageButton) findViewById(R.id.top1_2_right);
        handButton[2].setOnClickListener(this);
        
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
			showMyData();
		} else if (v == handButton[1]) {
		} else if (v == handButton[2]) {
			startGame();
		}
		for (int i=0; i<9; i++) {
			if(v == coinButton[i]) {
				isDialog = 1;
				LayoutInflater inflater = LayoutInflater.from(this);
				final int j=i+1;
				final View view = inflater.inflate(getResources().getIdentifier("coin"+j, "layout", getPackageName()), null);
				new AlertDialog.Builder(this)
				.setIcon(getResources().getIdentifier("coin"+j, "drawable", getPackageName()))
				.setTitle(mMedals[i])
				.setView(view)
				.setPositiveButton("�c�C�[�g", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						toTweet(j-1);
					}
				})
				.setNegativeButton("���ǂ�", null)
				.show();
			}
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if(isDialog == 1) {
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
	
	public void startGame() {
		Intent intent = new Intent(this, Janken.class);
		startActivity(intent);
	}
	
	public void showMyData() {
		Intent intent = new Intent(this, Mydata.class);
		startActivity(intent);
	}
	
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if(hasFocus) {
    		isDialog = 0;
    		home.start();
    	} else {
    		if(isDialog == 0) {
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

	public void toTweet(int j) {
		Global.twNum = j;
		if (check_oauth_key() == 0) {
			//Twitetr4J�̐ݒ��ǂݍ���
			Configuration conf = ConfigurationContext.getInstance();

			//Oauth�F�؃I�u�W�F�N�g�쐬
			_oauth = new OAuthAuthorization(conf);
			//Oauth�F�؃I�u�W�F�N�g��consumerKey��consumerSecret��ݒ�
			_oauth.setOAuthConsumer("bspn1nOHK7aSHIub5m3A", "vZQpWKsroaCQg50Q8wYUcvfOOffmW7MhtIpKZvGmSI");
			//�A�v���̔F�؃I�u�W�F�N�g�쐬
			try {
				_req = _oauth.getOAuthRequestToken("Callback://CallBackActivity");
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			String _uri;
			_uri = _req.getAuthorizationURL();
			startActivityForResult(new Intent(Intent.ACTION_VIEW , Uri.parse(_uri)), 0);
		} else {
			//twitter�I�u�W�F�N�g�̍쐬
			Twitter tw = new TwitterFactory().getInstance();
			//AccessToken�I�u�W�F�N�g�̍쐬
			AccessToken at = new AccessToken(Global.key, Global.secret);
			//Consumer key��Consumer key seacret�̐ݒ�
			tw.setOAuthConsumer(Global.consumer_key, Global.consumer_key_secret);
			//AccessToken�I�u�W�F�N�g��ݒ�
			tw.setOAuthAccessToken(at);

			try {
				tw.updateStatus(Global.twMsg[Global.twNum]);
			} catch (TwitterException e) {
				e.printStackTrace();
				if(e.isCausedByNetworkIssue()){
					Toast.makeText(this, "�l�b�g�[���[�N�ɖ�肪����܂�", Toast.LENGTH_LONG);
				}
			}
		}
	}
	
	public int check_oauth_key() {
		localSQLManager dbManager = new localSQLManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor c = db.query("twtoken", new String[] {"key", "secret"}, null, null, null, null, null);
        c.moveToFirst();
        Global.key = c.getString(0);
        Global.secret = c.getString(1);
        c.close();
        db.close();
        if (Global.key.equals("init")) {
        	return 0;
        } else {
        	return 1;
        }
	}
}
