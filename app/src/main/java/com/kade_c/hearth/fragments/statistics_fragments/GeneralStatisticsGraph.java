package com.kade_c.hearth.fragments.statistics_fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kade_c.hearth.InternalFilesManager;
import com.kade_c.hearth.R;
import com.kade_c.hearth.Statistics;

import java.util.HashMap;

/**
 * Sets up and displays our victory graphs.
 */
public class GeneralStatisticsGraph extends Fragment {

    View view;

    // Contains our number of victories per class.
    private HashMap<String, Integer> winPerClass;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_general_graph, container, false);

        // Retrieve statistics.
        Statistics statistics = new Statistics(getContext(), getActivity());
        winPerClass = statistics.getWinPerClass();

        // Sets up our graphics.
        setGraphs();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Statistics");
    }

    /**
     * Calls our 3 graph-setup methods.
     */
    private void setGraphs() {
        setMageHunterPaladin();
        setWarriorDruidWarlock();
        setShamanPriestRogue();
    }

    /**
     * Sets up our first graphic.
     */
    private void setMageHunterPaladin() {
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        int VMage = winPerClass.get("Mage");
        int VHunter = winPerClass.get("Hunter");
        int VPaladin = winPerClass.get("Paladin");

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, VMage),
                new DataPoint(1, VHunter),
                new DataPoint(2, VPaladin),
        });
        graph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        graph.setTitle("Victories per class");
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        gridLabelRenderer.setNumHorizontalLabels(3);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Mage", "Hunter", "Paladin"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);
    }

    /**
     * Sets up our second graphic.
     */
    private void setWarriorDruidWarlock() {
        GraphView graph = (GraphView) view.findViewById(R.id.graph2);
        int VWarrior = winPerClass.get("Warrior");
        int VDruid = winPerClass.get("Druid");
        int VWarlock = winPerClass.get("Warlock");

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, VWarrior),
                new DataPoint(1, VDruid),
                new DataPoint(2, VWarlock),
        });
        graph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        gridLabelRenderer.setNumHorizontalLabels(3);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Warrior", "Druid", "Warlock"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);
    }

    /**
     * Sets up our third graphic.
     */
    private void setShamanPriestRogue() {
        GraphView graph = (GraphView) view.findViewById(R.id.graph3);
        int VShaman = winPerClass.get("Shaman");
        int VPriest = winPerClass.get("Priest");
        int VRogue = winPerClass.get("Rogue");

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, VShaman),
                new DataPoint(1, VPriest),
                new DataPoint(2, VRogue),
        });
        graph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        gridLabelRenderer.setNumHorizontalLabels(3);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Shaman", "Priest", "Rogue"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);
    }
}
