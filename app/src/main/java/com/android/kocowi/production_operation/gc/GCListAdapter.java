package com.android.kocowi.production_operation.gc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.kocowi.R;
import com.android.kocowi.model.GC;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GCListAdapter extends RecyclerView.Adapter<GCListAdapter.GCViewHolder> {

    private final GCContract.Presenter presenter;

    public GCListAdapter(GCContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void bindGcList(ArrayList<GC> gcList) {
        presenter.bindGcList(gcList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gc_item_layout, parent, false);
        return new GCViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GCViewHolder holder, int position) {
        presenter.onBindGcItemAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getGcListSize();
    }

    class GCViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView gcCodeTextView, gcFiledHeaderTextView;
        private ImageButton moreImageButton;
        private Context context;

        public GCViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            gcCodeTextView = itemView.findViewById(R.id.gc_code_textview);
            gcFiledHeaderTextView = itemView.findViewById(R.id.gc_filedheader_textview);
            moreImageButton = itemView.findViewById(R.id.more_button);
            moreImageButton.setOnClickListener(this);
        }

        public void setGcCode(String code) {
            gcCodeTextView.setText(String.format("Code: %s", code));
        }

        public void setGcFiledHeader(String filedHeader) {
            gcFiledHeaderTextView.setText(String.format("Filed Header: %s", filedHeader));
        }

        @Override
        public void onClick(View v) {
            presenter.showGcActionsPopupMenu(v, getAdapterPosition());
        }
    }
}
