package pl.kibao.playground.recycleViewAnimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

public class ColorItemAnimator extends DefaultItemAnimator {
    private HashMap<RecyclerView.ViewHolder, AnimatorInfo> mAnimatorMap = new HashMap<>();

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @Override
    public ItemHolderInfo obtainHolderInfo() {
        return new ColorItemHolderInfo();
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldViewHolder, @NonNull final RecyclerView.ViewHolder newViewHolder, @NonNull ItemHolderInfo preInfo, @NonNull final ItemHolderInfo postInfo) {
        final ColorsAdapter.ColorViewHolder newHolder = (ColorsAdapter.ColorViewHolder) newViewHolder;
        int startColor = ((ColorItemHolderInfo) preInfo).color;
        int newColor = ((ColorItemHolderInfo) postInfo).color;
        final String oldText = ((ColorItemHolderInfo) preInfo).text;
        final String newText = ((ColorItemHolderInfo) postInfo).text;
        ObjectAnimator fadeToBlack = ObjectAnimator.ofArgb(newHolder.container, "backgroundColor", startColor, Color.BLACK);
        ObjectAnimator fadeFromBlack = ObjectAnimator.ofArgb(newHolder.container, "backgroundColor", Color.BLACK, newColor);
        AnimatorSet bgAnim = new AnimatorSet();
        bgAnim.playSequentially(fadeToBlack, fadeFromBlack);

        ObjectAnimator oldTextRotate = ObjectAnimator.ofFloat(newHolder.textView, View.ROTATION_X, 0, 90);
        ObjectAnimator newTextRotate = ObjectAnimator.ofFloat(newHolder.textView, View.ROTATION_X, -90, 0);
        AnimatorSet textAnim = new AnimatorSet();
        textAnim.playSequentially(oldTextRotate, newTextRotate);

        oldTextRotate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                newHolder.textView.setText(oldText);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                newHolder.textView.setText(newText);
            }
        });

        AnimatorSet changeAnim = new AnimatorSet();
        changeAnim.playTogether(bgAnim, textAnim);
        changeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchAnimationFinished(newHolder);
                mAnimatorMap.remove(newHolder);
            }
        });

        AnimatorInfo runningInfo = mAnimatorMap.get(newHolder);
        if (runningInfo != null) {
            boolean firstHalf = runningInfo.oldTextRotator != null && runningInfo.oldTextRotator.isRunning();
            long currentPlayTime = firstHalf ? runningInfo.oldTextRotator.getCurrentPlayTime() : runningInfo.newTextRotator.getCurrentPlayTime();
            runningInfo.overallAnim.cancel();

            if (firstHalf) {
                fadeToBlack.setCurrentPlayTime(currentPlayTime);
                oldTextRotate.setCurrentPlayTime(currentPlayTime);
            } else {
                fadeToBlack.setCurrentPlayTime(fadeToBlack.getDuration());
                oldTextRotate.setCurrentPlayTime(oldTextRotate.getDuration());

                fadeFromBlack.setCurrentPlayTime(currentPlayTime);
                newTextRotate.setCurrentPlayTime(currentPlayTime);
            }
        }

        AnimatorInfo runningAnimInfo = new AnimatorInfo(changeAnim, fadeToBlack, fadeFromBlack, oldTextRotate, newTextRotate);
        mAnimatorMap.put(newHolder, runningAnimInfo);
        changeAnim.start();

        // No other transformations are required.
        // return super.animateChange(oldViewHolder, newViewHolder, preInfo, postInfo);
        return false;
    }

    class ColorItemHolderInfo extends ItemHolderInfo {
        int color;
        String text;

        @Override
        public ItemHolderInfo setFrom(RecyclerView.ViewHolder viewHolder, @AdapterChanges int flags) {
            super.setFrom(viewHolder, flags);

            final ColorsAdapter.ColorViewHolder holder = (ColorsAdapter.ColorViewHolder) viewHolder;
            color = ((ColorDrawable) holder.container.getBackground()).getColor();
            text = (String) holder.textView.getText();

            return this;
        }
    }

    class AnimatorInfo {
        Animator overallAnim;
        ObjectAnimator fadeToBlackAnim, fadeFromBlackAnim, oldTextRotator, newTextRotator;

        public AnimatorInfo(Animator overallAnim, ObjectAnimator fadeToBlackAnim, ObjectAnimator fadeFromBlackAnim, ObjectAnimator oldTextRotator, ObjectAnimator newTextRotator) {
            this.overallAnim = overallAnim;
            this.fadeToBlackAnim = fadeToBlackAnim;
            this.fadeFromBlackAnim = fadeFromBlackAnim;
            this.oldTextRotator = oldTextRotator;
            this.newTextRotator = newTextRotator;
        }
    }
}
