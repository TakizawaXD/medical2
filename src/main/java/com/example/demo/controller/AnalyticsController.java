package com.example.demo.controller;

import com.example.demo.data.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class AnalyticsController {

    @FXML
    private PieChart genderPieChart;
    @FXML
    private PieChart bloodTypePieChart;
    @FXML
    private BarChart<String, Number> ageBarChart;

    private final Database database = new Database();

    @FXML
    private void initialize() {
        loadGenderData();
        loadBloodTypeData();
        loadAgeData();
    }

    private void loadGenderData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Integer> genderDistribution = database.getPatientCountByGender();
        for (Map.Entry<String, Integer> entry : genderDistribution.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        genderPieChart.setData(pieChartData);
    }

    private void loadBloodTypeData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Integer> bloodTypeDistribution = database.getPatientCountByBloodType();
        for (Map.Entry<String, Integer> entry : bloodTypeDistribution.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        bloodTypePieChart.setData(pieChartData);
    }

    private void loadAgeData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<String, Integer> ageDistribution = database.getPatientCountByAgeRange();
        for (Map.Entry<String, Integer> entry : ageDistribution.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        ageBarChart.getData().add(series);
    }
}
