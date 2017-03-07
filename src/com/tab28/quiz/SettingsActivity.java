package com.tab28.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

import com.tab28.quiz.muridquiz.Constants;

/**
 * @author xadimouSALIH
 * @Link http://www.tab28.com
 */

public class SettingsActivity extends Activity implements OnClickListener {
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/**
		 * set listener on update button
		 */
		Button updateBtn = (Button) findViewById(R.id.nextBtn);
		updateBtn.setOnClickListener(this);

		/**
		 * Set selected button if saved
		 */
		updateButtonWithPreferences();

	}

	/**
	 * Method to update default check box
	 */
	private void updateButtonWithPreferences() {
		RadioButton c1 = (RadioButton) findViewById(R.id.easySetting);
		RadioButton c2 = (RadioButton) findViewById(R.id.mediumSetting);
		RadioButton c3 = (RadioButton) findViewById(R.id.hardSetting);

		SharedPreferences settings = getSharedPreferences(Constants.SETTINGS, 0);
		int diff = settings.getInt(Constants.DIFFICULTY, Constants.MEDIUM);

		switch (diff) {
		case Constants.EASY:
			c1.toggle();
			break;

		case Constants.MEDIUM:
			c2.toggle();
			break;

		case Constants.EXTREME:
			c3.toggle();
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		/**
		 * check which settings set and return to menu
		 */
		if (!checkSelected()) {
			return;
		} else {
			SharedPreferences settings = getSharedPreferences(
					Constants.SETTINGS, 0);
			Editor e = settings.edit();
			e.putInt(Constants.DIFFICULTY, getSelectedSetting());
			e.commit();
			finish();
		}

	}

	/**
	 * Method to check that a checkbox is selected
	 * 
	 * @return boolean
	 */
	private boolean checkSelected() {
		RadioButton c1 = (RadioButton) findViewById(R.id.easySetting);
		RadioButton c2 = (RadioButton) findViewById(R.id.mediumSetting);
		RadioButton c3 = (RadioButton) findViewById(R.id.hardSetting);
		return (c1.isChecked() || c2.isChecked() || c3.isChecked());
	}

	/**
	 * Get the selected setting
	 */
	private int getSelectedSetting() {
		RadioButton c1 = (RadioButton) findViewById(R.id.easySetting);
		RadioButton c2 = (RadioButton) findViewById(R.id.mediumSetting);
		if (c1.isChecked()) {
			return Constants.EASY;
		}
		if (c2.isChecked()) {
			return Constants.MEDIUM;
		}

		return Constants.EXTREME;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.app_about);
		menu.add(0, 1, 1, R.string.str_exit);
		menu.add(0, 2, 2, R.string.str_stop);
		menu.add(0, 3, 3, R.string.str_start);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
	        event.startTracking();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
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
