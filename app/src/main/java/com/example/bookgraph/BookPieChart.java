package com.example.bookgraph;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BookPieChart extends AppCompatActivity {

    List<DataEntry> bookCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pie_chart);

        bookCategories = new ArrayList<>();
        ArrayList<String> categoriesReceived = getIntent().getStringArrayListExtra("data");
        ArrayList<String> categoriesWithoutDuplicates = removeDuplicates(categoriesReceived);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(BookPieChart.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        for(String bookCategory : categoriesWithoutDuplicates){
            int numberOfBooks = Collections.frequency(categoriesReceived, bookCategory);
            bookCategories.add(new ValueDataEntry(bookCategory, numberOfBooks));
        }

        pie.setData(bookCategories);
        anyChartView.setChart(pie);

    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> categories) {

        Set<String> setWithoutDuplicates = new LinkedHashSet<>(categories);

        return new ArrayList<>(setWithoutDuplicates);
    }
}