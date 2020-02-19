package com.example.simplesvg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class Province {

    private Path path;
    private  int drawColor;


    public Province( Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    void draw(Canvas canvas, Paint paint, boolean isSelect)
    {
        if(isSelect){
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8,0,0,0xffffff);
            canvas.drawPath(path,paint);

            paint.clearShadowLayer();
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(5);
            canvas.drawPath(path,paint);
        }else {
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFFD0E8F4);
            canvas.drawPath(path,paint);

        }
    }


    private static int  Mright=0, Mbotton=0;

    public boolean isSelect(int x, int y) {
        //构造一个区域对象
        RectF rectF = new RectF();
//        计算控制点的边界
        path.computeBounds(rectF,true);
        Region region = new Region();
        region.setPath(path, new Region((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom));

        if (rectF.right > Mright) {
            Mright = (int)rectF.right;
        }
        if (rectF.bottom > Mbotton) {
            Mbotton = (int)rectF.bottom;
        }
        System.out.println( Mright + " " + Mbotton);
        return region.contains(x,y);
    }
}
