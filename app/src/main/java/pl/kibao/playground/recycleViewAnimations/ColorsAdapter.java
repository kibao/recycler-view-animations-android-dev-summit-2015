package pl.kibao.playground.recycleViewAnimations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorViewHolder> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> mColors = new ArrayList<>();

    private View.OnClickListener mItemAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    public ColorsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public ColorsAdapter(Context context, ArrayList<Integer> colors) {
        this(context);
        mColors.addAll(colors);
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = mLayoutInflater.inflate(R.layout.item_color, parent, false);
        container.setOnClickListener(mItemAction);

        return new ColorViewHolder(container);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        int color = mColors.get(position);
        holder.container.setBackgroundColor(color);
        holder.textView.setTextColor(ColorsHelper.getTextColor(color));
        holder.textView.setText("#" + Integer.toHexString(color));
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    public void setItemAction(View.OnClickListener itemAction) {
        mItemAction = itemAction;
    }

    public void addItem(int pos, int color) {
        mColors.add(pos, color);
        notifyItemInserted(pos);
    }

    public void changeItem(int pos, int color) {
        mColors.set(pos, color);
        notifyItemChanged(pos);
    }

    public void removeItem(int pos) {
        mColors.remove(pos);
        notifyItemRemoved(pos);
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout container;

        public ColorViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView;
            textView = (TextView) itemView.findViewById(R.id.color);
        }
    }
}
