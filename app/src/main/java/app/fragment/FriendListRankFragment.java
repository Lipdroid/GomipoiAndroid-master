package app.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import app.adapter.FriendListRankAdapter;
import app.animation.button.ButtonAnimationManager;
import app.application.GBApplication;
import app.data.http.GomipoiFriendsRankResponse;
import app.define.FragmentEventCode;
import app.define.SeData;
import app.interfaces.FriendListRankListener;
import common.fragment.GBFragmentBase;
import lib.fragment.OnFragmentListener;
import lib.log.DebugLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendListRankFragment extends GBFragmentBase {


    // if set to true the currentView is set to weekly ranking, by default it is true.
    private boolean isViewingCurrentRank;

    FriendListRankAdapter adapter;

    RelativeLayout mContainer;
    RecyclerView rvFriendListRank;

    View btnBack;
    Button mButtonConfirmedRanking;
    Button mButtonCurrentRanking;

    TextView tvSelfRank;
    TextView tvSelfPoint;
    TextView tvDateTime;

    public FriendListRankFragment() {
        // Required empty public constructor
    }

    public static FriendListRankFragment newInstance() {
        FriendListRankFragment fragment = new FriendListRankFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list_rank, container, false);

        mContainer = view.findViewById(R.id.container);
        rvFriendListRank = view.findViewById(R.id.rv_friendRankList);

        btnBack = view.findViewById(R.id.back_button);
        mButtonConfirmedRanking = view.findViewById(R.id.button_confirm_ranking);
        mButtonCurrentRanking = view.findViewById(R.id.button_current_ranking);

        tvSelfRank = view.findViewById(R.id.tv_self_rank);
        tvSelfPoint = view.findViewById(R.id.tv_self_point);
        tvDateTime = view.findViewById(R.id.tv_date);

        rvFriendListRank.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnBack.setBackground(null);
        } else {
            btnBack.setBackgroundDrawable(null);
        }

        if (btnBack != null) {
            new ButtonAnimationManager(btnBack, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GBApplication app = getApp();
                    if (app != null) {
                        app.getSeManager().playSe(SeData.YES);
                        // if (getFragmentManager() != null)
                        //     getFragmentManager().popBackStack();
                        FriendListFragment fragment = FriendListFragment.newInstance();
                        FriendListRankListener mListener = (FriendListRankListener) getActivity();
                        mListener.replaceFragment(fragment);
                    }
                }
            });
        }

        if (mButtonConfirmedRanking != null) {
            mButtonConfirmedRanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isViewingCurrentRank) return;
                    GomipoiFriendsRankResponse.API = "week_rankings/last";
                    isViewingCurrentRank = false;
                    getFriendListRankData();
                    DebugLog.i("FriendListRankFragment - btnSwitchRank clicked.");
                }
            });
        }

        if (mButtonCurrentRanking != null) {
            mButtonCurrentRanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isViewingCurrentRank) return;
                    GomipoiFriendsRankResponse.API = "week_rankings/current";
                    isViewingCurrentRank = true;
                    getFriendListRankData();
                    DebugLog.i("FriendListRankFragment - btnSwitchRank clicked.");
                }
            });

            mButtonCurrentRanking.performClick();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //getFriendListRankData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {

        if (btnBack != null) {
            btnBack.setOnClickListener(null);
        }

        if (mButtonCurrentRanking != null) {
            mButtonCurrentRanking.setOnClickListener(null);
        }

        if (mButtonConfirmedRanking != null) {
            mButtonConfirmedRanking.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_friend_list_rank);
    }

    private void updateView(String executedAt) {
        DebugLog.i("FriendListRankFragment - Date: " + executedAt);
        DateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

        String displayDate = "";
        try {
            DebugLog.i("FriendListRankFragment - Server Time: " + executedAt);
            dateParse.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateParse.parse(executedAt);

            sdf.setTimeZone(TimeZone.getDefault());
            displayDate = sdf.format(date);
            DebugLog.i("FriendListRankFragment - Local Time: " + displayDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvDateTime.setText("");
        if (isViewingCurrentRank) {
            mButtonCurrentRanking.setBackgroundResource(R.drawable.drawable_tab_selected);
            mButtonConfirmedRanking.setBackgroundResource(R.drawable.drawable_tab_normal);

            if (displayDate.length() <= 0) return;
            tvDateTime.setText(String.format(getString(R.string.friend_list_rank_date_time), displayDate ));
        } else {
            mButtonCurrentRanking.setBackgroundResource(R.drawable.drawable_tab_normal);
            mButtonConfirmedRanking.setBackgroundResource(R.drawable.drawable_tab_selected);

            if (displayDate.length() < 10) return;
            tvDateTime.setText(displayDate.concat("確定"));
        }
    }

    public final void onReceivedFriendListRankData(String executedAt, int selfId, int selfRank, int selfPoint,
                                                   List<GomipoiFriendsRankResponse> listData) {
        // for (listData)
        DebugLog.i("FriendListRankFragment - Self ID: " + selfId);
        DebugLog.i("FriendListRankFragment - ListData: " + listData.toString());
        if (selfRank > 0) {
            tvSelfRank.setText(String.format(getString(R.string.friend_list_rank_rank), selfRank));
        } else {
            tvSelfRank.setText("ランキング対象外");
        }
        tvSelfPoint.setText(String.format(getString(R.string.friend_list_rank_points), selfPoint));

        int longestString = 0;

        DebugLog.i("FriendListRankFragment - listData Size: "+ listData.size());

        // Get the longest string in points
        for (GomipoiFriendsRankResponse data : listData) {
            String tmpPoint = String.valueOf(data.getPoint());
            if (longestString < tmpPoint.length()) {
                longestString = tmpPoint.length();
            }
        }

        // update the data of the points
        for (GomipoiFriendsRankResponse data : listData) {
            String tmpPoint = String.valueOf(data.getPoint());
            int tmpLength = longestString - tmpPoint.length();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < tmpLength; i++) {
                builder.append("0");
            }
            data.setExtraText(builder.toString());
        }

        Collections.sort(listData);

        adapter = new FriendListRankAdapter(getActivity(), listData);
        rvFriendListRank.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateView(executedAt);
    }

    private void getFriendListRankData() {
        tvDateTime.setText("");
        onFragmentEvent(FragmentEventCode.GetFriendListRank, null);
    }
}
