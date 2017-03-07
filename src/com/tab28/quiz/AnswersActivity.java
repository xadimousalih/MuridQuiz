/**
 * 
 */
package com.tab28.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tab28.quiz.muridquiz.GamePlay;
import com.tab28.quiz.util.Utility;

/**
 * @author xadimouSALIH
 * @Link http://www.tab28.com
 */
public class AnswersActivity extends Activity implements OnClickListener {
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				AnswersActivity.this);
		alertDialog2.setTitle(R.string.title);
		alertDialog2.setMessage(R.string.app_exit_message);
		alertDialog2.setIcon(R.drawable.ic_launcher);
		alertDialog2.setPositiveButton(R.string.str_yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						onDestroy();
						finish();
						System.exit(0);
					}
				});
		alertDialog2.setNegativeButton(R.string.str_no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertDialog2.show();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.answers);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		GamePlay currentGame = ((MuridQuizApplication) getApplication())
				.getCurrentGame();

		TextView results = (TextView) findViewById(R.id.answers);
		String answers = Utility.getAnswers(currentGame.getQuestions());
		results.setText(answers);

		// handle button actions
		Button finishBtn = (Button) findViewById(R.id.finishBtn);
		finishBtn.setOnClickListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 * 
	 * This method is to override the back button on the phone to prevent users
	 * from navigating back in to the quiz
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finishBtn:
			finish();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.app_about);
		menu.add(0, 1, 1, R.string.str_exit);
		menu.add(0, 2, 2, R.string.str_stop);
		menu.add(0, 3, 3, R.string.str_start);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case 0:
			openOptionsDialog();
			break;
		case 1:
			exitOptionsDialog();
			break;
		case 2:
			stopService(new Intent(this, MyService.class));
			break;
		case 3:
			startService(new Intent(this, MyService.class));
			break;
		}

		return true;
	}

	private void exitOptionsDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.title)
				.setMessage(R.string.app_exit_message)
				.setIcon(R.drawable.ic_launcher)
				.setNegativeButton(R.string.str_no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						})
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								onDestroy();
								finish();
								System.exit(0);
							}
						}).show();
	}

	private void openOptionsDialog() {
		AboutDialog about = new AboutDialog(this);
		about.setTitle(Html.fromHtml(this.getString(R.string.app_about)));
		about.show();
	}

}
