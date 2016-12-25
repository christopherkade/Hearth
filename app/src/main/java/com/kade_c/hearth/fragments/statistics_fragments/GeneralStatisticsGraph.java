package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kade_c.hearth.R;
import com.kade_c.hearth.Statistics;

/**
 * Should display various graphics of user deck / game progressions.
 */
// TODO: Work on applying stats to graph.
public class GeneralStatisticsGraph extends Fragment {

    View view;

    private Statistics statistics;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_general_graph, container, false);

        statistics = new Statistics(getContext(), getActivity());

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Statistics");
    }
}
