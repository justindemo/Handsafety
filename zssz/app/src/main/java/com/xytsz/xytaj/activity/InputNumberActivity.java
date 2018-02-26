package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/2/23.
 * <p/>
 * 输入设备号
 */
public class InputNumberActivity extends AppCompatActivity {
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.et_input_number)
    EditText etInputNumber;
    @Bind(R.id.tv_report)
    TextView tvReport;
    private String tip1;
    private String tip;
    private Handler handler  = new Handler(){};
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
             tvTip.setText(tip);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputnumber);
        ButterKnife.bind(this);

        initActionBar();

        tip = getResources().getString(R.string.tv_tip);
        tip1 = getResources().getString(R.string.tv_tip1);
        tvTip.setText(tip);

        etInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tvTip.setText(tip1);
                tvReport.setBackgroundResource(R.drawable.shape_btn_ok);
                if (count == 0){
                    tvReport.setBackgroundResource(R.drawable.shape_btn_cancle);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                handler.postDelayed(runnable,3000);

            }
        });


    }

    private void initActionBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("数字上报");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.tv_report)
    public void onViewClicked() {
        //获取数据
        String number = etInputNumber.getText().toString();

        if (!number.isEmpty()){
            Intent  intent = new Intent(InputNumberActivity.this,ReportActivity.class);
            intent.putExtra("number",number);
            startActivity(intent);
        }else {
            ToastUtil.shortToast(getApplicationContext(),"请先输入设备号");
        }

    }
}
