package com.xytsz.xytaj.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.xytsz.xytaj.activity.PhotoShowActivity;
import com.xytsz.xytaj.activity.SendRoadDetailActivity;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.TimeChoiceButton;
import com.xytsz.xytaj.R;

import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/17.
 * xiapai
 */
public class SendRoadAdapter extends BaseAdapter {


    private static final int ISSEND = 1000002;
    private static final int ISSENDPERSON = 1000003;
    private static final int ISSENDBACK = 1000004;

    private String str;
    private List<Review> reviews;
    private List<List<ImageUrl>> imageUrlLists;
    private String[] servicePerson;
    private List<Person> personlist;
    private List<AudioUrl> audioUrls;
    private int personid;
    private Handler handler;
    private int requirementsComplete_person_id;
    private String imgurl;
    private EditText etAdvice;
    private SoundUtil soundUtil;
    private String advise;

    private Context context;

    public SendRoadAdapter(Context context, Handler handler, List<Review> reviews,
                           List<List<ImageUrl>> imageUrlLists,
                           List<Person> personlist,
                           List<AudioUrl> audioUrls, int personid) {
        this.handler = handler;
        this.reviews = reviews;
        this.imageUrlLists = imageUrlLists;
        this.context = context;
        //this.servicePerson = servicePerson;
        this.personlist = personlist;
        this.audioUrls = audioUrls;
        this.personid = personid;

        this.servicePerson = new String[personlist.size()];
        for (int i = 0; i < servicePerson.length; i++) {
            this.servicePerson[i] = personlist.get(i).getName();
        }

        soundUtil = new SoundUtil();
    }

