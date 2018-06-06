package lib.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * フラグメント関連ユーティリティークラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class FragmentUtils {

	
	// ==============================
	// Accesser
	// ==============================
	/**
	 * フラグメントを差し替える
	 * @param fragmentManager
	 * @param containerID
	 * @param fragment
	 * @param fragmentTag
	 * @param addBackStack
	 */
    public static final void replaceFragment(FragmentManager fragmentManager, int containerID, Fragment fragment,
    		String fragmentTag, boolean addBackStack) {
        replaceFragment(fragmentManager, containerID, fragment, fragmentTag, addBackStack, new FragmentTranslateAnimationBase());
    }
    
    /**
     * フラグメントを差し替える
     * @param fragmentManager
     * @param containerID
     * @param fragment
     * @param fragmentTag
     * @param addBackStack
     * @param animation
     */
    public static final void replaceFragment(FragmentManager fragmentManager, int containerID, Fragment fragment, 
    		String fragmentTag, boolean addBackStack, FragmentTranslateAnimationBase animation) {
    	FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animation.isNeed()) {
        	transaction.setCustomAnimations(
                    animation.getRightEnter(),
                    animation.getLeftExit(),
                    animation.getLeftEnter(),
                    animation.getRightExit());
        }
        transaction.replace(containerID, fragment, fragmentTag);
        if (addBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    
	
}
