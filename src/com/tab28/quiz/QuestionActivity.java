/**
 * 
 */
package com.tab28.quiz;

import java.util.List;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tab28.quiz.muridquiz.GamePlay;
import com.tab28.quiz.muridquiz.Question;
import com.tab28.quiz.util.Utility;

/**
 * @author xadimouSALIH
 * @Link http://www.tab28.com
 */

public class QuestionActivity extends Activity implements OnClickListener {

	private Question currentQ;
	private GamePlay currentGame;

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				QuestionActivity.this);
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
		setContentView(R.layout.question);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		try {
			currentGame = ((MuridQuizApplication) getApplication())
					.getCurrentGame();
			currentQ = currentGame.getNextQuestion();
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}
		/**
		 * Configure current game and get question
		 */

		Button nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		Button chnBtn = (Button) findViewById(R.id.nextChN);
		chnBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						SettingsActivity.class);
				// startActivityForResult(i, Constants.SETTINGSBUTTON);
				startActivity(i);
				finish();
			}
		});

		/**
		 * Update the question and answer options..
		 */
		setQuestions();

	}

	/**
	 * Method to set the text for the question and answers from the current
	 * games current question
	 */
	private void setQuestions() {
		// set the question text from current question
		String question = Utility.capitalise(currentQ.getQuestion());
		TextView qText = (TextView) findViewById(R.id.question);
		qText.setText(question);

		// set the available options
		List<String> answers = currentQ.getQuestionOptions();
		TextView option1 = (TextView) findViewById(R.id.answer1);
		option1.setText(Utility.capitalise(" " + answers.get(0)));

		TextView option2 = (TextView) findViewById(R.id.answer2);
		option2.setText(Utility.capitalise(" " + answers.get(1)));

		TextView option3 = (TextView) findViewById(R.id.answer3);
		option3.setText(Utility.capitalise(" " + answers.get(2)));

		TextView option4 = (TextView) findViewById(R.id.answer4);
		option4.setText(Utility.capitalise(" " + answers.get(3)));
	}

	@Override
	public void onClick(View arg0) {
		// Log.d("Questions", "Moving to next question");
		Intent i;
		/**
		 * validate a checkbox has been selected
		 */
		if (!checkAnswer()) {
			Toast.makeText(getApplicationContext(),
					this.getApplicationContext().getString(R.string.choix), Toast.LENGTH_LONG).show();
			return;
		}

		/**
		 * check if end of game
		 */
		if (currentGame.isGameOver()) {
			// Log.d("Questions", "End of game! lets add up the scores..");
			// Log.d("Questions", "Questions Correct: " +
			// currentGame.getRight());
			// Log.d("Questions", "Questions Wrong: " + currentGame.getWrong());
			i = new Intent(this, EndgameActivity.class);
			startActivity(i);
			finish();
		} else {
			i = new Intent(this, QuestionActivity.class);
			startActivity(i);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Check if a checkbox has been selected, and if it has then check if its
	 * correct and update gamescore
	 */
	private boolean checkAnswer() {
		String answer = getSelectedAnswer();
		if (answer == null) {
			// Log.d("Questions", "No Checkbox selection made - returning");
			return false;
		} else {
			// Log.d("Questions",
			// "Valid Checkbox selection made - check if correct");
			if (currentQ.getAnswer().trim().equalsIgnoreCase(answer.trim())) {
				// Log.d("Questions", "Correct Answer!");
				Toast.makeText(getApplicationContext(),
						this.getApplicationContext().getString(R.string.juste) + answer, Toast.LENGTH_LONG).show();

				currentGame.incrementRightAnswers();
			} else {
				// Log.d("Questions", "Incorrect Answer!");
				Toast.makeText(getApplicationContext(),
						this.getApplicationContext().getString(R.string.ooup) + currentQ.getAnswer(), Toast.LENGTH_LONG)
						.show();
				currentGame.incrementWrongAnswers();
			}
			return true;
		}
	}

	private String getSelectedAnswer() {
		RadioButton c1 = (RadioButton) findViewById(R.id.answer1);
		RadioButton c2 = (RadioButton) findViewById(R.id.answer2);
		RadioButton c3 = (RadioButton) findViewById(R.id.answer3);
		RadioButton c4 = (RadioButton) findViewById(R.id.answer4);
		if (c1.isChecked()) {
			return c1.getText().toString();
		}
		if (c2.isChecked()) {
			return c2.getText().toString();
		}
		if (c3.isChecked()) {
			return c3.getText().toString();
		}
		if (c4.isChecked()) {
			return c4.getText().toString();
		}

		return null;
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
			// *** Your Code ***
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
