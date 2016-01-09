package pl.kibao.playground.recycleViewAnimations;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;

/**
 * ObjectAnimator BC
 */
public class ObjectAnimatorHelper {
    public static ObjectAnimator ofArgb(Object target, String propertyName, int... values) {
        ObjectAnimator animator = ObjectAnimator.ofInt(target, propertyName, values);
        animator.setEvaluator(new ArgbEvaluator());
        return animator;
    }
}
