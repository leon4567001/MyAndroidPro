package com.yfas.aliu4830.webservice;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONStringer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class AxisWebservice extends AppCompatActivity {
    //定义命名空间常量
    static final String namespace = "http://service.com";
    //调用参数
    static final String addurl = "http://192.168.1.18:8080/myWeb/services/SayHello";
    static final String addmethod = "add";
    static final String querymethod = "getCardNo";
    static final String addsoapAction = "http://service.com/add";

    //定义空参数
    private Handler handler = null;
    private String result = "";
    private String queryno = "";
    private EditText editTexti = null;
    private EditText editTextj = null;
    private EditText editTextr = null;
    private EditText edtname = null;
    private EditText edtcardno = null;
    private Button btnquery = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axis);

        //创建属于主线程的handler
        handler = new Handler();

        //定义控件
        editTexti = findViewById(R.id.i);
        editTextj = findViewById(R.id.j);
        editTextr = findViewById(R.id.result);
        Button btn = findViewById(R.id.ca);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        SoapObject soapObject = new SoapObject(namespace, addmethod);
                        soapObject.addProperty("i", Integer.parseInt(editTexti.getText().toString()));//必须转换为INT,神奇的C#可以,java不行
                        soapObject.addProperty("j", Integer.parseInt(editTextj.getText().toString()));
                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.bodyOut = soapObject;
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(soapObject);
                        HttpTransportSE httpTransportSE = new HttpTransportSE(addurl);
                        try {
                            httpTransportSE.call(addsoapAction, envelope);
                            Log.d("ljj1", "add: " + "成功调用axis");
                            //Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("ljj1", "add: " + "调用axis失败:" + e.toString());
                            //Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_SHORT).show();
                        }

                        if (envelope.bodyIn instanceof SoapFault) {
                            try {
                                soapObject = (SoapObject) envelope.getResponse();
                            } catch (SoapFault soapFault) {
                                soapFault.printStackTrace();
                                Log.d("ljj2", "add: " + "无法解析" + soapFault.toString());
                            }
                            Log.d("ljj2", "add: " + "无法解析");
                        } else {
                            try {
                                //获得服务数据开始解析
                                SoapObject object = (SoapObject) envelope.bodyIn;
                                Log.d("ljj2", "add: " + "开始解析");
                                //Toast.makeText(MainActivity.this,"成功获取数据",Toast.LENGTH_SHORT).show();
                                //result = object.getProperty("addResult").toString();
                                result = object.getProperty("addReturn").toString();
                                Log.d("ljj2", "解析结果: " + result);
                                //通过runable对象将数值传出去
                                handler.post(runnableUi);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("ljj2", "add: " + "解析失败:" + e.toString());
                            }
                        }



                       /*
                       报错:Only the original thread that created a view hierarchy can touch its views.
                       在子线程中不能改变view中的内容
                       try{
                        editTextr.setText(result);}catch (Exception e){
                            e.printStackTrace();
                            Log.d("ljj3", "error: "+e.toString());
                        }*/
                        //editTextr.setText(result);
                    }
                }.start();
            }
        });

        //定义控件
        edtname = findViewById(R.id.name);
        edtcardno = findViewById(R.id.cardno);
        btnquery = findViewById(R.id.query);
        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建子线程
                new Thread() {
                    //重写run
                    @Override
                    public void run() {
                        //创建soap对象,传入命名空间和调用方法
                        SoapObject soapObject = new SoapObject(namespace, querymethod);
                        //传入参数
                        soapObject.addProperty("name", edtname.getText().toString());
                        //创建envolpe对象
                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.bodyOut = soapObject;
                        //如果是.net的webservice需要加下面这句
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(soapObject);
                        //addurl是服务地址包含服务名称和后缀
                        HttpTransportSE httpTransportSE = new HttpTransportSE(addurl);
                        try {
                            //传入action要对啊,namespace+methodname
                            httpTransportSE.call("http://tempuri.org/getCardNo", envelope);
                            Log.d("ljj3", "add: " + "成功调用");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("ljj3", "add: " + "调用失败" + e.toString());
                        }

                        //查询cardno
                        SoapObject object = (SoapObject) envelope.bodyIn;
                        //根据webservice中标签<getCardNoResult>string</getCardNoResult>确定或提供索引
                        queryno = object.getProperty("getCardNoResult").toString();
                        handler.post(runnableUi);
                    }
                }.start();
            }
        });

    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            //更新界面
            editTextr.setText(result);
            edtcardno.setText(queryno);
        }
    };
}
