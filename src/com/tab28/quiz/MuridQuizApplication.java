package com.tab28.quiz;

import com.tab28.quiz.muridquiz.GamePlay;

import android.app.Application;

/**
 * @author xadimouSALIH
 * @Link http://www.tab28.com
 */
public class MuridQuizApplication extends Application {
	private GamePlay currentGame;

	/**
	 * @param currentGame
	 *            the currentGame to set
	 */
	public void setCurrentGame(GamePlay currentGame) {
		this.currentGame = currentGame;
	}

	/**
	 * @return the currentGame
	 */
	public GamePlay getCurrentGame() {
		return currentGame;
	}
}
