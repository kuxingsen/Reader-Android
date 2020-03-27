package com.monk.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.monk.reader.R;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.bean.Range;
import com.monk.reader.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class RangeAdapter extends RecyclerView.Adapter<RangeAdapter.ViewHolder> {


    private List<Range> rangeList;
    private FragmentManager childFragmentManager;

    public RangeAdapter(List<Range> rangeList, FragmentManager childFragmentManager) {

        this.rangeList = rangeList;
        this.childFragmentManager = childFragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_range, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Range range = rangeList.get(position);
        holder.tvRange.setText(range.getName());

        RangeFragmentPagerAdapter rangeFragmentPagerAdapter = new RangeFragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        rangeFragmentPagerAdapter.setRangeId(range.getId());
        holder.vpRange.setId(position+1);
        holder.vpRange.setAdapter(rangeFragmentPagerAdapter);
        holder.tlRange.setupWithViewPager(holder.vpRange);
        TabLayout.Tab tab = holder.tlRange.getTabAt(0);
        tab.setText("周榜");
        tab = holder.tlRange.getTabAt(1);
        tab.setText("月榜");
        tab = holder.tlRange.getTabAt(2);
        tab.setText("总榜");
    }

    @Override
    public int getItemCount() {
        return rangeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_range)
        TextView tvRange;
        @BindView(R.id.tl_range)
        TabLayout tlRange;
        @BindView(R.id.vp_range)
        ViewPager vpRange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
