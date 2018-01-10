package com.koolearn.android.kooreader;

import com.koolearn.kooreader.kooreader.KooAction;
import com.koolearn.kooreader.kooreader.KooReaderApp;

public abstract class KooAndroidAction extends KooAction {
	protected final KooReader BaseActivity;

	public KooAndroidAction(KooReader baseActivity, KooReaderApp kooreader) {
		super(kooreader);
		BaseActivity = baseActivity;
	}
}
