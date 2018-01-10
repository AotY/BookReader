/*
 * Copyright (C) 2007-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.koolearn.android.kooreader.popup;

import android.view.View;
import android.widget.RelativeLayout;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.android.kooreader.SimplePopupWindow;
import com.koolearn.klibrary.core.resources.ZLResource;
import com.koolearn.kooreader.kooreader.ActionCode;
import com.koolearn.kooreader.kooreader.KooReaderApp;
import com.ninestars.android.R;

/**
 * 页面字体选中之后
 */
public class SelectionPopup extends PopupPanel implements View.OnClickListener {
	public final static String ID = "SelectionPopup";

	public SelectionPopup(KooReaderApp kooReader) {
		super(kooReader);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void createControlPanel(KooReader activity, RelativeLayout root) {
		if (myWindow != null && activity == myWindow.getContext()) {
			return;
		}

		activity.getLayoutInflater().inflate(R.layout.selection_panel, root);
		myWindow = (SimplePopupWindow)root.findViewById(R.id.selection_panel);

		final ZLResource resource = ZLResource.resource("selectionPopup");
		setupButton(R.id.selection_panel_copy, resource.getResource("copyToClipboard").getValue());
		setupButton(R.id.selection_panel_share, resource.getResource("share").getValue());
		setupButton(R.id.selection_panel_translate, resource.getResource("translate").getValue());
		setupButton(R.id.selection_panel_bookmark, resource.getResource("bookmark").getValue());
		setupButton(R.id.selection_panel_close, resource.getResource("close").getValue());
	}

	private void setupButton(int buttonId, String description) {
		final View button = myWindow.findViewById(buttonId);
		button.setOnClickListener(this);
		button.setContentDescription(description);
	}

	public void move(int selectionStartY, int selectionEndY) {
		if (myWindow == null) {
			return;
		}

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,
			RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		final int verticalPosition;
		final int screenHeight = ((View)myWindow.getParent()).getHeight();
		final int diffTop = screenHeight - selectionEndY;
		final int diffBottom = selectionStartY;
		if (diffTop > diffBottom) {
			verticalPosition = diffTop > myWindow.getHeight() + 20
				? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.CENTER_VERTICAL;
		} else {
			verticalPosition = diffBottom > myWindow.getHeight() + 20
				? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.CENTER_VERTICAL;
		}

		layoutParams.addRule(verticalPosition);
		myWindow.setLayoutParams(layoutParams);
	}

	@Override
	protected void update() {
	}

	public void onClick(View view) {
		int i = view.getId();
		if (i == R.id.selection_panel_copy) {
			Application.runAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD);

		} else if (i == R.id.selection_panel_share) {
			Application.runAction(ActionCode.SELECTION_SHARE);

		} else if (i == R.id.selection_panel_translate) {//				Application.runAction(ActionCode.SELECTION_TRANSLATE);

		} else if (i == R.id.selection_panel_bookmark) {
			Application.runAction(ActionCode.SELECTION_BOOKMARK);

		} else if (i == R.id.selection_panel_close) {
			Application.runAction(ActionCode.SELECTION_CLEAR);

		}
		Application.hideActivePopup();
	}
}
