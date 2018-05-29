package com.xytsz.xytaj.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/28.
 *
 * 人生啊
 *
 */
public class FacilityCategroyFragment extends BaseFragment {


    @Bind(R.id.facility_categroy_rv)
    RecyclerView facilityCategroyRv;
    private static final String ARG_PARAM1 = "param1";
    private int id;

    public FacilityCategroyFragment() {

    }

    public static FacilityCategroyFragment newInstance(int id){
        FacilityCategroyFragment fragment = new FacilityCategroyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, id);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            id = getArguments().getInt(ARG_PARAM1);
        }
    }


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_facilitycategroy, null);

        return view;
    }

    @Override
    public void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        facilityCategroyRv.setLayoutManager(manager);
        getData();



    }

    private void getData() {
        //服务器获取数据  通过iD获取数据

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
