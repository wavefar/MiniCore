package com.lq.cxy.shop.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RatingBar;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.entity.EvaluateEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.KeyBoardUtil;
import org.wavefar.lib.utils.ToastUtils;

/**
 * 评价dialog
 * @author summer
 */
public class PostEvaluateDialog extends Dialog implements OnClickListener{


    private GoodsAPI mGoodsAPI;
    private EditText contextTv;
    private RatingBar ratingBar;
    private OrderEntity orderEntity;
    private Context mContext;
    public PostEvaluateDialog(Context context,OrderEntity orderEntity) {
        super(context, R.style.dialog);
        mContext = context;
        mGoodsAPI = new GoodsAPI();
        this.orderEntity  = orderEntity;
        initView();
    }


    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_post_evaluate, null);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_ok).setOnClickListener(this);

        contextTv = findViewById(R.id.contextTv);
        ratingBar = findViewById(R.id.ratingBar);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                KeyBoardUtil.showSoftInput(contextTv);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                submitData();
                break;
            default:
                break;
        }
    }

    private void submitData() {
       String context = contextTv.getText().toString().trim();
       float rating = ratingBar.getRating();
        if (TextUtils.isEmpty(context)) {
            ToastUtils.showShort("要说点什么哦");
            return;
        }

       EvaluateEntity evaluateEntity = new EvaluateEntity();
       evaluateEntity.setEvaluate(context);
       evaluateEntity.setStar((int)rating);
       ProductEntity productEntity = orderEntity.getGoods().get(0);
       evaluateEntity.setOrderId(productEntity.getOrderId());
       evaluateEntity.setGoodsId(productEntity.getGoodsId());

        AlertUtils.showProgressDialog(mContext,"","请求中...");
        mGoodsAPI.saveEvaluate(evaluateEntity, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                AlertUtils.closeProgressDialog();
                ToastUtils.showShort(response.getMessage());
                if (response.isSuccess()) {
                    dismiss();
                }
            }
        });
   }

}
