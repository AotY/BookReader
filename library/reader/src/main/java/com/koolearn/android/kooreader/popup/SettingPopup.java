package com.koolearn.android.kooreader.popup;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.klibrary.core.application.ZLApplication;
import com.koolearn.klibrary.core.options.ZLIntegerRangeOption;
import com.koolearn.klibrary.core.view.ZLView;
import com.koolearn.kooreader.kooreader.KooReaderApp;
import com.koolearn.kooreader.kooreader.options.ColorProfile;
import com.ninestars.android.R;

/**
 * 设置二级菜单，字体，翻页方式等
 */
public final class SettingPopup extends ZLApplication.PopupPanel implements View.OnClickListener {
    public final static String ID = "SettingPopup";

    private volatile SettingWindow myWindow;
    private volatile KooReader myActivity;
    private volatile RelativeLayout myRoot;
    private boolean myIsBrightnessAdjustmentInProgress;
    private final KooReaderApp myKooReader;
    private TextView tvFontMinus, tvFontAdd;
    private TextView tvLightMinus, tvLightAdd;
    private ZLIntegerRangeOption integerRangeOption;
    private SeekBar slider;
    //    private TextView tvPageMode, tvSetting;
    private ImageButton tvPageSimulation, tvPageCover, tvPageSlide, tvPageNone;
    private TextView bgWhite, bgBook, bgPink, bgGreen, bgColor;
    private TextView mTextViewDay, mTextViewNight;


    public SettingPopup(KooReaderApp kooReader) {
        super(kooReader);
        myKooReader = kooReader;
    }

    public void setPanelInfo(KooReader activity, RelativeLayout root) {
        myActivity = activity;
        myRoot = root;
    }

    public void runNavigation() {
        if (myWindow == null || myWindow.getVisibility() == View.GONE) {
            Application.showPopup(ID);
        }
    }

    @Override
    protected void show_() {
        myActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        if (myActivity != null) {
            createPanel(myActivity, myRoot);
        }
        if (myWindow != null) {
            myWindow.show();
            setupLight();
        }
    }

