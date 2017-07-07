package id.dekz.bakingapp.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.model.Step;

/**
 * Created by DEKZ on 7/3/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepVH> {

    private List<Step> data = new ArrayList<>();
    private OnStepClick onStepClick;
    private int rowIndex = -1;
    private int previousPos, nextPos;

    public interface OnStepClick{
        void onStepClicked(Step step,
                           int stepNumber,
                           int totalSteps,
                           int previousStepID,
                           int nextStepID);
    }

    public void addClickListener(OnStepClick onStepClick){
        this.onStepClick = onStepClick;
    }

    public StepAdapter (){
    }

    public void replaceAll(List<Step> data){
        this.data.clear();
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public StepVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_step, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final StepVH holder, final int position) {
        String idStep = String.valueOf(data.get(position).getId());
        String descStep = data.get(position).getShortDescription();
        holder.tvStep.setText(idStep+". "+descStep);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.getAdapterPosition() == 0){
                    previousPos = 0;
                    //nextPos = holder.getAdapterPosition()+1;
                }else{
                    previousPos = holder.getAdapterPosition()-1;
                    //nextPos = holder.getAdapterPosition()+1;
                }

                if(holder.getAdapterPosition() == data.size()-1){
                    nextPos = holder.getAdapterPosition();
                    //previousPos = holder.getAdapterPosition()-1;
                }else{
                    nextPos = holder.getAdapterPosition()+1;
                    //previousPos = holder.getAdapterPosition()-1;
                }

                rowIndex = position;
                onStepClick.onStepClicked(
                        data.get(holder.getAdapterPosition()),
                        data.get(holder.getAdapterPosition()).getId(),
                        data.get(data.size()-1).getId(),
                        data.get(previousPos).getId(),
                        data.get(nextPos).getId()
                );
                notifyDataSetChanged();
            }
        });

        if(rowIndex==position){
            holder.tvStep.setSelected(true);
        }else{
            holder.tvStep.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class StepVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step)TextView tvStep;

        public StepVH(View itemView) {
            super(itemView);
            ButterKnife.bind(StepVH.this, itemView);
        }
    }
}
