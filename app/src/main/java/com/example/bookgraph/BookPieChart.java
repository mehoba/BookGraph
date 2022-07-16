package com.example.bookgraph;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

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
import java.util.stream.Collectors;

public class BookPieChart extends AppCompatActivity {

    List<DataEntry> bookCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pie_chart);

        bookCategories = new ArrayList<>();
        ArrayList<String> categoriesReceived = getIntent().getStringArrayListExtra("data");
        categoriesReceived.add("Computers");
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

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Apples", 6371664));
        data.add(new ValueDataEntry("Pears", 789622));
        data.add(new ValueDataEntry("Bananas", 7216301));
        data.add(new ValueDataEntry("Grapes", 1486621));
        data.add(new ValueDataEntry("Oranges", 1200000));

        pie.setData(bookCategories);
        anyChartView.setChart(pie);

    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> categories) {

        Set<String> setWithoutDuplicates = new LinkedHashSet<>(categories);

        return new ArrayList<>(setWithoutDuplicates);
    }
}