package org.wavefar.lib.binding.viewadapter.viewgroup;

import android.databinding.ViewDataBinding;

/**
 * @param <V>
 * @author summer
 */
public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}
