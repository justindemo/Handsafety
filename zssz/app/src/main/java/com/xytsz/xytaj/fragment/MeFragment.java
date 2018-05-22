package com.xytsz.xytaj.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.AppraiseActivity;
import com.xytsz.xytaj.activity.ForUsActivity;
import com.xytsz.xytaj.activity.MainActivity;
import com.xytsz.xytaj.activity.MyDealedActivity;
import com.xytsz.xytaj.activity.MyInformationActivity;
import com.xytsz.xytaj.activity.MyReporteActivity;
import com.xytsz.xytaj.activity.MyReviewedActivity;
import com.xytsz.xytaj.activity.MySendActivity;
import com.xytsz.xytaj.base.BaseFragment;
import com.xytsz.xytaj.bean.UpdateStatus;
import com.xytsz.xytaj.bean.VersionInfo;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;
import com.xytsz.xytaj.util.UpdateVersionUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admin on 2017/1/4.
 * <p>
 * 我的界面
 */
public class MeFragment extends BaseFragment {

    private static final int ISNUMBER = 111123;
    private static final int RESULT = 12211;

    @Bind(R.id.tv_report_nume)
    TextView tvReportNume;
    @Bind(R.id.tv_deal_number)
    TextView tvDealNumber;
    @Bind(R.id.me_information)
    TextView meInformation;
    @Bind(R.id.me_update)
    TextView meUpdate;
    @Bind(R.id.me_share)
    TextView meShare;
    @Bind(R.id.me_exit)
    Button meExit;
    @Bind(R.id.me_activity)
    LinearLayout meActivity;
    @Bind(R.id.me_appraise)
    TextView meAppraise;
    @Bind(R.id.for_us)
    TextView forUs;
    @Bind(R.id.tv_send_nume)
    TextView tvSendNume;
    @Bind(R.id.ll_my_send)
    LinearLayout llMySend;
    private ImageView mIvicon;
    private TextView mTvLogin;
    private LinearLayout mLLDealed;
    private LinearLayout mLLReported;
    private LinearLayout mLLReview;
    public static final String ARGUMENT = "argument";
    private int personID;
    private List<String> numberlist;
    private boolean isvisitor;
    private TextView mine_tv_sign;
    private int role;
    private String error;
    private String signed;
    private boolean issign;
    private String userName;
    private TextView mActionbartext;
    private TextView tvReviewNum;


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        mActionbartext = (TextView) view.findViewById(R.id.actionbar_text);

        mIvicon = (ImageView) view.findViewById(R.id.iv_my_icon);
        mTvLogin = (TextView) view.findViewById(R.id.tv_my_login);

        mLLReported = (LinearLayout) view.findViewById(R.id.ll_my_reporte);
        mLLReview = (LinearLayout) view.findViewById(R.id.ll_my_review);
        mLLDealed = (LinearLayout) view.findViewById(R.id.ll_my_deal);


        mine_tv_sign = (TextView) view.findViewById(R.id.mine_tv_sign);
        tvReviewNum = (TextView) view.findViewById(R.id.tv_review_nume);

