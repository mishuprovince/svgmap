package com.example.simplesvg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.Nullable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class mapview  extends View {

    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFB0D7F8};

    private List<Province> provinces=new ArrayList<>();
    private Province selectItem;
    private Paint paint;
    private Context context;
    private float scale;




//    public mapview(Context context) {
//        super(context);
//        init(context);
//    }

    public mapview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

//    public mapview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }

    private void setmapResource(int mapResource){

    }

    private void init(Context context){
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        loadThread.start();
//        loadSVG();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (provinces != null) {
            canvas.scale(scale, scale);
            for (Province item : provinces) {
                if (item != selectItem) {
                    item.draw(canvas, paint, false);
                }
            }
            if (selectItem != null) {
                selectItem.draw(canvas, paint, true);
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (provinces != null) {
            Province provice = null;
            for ( Province item : provinces) {
                if (item.isSelect((int) (event.getX() / scale), (int) (event.getY() / scale))) {
                    provice = item;
                    break;
                }
            }
            if (provice != null) {
                selectItem = provice;
                postInvalidate();
            }
        }
        return true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        scale = Math.min(width/2000.0f, height/1000.0f);
    }
    //    private void loadSVG() {
//        provinces = new ArrayList<>();
//        ThreadPoolUtils.execute(new Runnable() {
//            @Override
//            public void run() {
//                InputStream inputStream = context.getResources().openRawResource(R.raw.chinahigh);
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//获取DocumentBuilderFactory
//                DocumentBuilder builder = null;
//
//                try {
//                    builder = factory.newDocumentBuilder();//从factory中获取DocumentBuilder 实例
//                    Document doc = builder.parse(inputStream);
//                    Element rootElement = doc.getDocumentElement();//dom解析
//                    NodeList items = rootElement.getElementsByTagName("path");//把所有包含path的节点拿出来
//
//                    for (int i = 0; i < items.getLength(); i++) {
//                        Element element = (Element) items.item(i);
//                        String pathData = element.getAttribute("android:pathData");//读取path的数据
//                        Path path = PathParser.createPathFromPathData(pathData);//通过工具类解析出Path
//                        Province province = new Province(path);
//                        provinces.add(province);
//                        //重绘
//                        handler.sendEmptyMessage(1);
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (provinces != null) {
//                int totalNumber = provinces.size();
//                for (int i = 0; i < totalNumber; i++) {
//                    int color = Color.WHITE;
//                    //每隔四个省换个颜色 这里随机分配颜色
//                    int flag = i % 4;
//                    switch (flag) {
//                        case 1:
//                            color = colorArray[0];
//                            break;
//                        case 2:
//                            color = colorArray[1];
//                            break;
//                        case 3:
//                            color = colorArray[2];
//                            break;
//                        default:
//                            color = colorArray[3];
//                            break;
//                    }
//                    //设置省的颜色
//                    provinces.get(i).setDrawColor(color);
//                    postInvalidate();//刷新
//
//                }
//
//            }
//        }
//    };

//    private void cc() {
        private Thread loadThread = new Thread() {
            @Override
            public void run() {
                InputStream inputStream = context.getResources().openRawResource(R.raw.chinahigh);
                try {
                    //取得 DocumentBuilderFactory 实例
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    //从 factory 获取 DocumentBuilder 实例
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    //解析输入流,得到 Document 实例
                    Document doc = builder.parse(inputStream);
                    Element rootElement = doc.getDocumentElement();
                    NodeList items = rootElement.getElementsByTagName("path");

                    for (int i = 0; i < items.getLength(); i++) {

                        Element element = (Element) items.item(i);
                        String pathData = element.getAttribute("android:pathData");
                        Path path = PathParser.createPathFromPathData(pathData);
                        provinces.add(new Province(path));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        };

//        private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFB0D7F8};
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (provinces == null) {
                    return;
                }
                for (int i = 0; i < provinces.size(); i++) {
                    int color;
                    int flag = i % 4;
                    switch (flag) {
                        case 1:
                            color = colorArray[0];
                            break;
                        case 2:
                            color = colorArray[1];
                            break;
                        case 3:
                            color = colorArray[2];
                            break;
                        default:
                            color = colorArray[3];
                            break;
                    }
                    provinces.get(i).setDrawColor(color);
                }
                postInvalidate();
            }
        };
//    }
}