    @Override
    public int getCount() {
        //假数据
        //根据id去返回不同的数组

        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_send, null);
            holder.Vname = (TextView) convertView.findViewById(R.id.tv_sendroad_Vname);
            holder.Pname = (TextView) convertView.findViewById(R.id.tv_send_Pname);
            holder.cb = (CheckBox) convertView.findViewById(R.id.sendroad_checkbox);
            holder.tvProblemAudio = (TextView) convertView.findViewById(R.id.tv_send_audio);
            holder.date = (TextView) convertView.findViewById(R.id.tv_send_date);
            holder.bottom = (LinearLayout) convertView.findViewById(R.id.ll_sendroad_bottom);
            holder.detail = (RelativeLayout) convertView.findViewById(R.id.rl_send_road_detail);
            holder.sendIcon = (ImageView) convertView.findViewById(R.id.iv_send_photo);
            holder.btSend = (TextView) convertView.findViewById(R.id.bt_send_send);
            holder.btSendBack = (TextView) convertView.findViewById(R.id.bt_send_back);
            holder.btChoice = (TimeChoiceButton) convertView.findViewById(R.id.bt_send_choice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Review reviewRoadDetail = reviews.get(position);
        //String upload_person_id = reviewRoadDetail.getUpload_Person_ID() + "";
        //通过上报人的ID 拿到上报人的名字
        //获取到所有人的列表 把对应的 id 找出名字
//        List<String> personNamelist = SpUtils.getStrListValue(parent.getContext(), GlobalContanstant.PERSONNAMELIST);
//        List<String> personIDlist = SpUtils.getStrListValue(parent.getContext(), GlobalContanstant.PERSONIDLIST);
//
//        for (int i = 0; i < personIDlist.size(); i++) {
//            if (upload_person_id.equals(personIDlist.get(i))) {
//                id = i;
//            }
//        }

        //String userName = personNamelist.get(id);

        String userName = reviewRoadDetail.getCheckPersonName();
        String uploadTime = reviewRoadDetail.getCheckTime();

        holder.btChoice.setReviewRoadDetail(this, reviewRoadDetail);
        //String userName = SpUtils.getString(parent.getContext(), GlobalContanstant.USERNAME);
        //赋值
        holder.Vname.setText(userName);
        holder.Pname.setText(reviewRoadDetail.getRemarks());
        holder.date.setText(uploadTime);


        //是否显示底部。
        if (reviewRoadDetail.isShow()) {
            holder.bottom.setVisibility(View.GONE);
            holder.cb.setVisibility(View.VISIBLE);
        } else {
            holder.bottom.setVisibility(View.VISIBLE);
            holder.cb.setVisibility(View.GONE);
        }

        holder.cb.setChecked(reviewRoadDetail.isCheck());


        //点击事件
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reviewRoadDetail.isMultiSelect()) {
                    if (holder.cb.isChecked()) {
                        holder.cb.setChecked(true);
                        reviewRoadDetail.setCheck(true);
                        reviewRoadDetail.setPosition(position);
                    } else {
                        holder.cb.setChecked(false);
                        reviewRoadDetail.setCheck(false);
                    }
                }
            }
        });

        holder.btChoice.setClickable(true);
        holder.btChoice.setFocusable(true);

        if (reviewRoadDetail.getRequestTime() == null) {
            holder.btChoice.setText("要求时间");
        } else {
            holder.btChoice.setText(reviewRoadDetail.getRequestTime());
        }


        // 根据position设置CheckBox是否可见，是否选中

        // ListView每一个Item的长按事件
        holder.detail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.OnLongclick(v, position);
                return true;
            }
        });


        //选择派发人
        holder.btSend.setTag(position);
        holder.btSendBack.setTag(position);

        holder.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //点击的时候弹出对话框 都是一样的 人员名字

                //改变bean类的参数
                if (reviewRoadDetail.getRequestTime() == null) {
                    ToastUtil.shortToast(view.getContext(), "请先选择要求时间");

                } else {
                    final Dialog dialog = new AlertDialog.Builder(context).create();
                    dialog.setCancelable(true);// 可以用“返回键”取消
                    dialog.setCanceledOnTouchOutside(true);//
                    dialog.show();
                    dialog.setContentView(R.layout.sendroad_choiceperson);
                    ListView lv = (ListView) dialog.findViewById(R.id.lv_sendroad_list);
                    final EditText mEtAdvice = (EditText) dialog.findViewById(R.id.et_sendroad_advice);
                    Button mBtOk = (Button) dialog.findViewById(R.id.btn_sendroad_ok);
                    Button mBtCancle = (Button) dialog.findViewById(R.id.btn_sendroad_cancel);
                    SendroadAdviceAdapter sendroadAdviceAdapter = new SendroadAdviceAdapter(personlist, reviewRoadDetail);

                    if (lv != null) {
                        lv.setAdapter(sendroadAdviceAdapter);

                    }
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                    mBtCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    mBtOk.setOnClickListener(new View.OnClickListener() {

                        private String advice;

                        @Override
                        public void onClick( View v) {

                            advice = mEtAdvice.getText().toString();
                            dialog.dismiss();

                            str  = reviewRoadDetail.getSendPerson();

                            new AlertDialog.Builder(v.getContext()).setTitle("维修人").
                                    setMessage("确定让：" + str + " 维修?").setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            Message message = Message.obtain();
                                            message.what = ISSENDPERSON;
                                            message.obj = str;
                                            handler.sendMessage(message);

                                            //上传服务器数据
                                            final int passposition = (int) view.getTag();
                                            SendRoadAdapter.this.taskNumber = reviewRoadDetail.getDeciceCheckNum();
                                            requstPersonID = reviewRoadDetail.getSendpersonID();
                                            requstTime = getRequstTime(passposition);
                                            new Thread() {
                                                @Override
                                                public void run() {

                                                    try {
                                                        String isSend = toDispatching(SendRoadAdapter.this.taskNumber, requstPersonID, requstTime, GlobalContanstant.GETDEAL, personid,advice);

                                                        Message message = Message.obtain();
                                                        message.what = ISSEND;
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt("passposition", passposition);
                                                        bundle.putString("issend", isSend);
                                                        message.setData(bundle);
                                                        //message.obj = isSend;
                                                        handler.sendMessage(message);
                                                    } catch (Exception e) {

                                                    }
                                                }
                                            }.start();

                                            reviews.remove(position);
                                            imageUrlLists.remove(position);
                                            audioUrls.remove(position);
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();

                        }
                    });

                }
            }
        });

        if (reviewRoadDetail.getSendPerson() == null) {
            holder.btSend.setText("派发");
        } else {
            holder.btSend.setText(reviewRoadDetail.getSendPerson());

        }

        //获取到当前点击的URL集合
        if (imageUrlLists.size() != 0) {
            urlList = imageUrlLists.get(position);
            //显示的第一张图片
            if (urlList.size() != 0) {
                ImageUrl imageUrl = urlList.get(0);
                imgurl = imageUrl.getImgurl();

                Glide.with(parent.getContext()).load(imgurl).into(holder.sendIcon);
                holder.sendIcon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PhotoShowActivity.class);
                        intent.putExtra("imageUrllist", (Serializable) imageUrlLists.get(position));
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                Glide.with(parent.getContext()).load(R.mipmap.prepost).fitCenter().into(holder.sendIcon);
            }

        }


        //判断是否有语音
        if (reviewRoadDetail.getRemarks().isEmpty()) {
            final AudioUrl audioUrl = audioUrls.get(position);
            if (audioUrl != null) {
                if (audioUrl.getAudioUrl() != null) {
                    if (!audioUrl.getAudioUrl().equals("false")) {
                        if (!audioUrl.getTime().isEmpty()) {
                            holder.Pname.setVisibility(View.GONE);
                            holder.tvProblemAudio.setVisibility(View.VISIBLE);

                            holder.tvProblemAudio.setText(audioUrl.getTime());

                            holder.tvProblemAudio.setOnClickListener(new View.OnClickListener() {


                                @Override
                                public void onClick(View v) {

                                    Drawable drawable = parent.getContext().getResources().getDrawable(R.mipmap.pause);
                                    final Drawable drawableRight = parent.getContext().getResources().getDrawable(R.mipmap.play);
                                    final TextView tv = (TextView) v;
                                    tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

                                    soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                        @Override
                                        public void onFinish() {
                                            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);

                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });

                                    soundUtil.play(audioUrl.getAudioUrl());
                                }
                            });
                        }
                    } else {
                        holder.Pname.setVisibility(View.VISIBLE);
                        holder.tvProblemAudio.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.Pname.setVisibility(View.VISIBLE);
                holder.tvProblemAudio.setVisibility(View.GONE);
            }

        } else {
            holder.Pname.setVisibility(View.VISIBLE);
            holder.tvProblemAudio.setVisibility(View.GONE);
        }

        holder.detail.setTag(position);
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position1 = (int) v.getTag();
                // 处于多选模式
                if (reviewRoadDetail.isMultiSelect()) {
                    if (holder.cb.isChecked()) {
                        holder.cb.setChecked(false);
                        reviewRoadDetail.setCheck(false);
                    } else {
                        holder.cb.setChecked(true);
                        reviewRoadDetail.setCheck(true);
                        reviewRoadDetail.setPosition(position1);
                    }
                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(v.getContext(), SendRoadDetailActivity.class);
                    intent.putExtra("detail", reviews.get(position1));
                    intent.putExtra("audioUrl", audioUrls.get(position1));
                    intent.putExtra("position", position1);
                    intent.putExtra("tag",2);
                    intent.putExtra("imageUrls", (Serializable) imageUrlLists.get(position1));
                    v.getContext().startActivity(intent);
                }
            }
        });

        //修改
        holder.btSendBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //去除当前条目
                final int failposition = (int) v.getTag();
                final String taskNumber = reviewRoadDetail.getDeciceCheckNum();

                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                View view = View.inflate(v.getContext(), R.layout.dialog_reject, null);
                etAdvice = (EditText) view.findViewById(R.id.dialog_et_advise);
                final Button btnOk = (Button) view.findViewById(R.id.btn_ok);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
                RadioGroup radiogroup = (RadioGroup) view.findViewById(R.id.back_rg);

                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.noblong_me_rb:
                                advise = "非管护段";
                                break;
                            case R.id.noblong_rb:
                                advise = "非权属";
                                break;
                        }
                    }
                });


                dialog.setView(view);
                dialog.show();

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String advice = advise + " " + etAdvice.getText().toString();
                        backList.add(taskNumber);
                        backList.add(advice);


                        Message message = Message.obtain();
                        message.what = ISSENDTASK;

                        Bundle bundle = new Bundle();
                        bundle.putInt("failposition", failposition);
                        bundle.putString("taskNumber", taskNumber);
                        bundle.putString("advice", advice);
                        message.setData(bundle);

                        //message.obj = backList;
                        handler.sendMessage(message);

                        reviews.remove(failposition);
                        imageUrlLists.remove(failposition);
                        audioUrls.remove(failposition);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                notifyDataSetChanged();


            }
        });


        return convertView;
    }

    private static final int ISSENDTASK = 1000005;
    private List<String> backList = new ArrayList<>();
    private int id;
    private List<ImageUrl> urlList;
    private List<Boolean> b = new ArrayList<>();

    private String getRequstTime(int position) {
        Review reviewRoadDetail = reviews.get(position);
        String requestTime = reviewRoadDetail.getRequestTime();

        return requestTime;
    }


    static class ViewHolder {
        public TextView Vname;
        public TextView date;
        public TextView Pname;
        public CheckBox cb;
        public LinearLayout bottom;
        public TextView tvProblemAudio;
        public ImageView sendIcon;
        public TextView btSend;
        public RelativeLayout detail;
        public TextView btSendBack;
        public TimeChoiceButton btChoice;

    }

    private String requstTime;
    private String taskNumber;
    private int requstPersonID;


    public String toDispatching(String taskNumber, int requstPersonID, String requestTime, int phaseIndication, int personid,String advice) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.sendmethodName);
        soapObject.addProperty("DeciceCheckNum", taskNumber);
        soapObject.addProperty("RequirementsComplete_Person_ID", requstPersonID);
        soapObject.addProperty("RequirementsCompleteTime", requestTime);
        soapObject.addProperty("opinion", advice);
        soapObject.addProperty("state", phaseIndication);
        soapObject.addProperty("personId", personid);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.toDispatching_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;

    }


    public interface OnItemLongClickListeners {
        void OnLongclick(View v, int position);
    }

    private OnItemLongClickListeners onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListeners onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
