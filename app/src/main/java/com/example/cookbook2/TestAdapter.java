package com.example.cookbook2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private Context ct;
    private List<String> list;
    private List<String> url;

    public TestAdapter(Context ct, List<String> list, List<String> url) {
        this.ct = ct;
        this.list = list;
        this.url = url;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.end_row, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TestViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView wdw;
        private AppCompatTextView description;
        private ImageView iamgeView;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.ingredient_amount);
            this.wdw =  itemView.findViewById(R.id.ingredient_amount);
            this.description = itemView.findViewById(R.id.ingredient_amount);
            this.iamgeView =  itemView.findViewById(R.id.ingredient_amount);
        }

    }
}
