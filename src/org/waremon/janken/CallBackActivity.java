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

		//Twitterの認証画面から発行されるIntentからUriを取得
		Uri uri = getIntent().getData();

		if(uri != null && uri.toString().startsWith("Callback://CallBackActivity")){
			//oauth_verifierを取得する
			String verifier = uri.getQueryParameter("oauth_verifier");
			try {
				//AccessTokenオブジェクトを取得
				token = Coins._oauth.getOAuthAccessToken(Coins._req, verifier);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		//tokenをdbに登録
		localSQLManager dbManager = new localSQLManager(this);
		SQLiteDatabase db = dbManager.getReadableDatabase();
		db.execSQL("update twtoken set key = ?;",new Object[]{String.valueOf(token.getToken())});
		db.execSQL("update twtoken set secret = ?;",new Object[]{String.valueOf(token.getTokenSecret())});
		db.close();

		//twitterオブジェクトの作成
		Twitter tw = new TwitterFactory().getInstance();

		//AccessTokenオブジェクトの作成
		AccessToken at = new AccessToken(token.getToken(), token.getTokenSecret());

		//Consumer keyとConsumer key seacretの設定
		tw.setOAuthConsumer(Global.consumer_key, Global.consumer_key_secret);

		//AccessTokenオブジェクトを設定
		tw.setOAuthAccessToken(at);

		try {
			tw.updateStatus(Global.twMsg[Global.twNum]);
		} catch (TwitterException e) {
			e.printStackTrace();
			if(e.isCausedByNetworkIssue()){
				Toast.makeText(this, "ネットーワークに問題があります", Toast.LENGTH_LONG);
			}
		}
		// アプリに戻る
		Intent intent = new Intent(this, Coins.class);
		startActivity(intent);
	} 
}
