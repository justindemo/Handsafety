package com.xytsz.xytaj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.AnwerInfo;
import com.xytsz.xytaj.util.CallBackUtil;

/**
 * ViewPager切换的View,用来显示题
 * <p>
 *
 */

public class ReadFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  int score;

    private AnwerInfo.SubDataBean subDataBean;
    private View view;
    private LinearLayout mllanswer;
    private RadioGroup mRg;
    private RadioButton mreadRB_a;
    private RadioButton mreadRB_b;
    private RadioButton mreadRB_c;
    private RadioButton mreadRB_d;
    private int position;


    public ReadFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReadFragment.
     */
    public static ReadFragment newInstance(AnwerInfo.SubDataBean subDataBean) {
        ReadFragment fragment = new ReadFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, subDataBean);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subDataBean = (AnwerInfo.SubDataBean) getArguments().getSerializable(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read, container, false);

        initView();
        return view;
    }



    private void initView() {
        TextView tv_question = (TextView) view.findViewById(R.id.tv_question);
        TextView tv_answer = (TextView) view.findViewById(R.id.tv_answer);
        mRg = (RadioGroup) view.findViewById(R.id.read_radiogroup);
        mreadRB_a = (RadioButton) view.findViewById(R.id.read_rb_a);
        mreadRB_b = (RadioButton) view.findViewById(R.id.read_rb_b);
        mreadRB_c = (RadioButton) view.findViewById(R.id.read_rb_c);
        mreadRB_d = (RadioButton) view.findViewById(R.id.read_rb_d);
        mllanswer = (LinearLayout) view.findViewById(R.id.ll_answer);
        initClick(true);

        tv_question.setText(subDataBean.getQuestionid() + ". " + subDataBean.getQuestion());
        mreadRB_a.setText(subDataBean.getOptiona());
        mreadRB_b.setText(subDataBean.getOptionb());
        mreadRB_c.setText(subDataBean.getOptionc());
        mreadRB_d.setText(subDataBean.getOptiond());
        tv_answer.setText(subDataBean.getAnswer());

        if (subDataBean.getOptionc().isEmpty() ||subDataBean.getOptionc() ==null){
            mreadRB_c.setVisibility(View.GONE);
            mreadRB_d.setVisibility(View.GONE);
        }else if (subDataBean.getOptiond().isEmpty() || subDataBean.getOptiond() == null){
            mreadRB_d.setVisibility(View.GONE);
        }


        position = subDataBean.getQuestionid();
        score =subDataBean.getScore();



        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.read_rb_a:
                        //保存答案 并进入下一题，
                        subDataBean.setUserAnswer("A");
                        break;
                    case R.id.read_rb_b:
                        //保存答案 并进入下一题，
                        subDataBean.setUserAnswer("B");

                        break;
                    case R.id.read_rb_c:
                        //保存答案 并进入下一题，
                        subDataBean.setUserAnswer("C");
                        break;
                    case R.id.read_rb_d:
                        //保存答案 并进入下一题，
                        subDataBean.setUserAnswer("D");
                        break;
                }

                subDataBean.setVisibility(true);
                if (isAnswer(subDataBean.getUserAnswer())){
                    //分数加2
                    score = subDataBean.getMark();
                    subDataBean.setScore(score);
                }
                mllanswer.setVisibility(View.VISIBLE);
                //其他的不能点击
                initClick(false);
                CallBackUtil.doNext(position);

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        //是否有保存的答案 ，如果有就显示没有就不显示
        String userAnswer = subDataBean.getUserAnswer();
        if (userAnswer != null){
            switch (userAnswer){
                case "A":
                    mRg.check(R.id.read_rb_a);
                    break;
                case "B":
                    mRg.check(R.id.read_rb_b);
                    break;
                case "C":
                    mRg.check(R.id.read_rb_c);
                    break;
                case "D":
                    mRg.check(R.id.read_rb_d);
                    break;
            }

        }

        if (subDataBean.isVisibility()){
            mllanswer.setVisibility(View.VISIBLE);
        }
    }

    private void initClick(boolean isClick) {
        mreadRB_a.setClickable(isClick);
        mreadRB_b.setClickable(isClick);
        mreadRB_c.setClickable(isClick);
        mreadRB_d.setClickable(isClick);

    }

    private boolean isAnswer(String answer) {
        return TextUtils.equals(answer,subDataBean.getAnswer());
    }
    private OnReadFragmentListener mListener;

    public interface OnReadFragmentListener{
         void onNext(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
