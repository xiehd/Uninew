package com.uninew.maintanence.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uninew.maintanence.interfaces.IGpsInfoPresenter;
import com.uninew.maintanence.interfaces.IGpsInfoView;
import com.uninew.maintanence.presenter.GpsPresenter;
import com.uninew.settings.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;


/**
 * Created by Administrator on 2017/8/30.
 */


public class GPSFragment extends Fragment implements IGpsInfoView, ColumnChartOnValueSelectListener {
    private View view;
    private IGpsInfoPresenter mIGpsInfoPresenter;
    private static Context mContext;
    //界面相关
    private TextView gps_location_state, gps_location_longt, gps_location_lat, gps_location_allNumber, gps_location_BDNumber,
            gps_location_sateNumber, gps_location_gpsNumber, gps_location_Modestate;
    private lecho.lib.hellocharts.view.ColumnChartView gps_chart;
    private ColumnChartData data;
    public static List<String> dataX = new ArrayList<>();

    private static List<Integer> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gps, container, false);
        mContext = this.getActivity();
        initView();
        mIGpsInfoPresenter = new GpsPresenter(mContext, this);
        SetListener();
        SetChart();
        return view;
    }

    private void initView() {
        gps_location_Modestate = (TextView) view.findViewById(R.id.gps_location_Modestate);
        gps_location_state = (TextView) view.findViewById(R.id.gps_location_state);
        gps_location_longt = (TextView) view.findViewById(R.id.gps_location_longt);
        gps_location_lat = (TextView) view.findViewById(R.id.gps_location_lat);
        gps_location_gpsNumber = (TextView) view.findViewById(R.id.gps_location_gpsNumber);
        gps_location_BDNumber = (TextView) view.findViewById(R.id.gps_location_BDNumber);
        gps_location_sateNumber = (TextView) view.findViewById(R.id.gps_location_sateNumber);
        gps_location_allNumber = (TextView) view.findViewById(R.id.gps_location_allNumber);
        gps_location_Modestate = (TextView) view.findViewById(R.id.gps_location_Modestate);

        gps_chart = (lecho.lib.hellocharts.view.ColumnChartView) view.findViewById(R.id.gps_chart);
        gps_chart.setZoomEnabled(true);//禁止手势缩放
    }

    private void SetListener() {
        // mIGpsInfoPresenter.registerGpsInfoListener();
    }

    @Override
    public void ShowLocationSate(int type, String state) {
        gps_location_Modestate.setText(mContext.getResources().getString(R.string.mangae_normal));
        if (type == 0) {
            gps_location_state.setText(state);
        } else {

        }
    }

    @Override
    public void ShowLocationInfo(double longitude, double latitude, int sateNumber, int userNumber, int gpsNUmber, int bdNumber) {
        gps_location_longt.setText(longitude + "");
        gps_location_lat.setText(latitude + "");
        gps_location_sateNumber.setText(sateNumber + "");
        gps_location_allNumber.setText(userNumber + "");
        gps_location_gpsNumber.setText(gpsNUmber + "");
        gps_location_BDNumber.setText(bdNumber + "");
    }

    @Override
    public void ShowLocationgSignals(Map<Integer, Integer> signals) {
        if (list != null && signals != null && signals.size() > 0) {
            try {
                list.clear();
                dataX.clear();
                Iterator<Map.Entry<Integer, Integer>> it = signals.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, Integer> entry = it.next();
                     Log.v("GPSInfo","key= " + entry.getKey() + " and value= " + entry.getValue());
                    if (entry.getValue() > 0) {
                        dataX.add(String.valueOf(entry.getKey()));
                        list.add(entry.getValue());
                    }
                }
                while (dataX.size() < 10) {
                    dataX.add("0");
                    list.add(0);
                }
                SetChart();
            } catch (Exception e) {
                Log.e("GPSFragment", "ShowLocationgSignals error" + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIGpsInfoPresenter.unRegisterGpsInfoListener();
    }

    private void SetChart() {
        setFirstChart();
    }

    private void setFirstChart() {
        // 使用的 7列，每列1个subcolumn。
        int numSubcolumns = 1;
        int numColumns = dataX.size();
        //定义一个圆柱对象集合
        List<Column> columns = new ArrayList<Column>();
        //子列数据集合
        List<SubcolumnValue> values;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //遍历列数numColumns
        for (int i = 0; i < numColumns; i++) {
            values = new ArrayList<SubcolumnValue>();
            //遍历每一列的每一个子列
            for (int j = 0; j < numSubcolumns; j++) {
                //为每一柱图添加颜色和数值
                if (list != null && list.size() > i) {
                    float f = list.get(i);
                    values.add(new SubcolumnValue(f, getColor(f)));
                }
            }
            //创建Column对象
            Column column = new Column(values);
            //这一步是能让圆柱标注数据显示带小数的重要一步 让我找了好久问题
            //作者回答https://github.com/lecho/hellocharts-android/issues/185

//            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
//            column.setFormatter(chartValueFormatter);

            //是否有数据标注
            if (list.get(i) != 0) {
                column.setHasLabels(true);
                //是否是点击圆柱才显示数据标注
                column.setHasLabelsOnlyForSelected(false);
            } else {
                column.setHasLabels(false);
            }
            columns.add(column);
            //给x轴坐标设置描述
            if (list.get(i) != 0) {
                axisValues.add(new AxisValue(i).setLabel(dataX.get(i)));
            }
        }
        //创建一个带有之前圆柱对象column集合的ColumnChartData
        data = new ColumnChartData(columns);

        //定义x轴y轴相应参数
        Axis axisX = new Axis().setHasLines(false);
        Axis axisY = new Axis().setHasLines(false);
        // axisY.setName("轴名称");//轴名称
        // axisY.hasLines();//是否显示网格线
        //  axisY.setMaxLabelChars(2);

        //        axisY.setTextColor( Color.parseColor("#686868"));//颜色
        //        List<AxisValue> valuesY = new ArrayList<>();
        //        valuesY.add(new AxisValue(0).setLabel("0"));
        //        valuesY.add(new AxisValue(1).setLabel("30"));
        //        valuesY.add(new AxisValue(2).setLabel("60"));
        //        axisY.setValues(valuesY);
        //       axisY.setAutoGenerated(true);

        axisX.hasLines();
        axisX.setTextColor(Color.parseColor("#686868"));
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(1);
        axisX.setInside(false);
        axisX.setValues(axisValues);

        //把X轴Y轴数据设置到ColumnChartData 对象中
        //data.setAxisXTop(axisX);  //x 轴在顶部
        data.setAxisXBottom(axisX);
        data.setFillRatio(0.8f);//柱子宽度，（0-1）
        data.setValueLabelBackgroundEnabled(true);
        data.setValueLabelBackgroundAuto(false);
        data.setValueLabelBackgroundColor(Color.parseColor("#00000000"));
        data.setValueLabelTextSize(8);
        // data.setAxisYLeft(axisY);
        //给表填充数据，显示出来
        gps_chart.setColumnChartData(data);
    }

    @Override
    public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {

    }

    @Override
    public void onValueDeselected() {

    }

    private int getColor(float number) {
        int color;
        color = Color.parseColor("#FF8C00");
        if (number < 10) {
            color = Color.parseColor("#FF8C00");
        } else if (number < 20) {
            color = Color.parseColor("#FF8C00");
        } else if (number < 35) {
            color = Color.parseColor("#FF8C00");
        } else {
            color = Color.parseColor("#3CB371");
        }
        return color;
    }
}
