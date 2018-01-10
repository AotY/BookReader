package com.koolearn.android.kooreader.popup;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.android.kooreader.SimplePopupWindow;
import com.koolearn.klibrary.text.view.ZLTextWordCursor;
import com.koolearn.klibrary.core.application.ZLApplication;

import com.koolearn.kooreader.kooreader.KooReaderApp;

public abstract class PopupPanel extends ZLApplication.PopupPanel {
	public ZLTextWordCursor StartPosition;

	protected volatile SimplePopupWindow myWindow;//用来显示下面的pop
//	protected volatile SimplePopupWindow myWindowTop;//用来显示上面的pop
	private volatile KooReader myActivity;
	private volatile RelativeLayout myRoot;

	protected PopupPanel(KooReaderApp fbReader) {
		super(fbReader);
	}

	protected final KooReaderApp getReader() {
		return (KooReaderApp)Application;
	}

	@Override
	protected void show_() {
		if (myActivity != null) {
			createControlPanel(myActivity, myRoot);
		}
		if (myWindow != null) {
			myWindow.show();
		}

//		if (myWindowTop != null){
//			myWindowTop.show();
//		}
	}

	@Override
	protected void hide_() {
		if (myWindow != null) {
			myWindow.hide();
		}
//		if (myWindowTop != null){
//			myWindowTop.hide();
//		}
	}

	private final void removeWindow(Activity activity) {
		if (myWindow != null && activity == myWindow.getContext()) {
			final ViewGroup root = (ViewGroup)myWindow.getParent();
			myWindow.hide();
			root.removeView(myWindow);
			myWindow = null;
		}
//		if (myWindowTop != null && activity == myWindowTop.getContext()) {
//			final ViewGroup root = (ViewGroup)myWindowTop.getParent();
//			myWindowTop.hide();
//			root.removeView(myWindowTop);
//			myWindowTop = null;
//		}
	}

	public static void removeAllWindows(ZLApplication application, Activity activity) {
		for (ZLApplication.PopupPanel popup : application.popupPanels()) {
			if (popup instanceof PopupPanel) {
				((PopupPanel)popup).removeWindow(activity);
			} else if (popup instanceof NavigationPopup) {
				((NavigationPopup)popup).removeWindow(activity);
			}
		}
	}

	public static void restoreVisibilities(ZLApplication application) {
		final ZLApplication.PopupPanel popup = application.getActivePopup();
		if (popup instanceof PopupPanel) {
			((PopupPanel)popup).show_();
		} else if (popup instanceof NavigationPopup) {
			((NavigationPopup)popup).show_();
		}
	}

	public final void initPosition() {
		if (StartPosition == null) {
			StartPosition = new ZLTextWordCursor(getReader().getTextView().getStartCursor());
		}
	}

	public final void storePosition() {
		if (StartPosition == null) {
			return;
		}

		final KooReaderApp reader = getReader();
		if (!StartPosition.equals(reader.getTextView().getStartCursor())) {
			reader.addInvisibleBookmark(StartPosition);
			reader.storePosition();
		}
	}

	public void setPanelInfo(KooReader activity, RelativeLayout root) {
		myActivity = activity;
		myRoot = root;
	}

	public abstract void createControlPanel(KooReader activity, RelativeLayout root);
}
