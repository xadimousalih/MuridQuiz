package com.tab28.quiz;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tab28.quiz.db.DBHelper;
import com.tab28.quiz.muridquiz.Constants;
import com.tab28.quiz.muridquiz.GamePlay;
import com.tab28.quiz.muridquiz.Question;

/**
 * @author xadimouSALIH
 * @Link http://www.tab28.com
 */

public class SplashActivity extends Activity implements OnClickListener {
	Handler handler;
	public MediaPlayer splashSound;
	Intent playbackServiceIntent;

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				SplashActivity.this);
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
		setContentView(R.layout.welcome);

		Button playBtn = (Button) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);
		Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
		settingsBtn.setOnClickListener(this);
		Button rulesBtn = (Button) findViewById(R.id.rulesBtn);
		rulesBtn.setOnClickListener(this);
		Button exitBtn = (Button) findViewById(R.id.exitBtn);
		exitBtn.setOnClickListener(this);
		//startService(new Intent(this, MyService.class));
	}

	/**
	 * Listener for game menu
	 */
	@Override
	public void onClick(View v) {
		Intent i;

		switch (v.getId()) {
		case R.id.playBtn:
			List<Question> questions = getQuestionSetFromDb();
			GamePlay c = new GamePlay();
			c.setQuestions(questions);
			c.setNumRounds(getNumQuestions());
			((MuridQuizApplication) getApplication()).setCurrentGame(c);
			i = new Intent(this, QuestionActivity.class);
			startActivityForResult(i, Constants.PLAYBUTTON);
			break;

		case R.id.rulesBtn:
			i = new Intent(this, RulesActivity.class);
			startActivityForResult(i, Constants.RULESBUTTON);
			break;

		case R.id.settingsBtn:
			i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, Constants.SETTINGSBUTTON);
			break;

		case R.id.exitBtn:
			stopService(new Intent(this, MyService.class));
			finish();
			break;
		}

	}

	/**
	 * Method that retrieves a random set of questions from the database for the
	 * given difficulty
	 * 
	 * @return
	 * @throws Error
	 */
	private List<Question> getQuestionSetFromDb() throws Error {
		int diff = getDifficultySettings();
		int numQuestions = getNumQuestions();
		DBHelper myDbHelper = new DBHelper(this);
		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		List<Question> questions = myDbHelper
				.getQuestionSet(diff, numQuestions);
		myDbHelper.close();
		return questions;
	}

	/**
	 * Method to return the difficulty settings
	 * 
	 * @return
	 */
	private int getDifficultySettings() {
		SharedPreferences settings = getSharedPreferences(Constants.SETTINGS, 0);
		int diff = settings.getInt(Constants.DIFFICULTY, Constants.MEDIUM);
		return diff;
	}

	/**
	 * Method to return the number of questions for the game
	 * 
	 * @return
	 */
	private int getNumQuestions() {
		SharedPreferences settings = getSharedPreferences(Constants.SETTINGS, 0);
		int numRounds = settings.getInt(Constants.NUM_ROUNDS, 10);
		return numRounds;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.app_about);
		menu.add(0, 1, 1, R.string.str_exit);
		menu.add(0, 2, 2, R.string.str_stop);
		menu.add(0, 3, 3, R.string.str_start);
		return super.onCreateOptionsMenu(menu);
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