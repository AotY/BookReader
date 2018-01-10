package com.koolearn.android.kooreader.popup;

import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.klibrary.core.application.ZLApplication;
import com.koolearn.klibrary.text.view.ZLTextView;
import com.koolearn.klibrary.text.view.ZLTextWordCursor;
import com.koolearn.kooreader.bookmodel.TOCTree;
import com.koolearn.kooreader.kooreader.KooReaderApp;
import com.ninestars.android.R;

public class ProgressPopup extends ZLApplication.PopupPanel {
    public final static String ID = "ProgressPopup";
    private volatile ProgressWindow myWindow;
    private volatile KooReader myActivity;
    private volatile RelativeLayout myRoot;
    private ZLTextWordCursor myStartPosition;
    private final KooReaderApp myKooReader;
    private volatile boolean myIsInProgress;
    private ZLTextView.PagePosition pagePosition;
    private SeekBar mSlider;
//    private TextView mText;
    private TextView mPre_character;
    private TextView mNext_character;
    public ProgressPopup(KooReaderApp kooReader) {
        super(kooReader);
        myKooReader = kooReader;
    }

    public void runNavigation() {
        if (myWindow == null || myWindow.getVisibility() == View.GONE) {
            myIsInProgress = false;
            Application.showPopup(ID);
        }
    }
    public void setPanelInfo(KooReader activity, RelativeLayout root) {
        myActivity = activity;
        myRoot = root;
    }
    private void setStatusBarVisibility(boolean visible) {
        if (visible) {
            myActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); // 设置状态栏
        } else {
            myActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }
    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void update() {
        if (!myIsInProgress && myWindow != null) {
            setupNavigation();
        }
    }
    //进度跳转
    private void gotoPage(int page) {
        final ZLTextView view = myKooReader.getTextView();
        if (page == 1) {
            view.gotoHome();
        } else {
            view.gotoPage(page);
        }
//        myKooReader.clearTextCaches();
        myKooReader.getViewWidget().reset();
        myKooReader.getViewWidget().repaint();
    }
    @Override
    protected void hide_() {
        setStatusBarVisibility(false);
        if (myWindow != null) {
            myWindow.hide();
        }
    }

    @Override
    protected void show_() {
        setStatusBarVisibility(true);
        if (myActivity != null){
            createPanel(myActivity, myRoot);
        }
        if (myWindow != null){
            myWindow.show();
            setupNavigation();//显示章节信息
        }
    }


    private void createPanel(KooReader myActivity, RelativeLayout myRoot) {
        if (myWindow != null && myActivity == myWindow.getContext()) {
            return;
        }
        myActivity.getLayoutInflater().inflate(R.layout.progress,myRoot);
        myWindow = (ProgressWindow) myRoot.findViewById(R.id.progressWindow);
//        mText = (TextView) myWindow.findViewById(R.id.navigation_text);//章节信息显示
        mSlider = (SeekBar) myWindow.findViewById(R.id.navigation_slider);//进度条
        mPre_character = (TextView) myWindow.findViewById(R.id.pre_character);//上一章
        mNext_character = (TextView) myWindow.findViewById(R.id.next_character);//下一章
        initClick();
    }
    private void initClick(){
        mSlider.setOnSeekBarChangeListener(changeListener);
        mPre_character.setOnClickListener(l);
        mNext_character.setOnClickListener(l);
    }

    //SeekBar监听事件
    private SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
        private void gotoPage(int page){
            final ZLTextView view = myKooReader.getTextView();
            if (page == 1){
                view.gotoHome();
            }else {
                view.gotoPage(page);
            }
        }
        private void gotoPagePer(int page){
            final ZLTextView view = myKooReader.getTextView();
            view.gotoPageByPec(page);
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
//                gotoPagePer(progress);
                final TOCTree tocElement = myKooReader.getProgressTOCElement(progress);
                if (tocElement != null) {
                    gotoPageByPrah(tocElement.getReference().ParagraphIndex + 1);
                    // gotoPageByPrah(tocElement.getReference().ParagraphIndex + 1);
                }
//                mText.setText(makeProgressTextPer(myKooReader.getTextView().pagePositionPec()));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            myIsInProgress = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            myKooReader.getViewWidget().reset();
            myKooReader.getViewWidget().repaint();
            myIsInProgress = false;
            if (myStartPosition != null && !myStartPosition.equals(myKooReader.getTextView().getStartCursor())){
                myKooReader.addInvisibleBookmark(myStartPosition);
                myKooReader.storePosition();
                System.out.println("myKooReader.getTextView().getStartCursor() == " + myKooReader.getTextView().getStartCursor());
            }
            myStartPosition = null;
        }
    };

    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.pre_character) {//                    gotoPage(pagePosition.Current - 30);
                final TOCTree tocElement = myKooReader.getPrepTOCElement();
                //   Log.d("NavigationPopup", "tocElement.getReference().ParagraphIndex:" + tocElement.getReference().ParagraphIndex);
                if (tocElement != null) {
                    gotoPageByPrah(tocElement.getReference().ParagraphIndex + 1);
//                    int index = textView.getModel().getParagraphsNumber();
//                    Log.d("NavigationPopup", "index---------》:" + index);
                    // gotoPageByPrah(tocElement.getReference().ParagraphIndex + 1);
                }

            } else if (i == R.id.next_character) {
                final TOCTree tocElement1 = myKooReader.getNextTOCElement();
                //   Log.d("NavigationPopup", "tocElement.getReference().ParagraphIndex:" + tocElement.getReference().ParagraphIndex);
                if (tocElement1 != null) {
                    gotoPageByPrah(tocElement1.getReference().ParagraphIndex + 1);
                    // gotoPageByPrah(tocElement.getReference().ParagraphIndex + 1);
                }
//                    gotoPage(pagePosition.Current + 30);

            }
        }
    };

    private void setupNavigation() {
        final ZLTextView textView = myKooReader.getTextView();
        pagePosition = textView.pagePosition();
        String progress = textView.pagePositionPec();//获取当前进度
        mSlider.setMax(textView.pagePosition2());//设置最大长度
        mSlider.setProgress(textView.pagePosition1());//设置当前长度
//        mText.setText(makeProgressTextPer(progress));
    }
    private String makeProgressTextPer(String progress) {
        final StringBuilder builder = new StringBuilder();
        builder.append(progress);
        final TOCTree tocElement = myKooReader.getCurrentTOCElement();
        if (tocElement != null) {
            builder.append("  ");
            builder.append(tocElement.getText());//章节信息
        }
        return builder.toString();
    }
    private void gotoPageByPrah(int prah) {
        final ZLTextView view = myKooReader.getTextView();
//        pagePosition = view.pagePosition();
        view.gotoPageByPec(prah);
//        myKooReader.clearTextCaches();
        myKooReader.getViewWidget().reset();
        myKooReader.getViewWidget().repaint();
//        myKooReader.showBookTextView();
    }
}
