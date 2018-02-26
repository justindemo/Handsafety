package com.xytsz.xytaj;




import com.baidu.mapapi.SDKInitializer;

import com.mob.MobApplication;
import com.xytsz.xytaj.net.NetUrl;



import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;




/**
 * Created by admin on 2017/1/11.
 *
 */
public class MyApplication extends MobApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化SDK
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        SDKInitializer.initialize(this);
        //分享


    }


    public static String getAllPersonList() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPersonList);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getPersonList_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;

    }


    public static String getAllImagUrl(String taskNumber, int phaseIndication) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllImageURLmethodName);
        soapObject.addProperty("DeciceCheckNum", taskNumber);
        soapObject.addProperty("PhaseId", phaseIndication);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getAllImageURL_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


}
