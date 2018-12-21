package com.example.konta.sketch_loyalityapp.common;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * This is the base class which provides basic common functionality to MVC views implementations.
 */
public class BaseViewMvc<ListenerType> extends BaseObservable<ListenerType>
        implements ObservableViewMvc<ListenerType> {

    private View mRootView;

    // ---------------------------------------------------------------------------------------------
    // Root View region

    @Override
    public View getRootView() { return mRootView; }

    /**
     * Set the root android view of this MVC view.
     */
    protected void setRootView(View rootView) { mRootView = rootView; }

    // Root View endregion
    // ---------------------------------------------------------------------------------------------

    /**
     * Convenience method for obtaining references to {@link View}s contained in the hierarchy of
     * the root {@link View}.
     * This method uses Java's type inference in order to automatically cast the returned
     * {@link View}s to the type of the containing variable.
     * @return {@link View} object casted to the type inferred by Java's automatic type inference
     *         mechanism, or null
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(@IdRes int id) {
        return (T) mRootView.findViewById(id);
    }

    /**
     * Convenience method for obtaining reference to {@link Context}
     * @return instance of {@link Context} associated with the root {@link View} of this MVC view.
     */
    protected Context getContext() { return getRootView().getContext(); }

    /**
     * Convenience method for obtaining a string resource.
     */
    protected String getString(@StringRes int id) {return getContext().getString(id); }

    /**
     * Convenience method for obtaining a string resource with format arguments.
     */
    protected String getString(@StringRes int id, Object... formatArgs) {
        return getContext().getString(id, formatArgs);
    }
}
