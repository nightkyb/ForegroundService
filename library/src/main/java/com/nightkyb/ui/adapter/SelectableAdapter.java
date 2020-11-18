package com.nightkyb.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.nightkyb.ui.adapter.listener.OnItemClickListener;
import com.nightkyb.ui.adapter.listener.OnItemLongClickListener;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 支持单选或多选的[RecyclerView.Adapter]基类。
 *
 * @author nightkyb
 */
public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder, T> extends
        RecyclerView.Adapter<VH> {
    private Context context;
    private final List<T> itemList = new ArrayList<>();
    private final SparseBooleanArray selectedPositions = new SparseBooleanArray();
    @SelectType
    private int selectType = SelectType.SINGLE;
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    @Retention(SOURCE)
    @Target({FIELD, PARAMETER, METHOD})
    @IntDef({SelectType.SINGLE, SelectType.MULTIPLE})
    public @interface SelectType {
        int SINGLE = 100; // 单选
        int MULTIPLE = 200; // 多选
    }

    @Override
    @NonNull
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(getLayout(), parent, false);
        return createViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(
                            holder.itemView,
                            itemList.get(adapterPosition),
                            adapterPosition
                    );
                }
            });
        }

        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    return onItemLongClickListener.onItemLongClick(
                            holder.itemView,
                            itemList.get(adapterPosition),
                            adapterPosition
                    );
                } else {
                    return false;
                }
            });
        }

        bind(holder, itemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @LayoutRes
    public abstract int getLayout();

    @NonNull
    public abstract VH createViewHolder(@NonNull View itemView);

    public abstract void bind(@NonNull VH holder, @NonNull T item, int position);

    public Context getContext() {
        return context;
    }

    @SelectType
    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(@SelectType int selectType) {
        this.selectType = selectType;
    }

    @NonNull
    public List<T> getItemList() {
        return itemList;
    }

    public void update(@Nullable List<T> itemList) {
        update(itemList, null);
    }

    public void update(@Nullable List<T> itemList, @Nullable SparseBooleanArray selectedPositions) {
        setSelectedPositions(selectedPositions);
        this.itemList.clear();

        if (itemList != null) {
            this.itemList.addAll(itemList);
        }

        notifyDataSetChanged();
    }

    public void clear() {
        setSelectedPositions(null);
        itemList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    public SparseBooleanArray getSelectedPositions() {
        return selectedPositions;
    }

    public void setSelectedPositions(@Nullable SparseBooleanArray selectedPositions) {
        this.selectedPositions.clear();

        if (selectedPositions == null) {
            return;
        }

        for (int i = 0; i < selectedPositions.size(); i++) {
            this.selectedPositions.append(selectedPositions.keyAt(i), selectedPositions.valueAt(i));
        }
    }

    @NonNull
    public List<T> getSelectedItems() {
        List<T> selectedItems = new ArrayList<>();

        for (int i = 0; i < selectedPositions.size(); i++) {
            int position = selectedPositions.keyAt(i);
            selectedItems.add(itemList.get(position));
        }

        return selectedItems;
    }

    public int getSelectedItemCount() {
        return selectedPositions.size();
    }

    public boolean hasSelectedItem() {
        return selectedPositions.size() > 0;
    }

    public void toggleSelect(int position) {
        selectItem(position, !isItemSelected(position));
    }

    public void selectItem(int position, boolean isSelected) {
        if (selectType == SelectType.SINGLE && hasSelectedItem()) { // 已选择项目
            int lastSelectedPosition = selectedPositions.keyAt(0);

            if (lastSelectedPosition != position) {
                selectedPositions.delete(lastSelectedPosition);
                notifyItemChanged(lastSelectedPosition);
            }
        }

        if (isSelected) {
            selectedPositions.append(position, true);
        } else {
            selectedPositions.delete(position);
        }

        notifyItemChanged(position);
    }

    public boolean isItemSelected(int position) {
        return selectedPositions.get(position);
    }

    public void selectAllItems(boolean isSelectAll) {
        if (isSelectAll) {
            if (selectType == SelectType.SINGLE) {
                return;
            }

            for (int i = 0; i < itemList.size(); i++) {
                selectedPositions.append(i, true);
            }
        } else {
            if (!hasSelectedItem()) {
                return;
            }

            selectedPositions.clear();
        }

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(@Nullable OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
