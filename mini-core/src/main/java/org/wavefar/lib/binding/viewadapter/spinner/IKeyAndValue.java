package org.wavefar.lib.binding.viewadapter.spinner;

/**
 * 下拉Spinner控件的键值对, 实现该接口,返回key,value值, 在xml绑定<pre>{@code List<IKeyAndValue>}</pre>
 * @author Administrator
 */
public interface IKeyAndValue {
    String getKey();

    String getValue();
}