        return view;
    }


    @Override
    public void initData() {

        //遊客loginid == 电话
        personID = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);

        role = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);

        issign = SpUtils.getBoolean(getContext(), GlobalContanstant.SIGN, false);

        error = getString(R.string.visitor_neterror);
        signed = getString(R.string.signed);
        //第一次点进去的时候获取用户名
        userName = SpUtils.getString(getContext(), GlobalContanstant.USERNAME);
        mActionbartext.setText(R.string.me);
        if (!TextUtils.isEmpty(userName)) {
            mTvLogin.setText(userName);
            mTvLogin.setClickable(false);
        }

        //服务器保存到本地的登陆属性：  是否是游客

        if (role != 0) {
           // meActivity.setVisibility(View.VISIBLE);
            //getNumber();
            mine_tv_sign.setVisibility(View.GONE);
        } else {
            //获取是否签到
            if (issign) {
                mine_tv_sign.setText(signed);
            }
        }

        if (role != 1){
//            mLLReview.setVisibility(View.GONE);
//            llMySend.setVisibility(View.GONE);
        }



        //icon 调用图库
        mIvicon.setOnClickListener(listener);
        mine_tv_sign.setOnClickListener(listener);
        mLLReported.setOnClickListener(listener);
        mLLDealed.setOnClickListener(listener);
        mLLReview.setOnClickListener(listener);
        //mTvData.setOnClickListener(listener);
        forUs.setOnClickListener(listener);
    }


    private String getTaskCountOfReport(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfReport);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfReport_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();


        return number;
    }

    private String getTaskCountOfReview(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfReview);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfReview_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();


        return number;
    }

    private String getTaskCountOfDeal(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfDeal);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfDeal_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();

        return number;


    }

    private Bitmap getdiskbitmap(String pathString) {

        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
        }

        return bitmap;

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_my_icon:
                    //
                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1, 200);
                    break;

                case R.id.ll_my_reporte:
                    //点击跳转到自己上报的数据界面
                    IntentUtil.startActivity(v.getContext(), MyReporteActivity.class);
                    break;

                case R.id.ll_my_deal:
                    //点击显示自己处置界面
                    IntentUtil.startActivity(v.getContext(), MyDealedActivity.class);
                    break;

                case R.id.ll_my_review:
                    IntentUtil.startActivity(v.getContext(), MyReviewedActivity.class);
                    break;



                case R.id.for_us:
                    IntentUtil.startActivity(getContext(), ForUsActivity.class);
                    break;

            }
        }
    };


    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //签到的回传
        if (requestCode == 1) {
            if (resultCode == 200) {
                mine_tv_sign.setText(signed);
                mine_tv_sign.setFocusable(false);

            }
        }

        if (requestCode == 300) {
            if (resultCode == 3) {
                userName = SpUtils.getString(getContext(), GlobalContanstant.USERNAME);
                mTvLogin.setText(userName);
            }
        }


        if (resultCode == getActivity().RESULT_CANCELED) {

            return;
        }

        Bitmap photo = null;
        if (data != null) {
            if (requestCode == 200) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                photo = getBitmap(mIvicon, picturePath);
                //保存到本地
                String photoName = saveToSDCard(photo);
                String encode = photo2Base64(path);
                upload(photoName, encode);
                //mIvicon.setImageBitmap(photo);


            }
        }

    }

    private String path;
    private String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/myIcon/";
    private final String pathString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/myIcon/myicon.jpg";

    public String saveToSDCard(Bitmap bitmap) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createFileName();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(iconPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = iconPath + "myicon.jpg";
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);//把图片数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            ToastUtil.shortToast(getActivity(), "SD卡不存在");
        }

        return photoName;
    }


    private Bitmap getBitmap(ImageView imageView, String path) {
        Bitmap bitmap;
        int width = imageView.getWidth();

        int height = imageView.getHeight();

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, factoryOptions);

        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(imageWidth / width, imageHeight
                / height);

        // Decode the image file into a Bitmap sized to fill the
        // View
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(path,
                factoryOptions);
        return bitmap;
    }

    private String photo2Base64(String path) {

        try {
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            byte[] encode = Base64.encode(baos.toByteArray(), Base64.DEFAULT);
            String uploadBuffer = new String(encode);
            Log.i("upload", uploadBuffer);
            fis.close();
            return uploadBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传头像
     *
     * @param photoName ： 照片名称
     * @param encode：   64位编码
     */
    private void upload(final String photoName, final String encode) {
        new Thread() {
            @Override
            public void run() {
                SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.uploadHeadImg);
                soapObject.addProperty("personid", personID);
                soapObject.addProperty("FileName", photoName);  //文件类型
                soapObject.addProperty("ImgBase64String", encode);   //参数2  图片字符串

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.dotNet = true;
                envelope.bodyOut = soapObject;
                envelope.setOutputSoapObject(soapObject);

                HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
                try {
                    httpTransportSE.call(NetUrl.uploadHeadImg_SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String result = object.getProperty(0).toString();

                    Message message = Message.obtain();
                    message.obj = result;
                    message.what = RESULT;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    //e.printStackTrace();
                }


            }
        }.start();


    }

    private String createFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";
        return fileName;

    }


    @Override
    public void onStart() {
        super.onStart();

        Bitmap bitmap = getdiskbitmap(pathString);
        if (bitmap != null) {
            mIvicon.setImageBitmap(bitmap);
        }


    }

    private void getNumber() {
        new Thread() {
            @Override
            public void run() {

                try {
                    String taskCountOfDealNumber = getTaskCountOfDeal(personID);
                    String taskCountOfReportNumber = getTaskCountOfReport(personID);
                    String taskCountOfReviewNumber = getTaskCountOfReview(personID);
                    String taskCountOfSendNumber = getTaskCountOfSend(personID);

                    numbers.clear();

                    numbers.add(taskCountOfDealNumber);
                    numbers.add(taskCountOfReportNumber);
                    numbers.add(taskCountOfReviewNumber);
                    numbers.add(taskCountOfSendNumber);


                    Message message = Message.obtain();
                    message.what = ISNUMBER;
                    message.obj = numbers;
                    handler.sendMessage(message);

                } catch (Exception e) {

                }
            }
        }.start();
    }

    private String getTaskCountOfSend(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfSend);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfReview_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();


        return number;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private List<String> numbers = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case ISNUMBER:

                    numberlist = (List<String>) msg.obj;
                    if (tvReportNume != null && tvDealNumber != null) {
                        tvDealNumber.setText(numberlist.get(0));
                        tvReportNume.setText(numberlist.get(1));
                        tvReviewNum.setText(numberlist.get(2));
                        tvSendNume.setText(numberlist.get(3));

                    }
                    break;

                case RESULT:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (result.equals("true")) {
                            ToastUtil.shortToast(getContext(), "头像设置完成");

                            Bitmap bitmap = getdiskbitmap(pathString);
                            if (bitmap != null) {
                                mIvicon.setImageBitmap(bitmap);
                            }
                        }
                    }
                    break;


                case VERSIONINFO:
                    String info = (String) msg.obj;
                    if (info != null) {
                        //检查更新
                        UpdateVersionUtil.localCheckedVersion(MeFragment.this.getActivity().getApplicationContext(), new UpdateVersionUtil.UpdateListener() {

                            @Override
                            public void onUpdateReturned(int updateStatus, final VersionInfo versionInfo) {
                                //判断回调过来的版本检测状态
                                switch (updateStatus) {
                                    case UpdateStatus.YES:
                                        //弹出更新提示
                                        UpdateVersionUtil.showDialog(MeFragment.this.getActivity(), versionInfo,false);
                                        break;
                                    case UpdateStatus.NO:
                                        //没有新版本
                                        ToastUtil.shortToast(MeFragment.this.getActivity().getApplicationContext(), "已经是最新版本了!");
                                        break;
                                    case UpdateStatus.NOWIFI:
                                        //当前是非wifi网络
                                        //UpdateVersionUtil.showDialog(getContext(),versionInfo);
                                        UpdateVersionUtil.showDialog(MeFragment.this.getActivity(), versionInfo,true);



                                        break;
                                    case UpdateStatus.ERROR:
                                        //检测失败
                                        ToastUtil.shortToast(MeFragment.this.getActivity().getApplicationContext(), "检测失败，请稍后重试！");
                                        break;
                                    case UpdateStatus.TIMEOUT:
                                        //链接超时
                                        ToastUtil.shortToast(MeFragment.this.getActivity().getApplicationContext(), "链接超时，请检查网络设置!");
                                        break;
                                }
                            }
                        }, info);
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        userName = SpUtils.getString(getContext(), GlobalContanstant.USERNAME);
        mTvLogin.setText(userName);
        personID = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
        //getNumber();
        //是否签到
        issign = SpUtils.getBoolean(getContext(), GlobalContanstant.SIGN, false);
        if (issign) {
            mine_tv_sign.setText(signed);
        }

    }

    private static final int VERSIONINFO = 100211;

    @OnClick({R.id.me_information, R.id.me_update, R.id.me_share, R.id.me_exit, R.id.me_appraise,R.id.ll_my_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_information:

                Intent intent = new Intent(view.getContext(), MyInformationActivity.class);
                startActivityForResult(intent, 300);
                break;
            //积分

            //更新
            case R.id.me_update:
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            String versionInfo = UpdateVersionUtil.getVersionInfo();
                            Message message = Message.obtain();
                            message.obj = versionInfo;
                            message.what = VERSIONINFO;
                            handler.sendMessage(message);
                        } catch (Exception e) {

                        }

                    }
                }.start();

                break;
            //分享
            case R.id.me_share:
                PermissionUtils.requestPermission(MeFragment.this.getActivity(), PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermission);
                break;

            //退出登录
            case R.id.me_exit:
                SpUtils.exit(getActivity().getApplicationContext());
                SpUtils.saveBoolean(getContext(), GlobalContanstant.ISFIRSTENTER, false);
                Intent intent1 = new Intent(MeFragment.this.getActivity(), MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                break;
            //软件评价
            case R.id.me_appraise:

                IntentUtil.startActivity(MeFragment.this.getActivity(), AppraiseActivity.class);
                break;

            case R.id.ll_my_send:
                IntentUtil.startActivity(view.getContext(), MySendActivity.class);
                break;
        }
    }

    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.xytgps.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("掌上市政是一款助力中国智慧城市的发展，为中国城市里的市政养护行业提供“规划设计+软件开发+系统集成+行内专业术语规范+运营维护+定期培训考核”的一体化解决方案的App。");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(testPath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.xytgps.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.xytgps.com");

        // 启动分享GUI
        oks.show(MeFragment.this.getActivity());
    }


    private String icpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zssz/Image/";

    private String testPath;
    private PermissionUtils.PermissionGrant mPermission = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    //保存图片
                    Bitmap bitmap = BitmapFactory.decodeResource(MeFragment.this.getActivity().getResources(), R.mipmap.ic_launcher);

                    File file = new File(icpath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    testPath = icpath + "test.jpg";
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(testPath);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
                        showShare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(MeFragment.this.getActivity(), requestCode, permissions, grantResults, mPermission);
    }


}
