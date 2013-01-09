package org.waremon.janken;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class CallBackActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callback);

		AccessToken token = null;

		//Twitter�̔F�؉�ʂ��甭�s�����Intent����Uri���擾
		Uri uri = getIntent().getData();

		if(uri != null && uri.toString().startsWith("Callback://CallBackActivity")){
			//oauth_verifier���擾����
			String verifier = uri.getQueryParameter("oauth_verifier");
			try {
				//AccessToken�I�u�W�F�N�g���擾
				token = Coins._oauth.getOAuthAccessToken(Coins._req, verifier);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		//token��db�ɓo�^
		localSQLManager dbManager = new localSQLManager(this);
		SQLiteDatabase db = dbManager.getReadableDatabase();
		db.execSQL("update twtoken set key = ?;",new Object[]{String.valueOf(token.getToken())});
		db.execSQL("update twtoken set secret = ?;",new Object[]{String.valueOf(token.getTokenSecret())});
		db.close();

		//twitter�I�u�W�F�N�g�̍쐬
		Twitter tw = new TwitterFactory().getInstance();

		//AccessToken�I�u�W�F�N�g�̍쐬
		AccessToken at = new AccessToken(token.getToken(), token.getTokenSecret());

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
		// �A�v���ɖ߂�
		Intent intent = new Intent(this, Coins.class);
		startActivity(intent);
	} 
}
