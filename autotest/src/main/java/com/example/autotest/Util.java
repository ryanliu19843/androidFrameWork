package com.example.autotest;

import android.widget.SeekBar;

import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.widget.MImageView;

public class Util {


    public static String bindImgtest(AutoFit fit) {
        SeekBar round = (SeekBar) fit.findView(R.id.seek);
        SeekBar blur = (SeekBar) fit.findView(R.id.blur);
        MImageView img = (MImageView) fit.findView(R.id.gif);
        blur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img.setUserAnim(false);
                img.setBlur(progress);
                img.setObj(img.getObj());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        round.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img.setRound(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return "";
    }
}
