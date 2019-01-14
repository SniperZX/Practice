package zhr.com.practice;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    private LineChartView mLineChart;

    private final static String[] mLabels = {"ANT", "GNU", "OWL", "APE", "COD","YAK", "RAM", "JAY"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLineChart = (LineChartView) findViewById(R.id.lineChart);

        LineSet data;
        mLineChart.reset();

        int nSets = 2;  //两条线
        int nPoints = 5;  //每条线5个点
        for(int i = 0; i < nSets; i++){

            data = new LineSet();
            for(int j = 0; j < nPoints; j++)
                data.addPoint(new Point(mLabels[j], DataRetriever.randValue(0, 10)));

            data.setDots(DataRetriever.randBoolean())
                    .setDotsColor(Color.parseColor(DataRetriever.getColor(DataRetriever.randNumber(0,2))))
                    .setDotsRadius(DataRetriever.randDimen(4,7))
                    .setLineThickness(DataRetriever.randDimen(3,8))
                    .setLineColor(Color.parseColor(DataRetriever.getColor(i)))
                    .setDashed(DataRetriever.randBoolean())
                    .setSmooth(DataRetriever.randBoolean())
            ;

            if(i == 2){
                //data.setFill(Color.parseColor("#3388c6c3"));
                int[] colors = {Color.parseColor("#3388c6c3"), Color.TRANSPARENT};
                data.setGradientFill(colors, null);
            }

            if(DataRetriever.randBoolean())
                data.setDotsStrokeThickness(DataRetriever.randDimen(1,4))
                        .setDotsStrokeColor(Color.parseColor(DataRetriever.getColor(DataRetriever.randNumber(0,2))))
                        ;

            mLineChart.addData(data);
        }

        mLineChart.setGrid(DataRetriever.randPaint())
                .setVerticalGrid(DataRetriever.randPaint())
                .setHorizontalGrid(DataRetriever.randPaint())
                //.setThresholdLine(2, randPaint())
                .setYLabels(YController.LabelPosition.NONE)
                .setYAxis(false)
                .setXLabels(DataRetriever.getXPosition())
                .setXAxis(DataRetriever.randBoolean())
                .setMaxAxisValue(10, 2)
                .animate(DataRetriever.randAnimation(null, nPoints))
        //.show()
        ;


    }
}