    @Override
    protected void hide_() {
        if (myWindow != null) {
            myWindow.hide();
        }
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void update() {
        if (myWindow != null) {
            setupLight();
        }
    }

    private void createPanel(KooReader activity, RelativeLayout root) {
        if (myWindow != null && activity == myWindow.getContext()) {
            return;
        }
        activity.getLayoutInflater().inflate(R.layout.setting_panel, root);
        myWindow = (SettingWindow) root.findViewById(R.id.setting_panel);
        integerRangeOption = myKooReader.ViewOptions.getTextStyleCollection().getBaseStyle().FontSizeOption; // 字体
//        pageMode = myKooReader.PageTurningOptions.Horizontal.getValue(); // 当前翻页方式
//        tvFontSize = (TextView) myWindow.findViewById(R.id.tv_font_size);//字号
//        tvPageMode = (TextView) myWindow.findViewById(R.id.tv_page_mode);
//        if (pageMode) {
//            tvPageMode.setText("上下");
//        } else {
//            tvPageMode.setText("左右");
//        }
        updateFontSize(); // 设置当前字号
        slider = (SeekBar) myWindow.findViewById(R.id.light_slider);
        tvLightMinus = (TextView) myWindow.findViewById(R.id.tv_light_minus);
        tvLightAdd = (TextView) myWindow.findViewById(R.id.tv_light_add);
        tvFontMinus = (TextView) myWindow.findViewById(R.id.tv_font_minus);
        tvFontAdd = (TextView) myWindow.findViewById(R.id.tv_font_add);
        //翻页方式
        tvPageSimulation = (ImageButton) myWindow.findViewById(R.id.tv_page_simulation);//仿真翻页
        tvPageCover = (ImageButton) myWindow.findViewById(R.id.tv_page_cover);//覆盖
        tvPageSlide = (ImageButton) myWindow.findViewById(R.id.tv_page_slide);//滑动
        tvPageNone = (ImageButton) myWindow.findViewById(R.id.tv_page_none);//无
        //背景
        bgWhite = (TextView) myWindow.findViewById(R.id.bg_white);
        bgBook = (TextView) myWindow.findViewById(R.id.bg_book);
        bgPink = (TextView) myWindow.findViewById(R.id.bg_pink);
        bgColor = (TextView) myWindow.findViewById(R.id.bg_color);
        bgGreen = (TextView) myWindow.findViewById(R.id.bg_green);
        //模式
        mTextViewDay = (TextView) myWindow.findViewById(R.id.daymodel);
        mTextViewNight = (TextView) myWindow.findViewById(R.id.nightmodel);
        init();
        selectePurItem(); //y 当前翻页方式

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
                myIsBrightnessAdjustmentInProgress = true;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (myIsBrightnessAdjustmentInProgress) {
                    myIsBrightnessAdjustmentInProgress = false;
                }
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (myIsBrightnessAdjustmentInProgress) {
                        myKooReader.getViewWidget().setScreenBrightness(progress);
                        return;
                    }
                }
            }
        });
    }
    private void init() {
        bgWhite.setOnClickListener(this);
        bgBook.setOnClickListener(this);
        bgPink.setOnClickListener(this);
        bgColor.setOnClickListener(this);
        bgGreen.setOnClickListener(this);
        tvLightMinus.setOnClickListener(this);
        tvLightAdd.setOnClickListener(this);
        tvFontMinus.setOnClickListener(this);
        tvFontAdd.setOnClickListener(this);
        tvPageSimulation.setOnClickListener(this);
        tvPageCover.setOnClickListener(this);
        tvPageSlide.setOnClickListener(this);
        tvPageNone.setOnClickListener(this);
        mTextViewDay.setOnClickListener(this);
        mTextViewNight.setOnClickListener(this);
    }

    public void updateFontSize() {
//        tvFontSize.setText(integerRangeOption.getValue() + "");
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.tv_light_minus) {
            slider.setProgress(slider.getProgress() - 2);
            myKooReader.getViewWidget().setScreenBrightness(slider.getProgress() - 2);
            return;
        } else if (i == R.id.tv_light_add) {
            slider.setProgress(slider.getProgress() + 2);
            myKooReader.getViewWidget().setScreenBrightness(slider.getProgress() + 2);
            return;
        } else if (i == R.id.tv_font_add) {
            integerRangeOption.setValue(integerRangeOption.getValue() + 2);
            myKooReader.clearTextCaches();
            myKooReader.getViewWidget().repaint();
            updateFontSize();
            return;
        } else if (i == R.id.tv_font_minus) {
            integerRangeOption.setValue(integerRangeOption.getValue() - 2);
            myKooReader.clearTextCaches();
            myKooReader.getViewWidget().repaint();
            updateFontSize();
            return;
        } else if (i == R.id.tv_page_simulation) {
            clearSelecte();
            tvPageSimulation.setBackgroundResource(R.mipmap.fangzhen);
            myKooReader.PageTurningOptions.Animation.setValue(ZLView.Animation.curl);
            return;
        } else if (i == R.id.tv_page_cover) {
            clearSelecte();
            tvPageCover.setBackgroundResource(R.mipmap.fugai);
            myKooReader.PageTurningOptions.Animation.setValue(ZLView.Animation.slide);
            return;
        } else if (i == R.id.tv_page_slide) {
            clearSelecte();
            tvPageSlide.setBackgroundResource(R.mipmap.huadong);
            myKooReader.PageTurningOptions.Animation.setValue(ZLView.Animation.shift);
            return;
        } else if (i == R.id.tv_page_none) {
            clearSelecte();
            tvPageNone.setBackgroundResource(R.mipmap.never);
            myKooReader.PageTurningOptions.Animation.setValue(ZLView.Animation.none);
            return;
        } else if (i == R.id.bg_white) {
            setWallpaper("wallpapers/white.png");
            return;
        } else if (i == R.id.bg_book) {
            setWallpaper("wallpapers/book.png");
            return;
        } else if (i == R.id.bg_pink) {
            setWallpaper("wallpapers/pink.png");
            return;
        } else if (i == R.id.bg_color) {
            setWallpaper("wallpapers/black.png");
            return;
        } else if (i == R.id.bg_green) {
            setWallpaper("wallpapers/green.png");
            return;
        } else if (i == R.id.daymodel) {
            setModel(ColorProfile.DAY);

        } else if (i == R.id.nightmodel) {
            setModel(ColorProfile.NIGHT);

        }
    }

    //设置墙纸
    private void setWallpaper(final String wallpaper) {
        myKooReader.ViewOptions.getColorProfile().WallpaperOption.setValue(wallpaper);
        myKooReader.getViewWidget().reset();
        myKooReader.getViewWidget().repaint();
    }

    private void clearSelecte() {
        tvPageSimulation.setBackgroundResource(R.mipmap.fangzhen_d);
        tvPageCover.setBackgroundResource(R.mipmap.fugai_d);
        tvPageSlide.setBackgroundResource(R.mipmap.huadong_d);
        tvPageNone.setBackgroundResource(R.mipmap.never_d);
    }


    private void setupLight() {
        final SeekBar slider = (SeekBar) myWindow.findViewById(R.id.light_slider); //y 屏幕亮度 1~100
        slider.setMax(100);
        slider.setProgress(myKooReader.getViewWidget().getScreenBrightness());
    }

    final void removeWindow(Activity activity) {
        if (myWindow != null && activity == myWindow.getContext()) {
            final ViewGroup root = (ViewGroup) myWindow.getParent();
            myWindow.hide();
            root.removeView(myWindow);
            myWindow = null;
        }
    }

    private void selectePurItem() {
        switch (myKooReader.PageTurningOptions.Animation.getValue().toString()) {
            case "curl":
                tvPageSimulation.setBackgroundResource(R.mipmap.fangzhen);
                return;
            case "slide":
                tvPageCover.setBackgroundResource(R.mipmap.fugai);
                return;
            case "shift":
                tvPageSlide.setBackgroundResource(R.mipmap.huadong);
                return;
            case "none":
                tvPageNone.setBackgroundResource(R.mipmap.never);
                return;
        }
    }

    //设置模式
    private void setModel(String model) {
        myKooReader.ViewOptions.ColorProfileName.setValue(model);
        myKooReader.getViewWidget().reset();
        myKooReader.getViewWidget().repaint();
    }
}