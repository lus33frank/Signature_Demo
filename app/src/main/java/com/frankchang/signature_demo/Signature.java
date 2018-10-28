package com.frankchang.signature_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;


public class Signature extends View {

    private static final float PEN_WIDTH = 3;
    private final Paint pen;
    private final LinkedList<LinkedList<HashMap<String, Float>>> lines;
    private final LinkedList<LinkedList<HashMap<String, Float>>> reLine;
    private boolean isStatus;
    private LinkedList<HashMap<String, Float>> line;


    public Signature(Context context) {
        super(context);

        // 建立畫筆
        pen = new Paint();
        pen.setColor(Color.BLUE);
        pen.setStrokeWidth(PEN_WIDTH);

        // 建立"線"物件
        lines = new LinkedList<>();
        reLine = new LinkedList<>();

        isStatus = false;
    }

    // 繪圖
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 設定背景顏色
        canvas.drawColor(Color.WHITE);  // 白色

        // 畫線
        for (LinkedList<HashMap<String, Float>> line : lines) {
            if (line.size() > 1) {
                for (int i = 1; i < line.size(); i++) {
                    HashMap<String, Float> point1 = line.get(i - 1);    // 上一坐標點
                    HashMap<String, Float> point2 = line.get(i);        // 目前坐標點
                    // 繪製
                    canvas.drawLine(point1.get("X"), point1.get("Y"),
                                    point2.get("X"), point2.get("Y"), pen);
                }
            }
        }
    }

    // 手勢動作
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 手提起來手勢
            isStatus = false;

        } else {
            // 手放下和滑動手勢
            if (!isStatus) {
                line = new LinkedList<>();
                lines.add(line);
                isStatus = true;
            }

            // 建立點的坐標物件
            HashMap<String, Float> point = new HashMap<>();
            point.put("X", event.getX());   // X軸
            point.put("Y", event.getY());   // Y軸
            line.add(point);        // 放到線物件中
            postInvalidate();       // 呼叫繪圖
        }

        return true;
    }

    // 上一步
    public void actionUndo() {
        if (lines.size() > 0) {
            reLine.add(lines.removeLast());
            postInvalidate();
        }
    }

    // 下一步
    public void actionRedo() {
        if (reLine.size() > 0) {
            lines.add(reLine.removeLast());
            postInvalidate();
        }
    }

    // 清除畫面
    public void actionClear() {
        lines.clear();
        reLine.clear();
        postInvalidate();
    }

    // 變更筆色
    public void actioncolor() {
        pen.setColor(Color.RED);
    }

}
