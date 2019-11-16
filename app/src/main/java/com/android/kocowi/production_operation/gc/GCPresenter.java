package com.android.kocowi.production_operation.gc;

import android.view.View;

import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.model.GC;

import java.util.ArrayList;

public class GCPresenter implements GCContract.Presenter {

    private ArrayList<GC> gcList = new ArrayList<>();
    private final GCContract.View mView;
    private final GcRepository gcRepository;

    public GCPresenter(GCContract.View view, GcRepository gcRepository) {
        mView = view;
        this.gcRepository = gcRepository;
        mView.setPresenter(this);
    }

    @Override
    public void bindGcList(ArrayList<GC> gcList) {
        this.gcList = gcList;
    }

    @Override
    public void onBindGcItemAtPosition(GCListAdapter.GCViewHolder holder, int position) {
        GC gc = gcList.get(position);
        holder.setGcCode(gc.getCode());
        holder.setGcFiledHeader(gc.getFiledHeader());

    }

    @Override
    public int getGcListSize() {
        return gcList.size();
    }

    @Override
    public void showGcActionsPopupMenu(View v) {
        mView.showGcActionsPopupMenu(v);
    }

    @Override
    public void retrieveGcs(GcRepository.GcRetrievingCallback callback) {
        gcRepository.retrieveGcs(callback);
    }

    @Override
    public void start() {

    }
}
