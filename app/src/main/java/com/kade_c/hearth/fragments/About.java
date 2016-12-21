package com.kade_c.hearth.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kade_c.hearth.R;

/**
 * Handles the About tab that displays information about me.
 * Sets up onClick Listeners for links to social media.
 */
public class About extends Fragment {

    View view;

    final private String linkedinURL = "https://www.linkedin.com/in/christopher-kade-696501a8";
    final private String githubURL = "https://github.com/christopherkade";
    final private String facebookURL = "https://www.facebook.com/Christopher.Kade";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.about, container, false);

        setListeners();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("About");
    }

    /**
     * Sets social media link listeners.
     */
    private void setListeners() {
        final ImageView linkedinImg = (ImageView)view.findViewById(R.id.linkdinImg);
        linkedinImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(linkedinURL));
                startActivity(intent);
            }
        });

        ImageView githubImg = (ImageView)view.findViewById(R.id.githubImg);
        githubImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(githubURL));
                startActivity(intent);
            }
        });

        ImageView facebookImg = (ImageView)view.findViewById(R.id.facebookImg);
        facebookImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(facebookURL));
                startActivity(intent);
            }
        });
    }
}
