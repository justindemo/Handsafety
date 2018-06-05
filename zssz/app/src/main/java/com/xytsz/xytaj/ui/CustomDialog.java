package com.xytsz.xytaj.ui;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xytsz.xytaj.R;

/**
 * Created by admin on 2018/5/30.
 * 自定义弹窗
 */
public class CustomDialog {


    private Dialog mDialog;
    private Context context;
    private RelativeLayout rlContent;
    private TextView tvContent;
    private TextView tvCancle;
    private TextView tvEnsure;
    private ImageView icContentIcon;

    public CustomDialog(Context context) {
        this.context = context;
        mDialog = new Dialog(context, R.style.customDialogStyle);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.customdialog, null);
        mDialog.setContentView(dialogView);
        findView();

    }

    private void findView() {
        tvContent = (TextView) mDialog.findViewById(R.id.tv_default_content_text);
        tvCancle = (TextView) mDialog.findViewById(R.id.tv_cancle);
        tvEnsure = (TextView) mDialog.findViewById(R.id.tv_ensure);
        icContentIcon = (ImageView) mDialog.findViewById(R.id.ic_content_icon);
    }

    /**
     * 左边按钮点击事件
     *
     * @param listener
     */
    public CustomDialog setLeftOnClick(View.OnClickListener listener,String cancleTxt) {
        tvCancle.setOnClickListener(listener);
        tvCancle.setText(cancleTxt);
        return this;
    }

    /**
     * 右边按钮点击事件
     * @param listener
     */
    public CustomDialog setRightOnClick(View.OnClickListener listener,String ensureTxt) {
        tvEnsure.setOnClickListener(listener);
        tvEnsure.setText(ensureTxt);
        return this;
    }

    /**
     * 设置详情
     * @param detial
     */
    public CustomDialog setDetial(String detial) {
        tvContent.setText(detial);
        return this;
    }

    /**
     * 设置字体颜色
     * @param color
     */
    public CustomDialog setDetialTxtColor(int color) {
        tvContent.setTextColor(color);
        return this;
    }

    /**
     * 设置并显示图片
     * @param resId
     * @return
     */
    public CustomDialog setContentIcon(int resId){
        icContentIcon.setImageResource(resId);
        icContentIcon.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置图片的位置
     * @return
     */
    public CustomDialog setIconPosition(){
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        int iconHeight = (int) ((metric.density)*40);
        int iconWidth = (int) ((metric.density)*150);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iconWidth,iconHeight);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        icContentIcon.setLayoutParams(params);
        return this;
    }

    /**
     * 设置内容背景图片
     */
    public CustomDialog setContentBackground(int resId){
        rlContent.setBackgroundResource(resId);
        return this;
    }

    /**
     * 单按钮
     */
    public CustomDialog hideCancleBtn(){
        tvCancle.setVisibility(View.GONE);
        return this;
    }

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }
}
