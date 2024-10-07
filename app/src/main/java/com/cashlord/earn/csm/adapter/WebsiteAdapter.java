package com.cashlord.earn.csm.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cashlord.earn.R;
import com.cashlord.earn.UpdateListener;
import com.cashlord.earn.csm.model.WebsiteModel;
import com.cashlord.earn.helper.ContextExtensions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class WebsiteAdapter extends RecyclerView.Adapter<WebsiteAdapter.ViewHolder> {
    private final List<WebsiteModel> mList;
    private final UpdateListener updateListener;
    private final String type;
    private final Context context;

    public WebsiteAdapter(List<WebsiteModel> mList, UpdateListener updateListener, String type, Context context) {
        this.mList = mList;
        this.updateListener = updateListener;
        this.type = type;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView visit_coin;
        public final TextView visit_desc;
        public final TextView visit_title;
        public final CardView visitCard;
        public final RoundedImageView visitLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            visit_coin = itemView.findViewById(R.id.visit_coin);
            visit_desc = itemView.findViewById(R.id.visit_desc);
            visit_title = itemView.findViewById(R.id.visit_title);
            visitCard = itemView.findViewById(R.id.visit);
            visitLogo = itemView.findViewById(R.id.visit1_logo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.website_visit_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WebsiteModel website = mList.get(position);
        holder.visit_title.setText(website.getVisitTitle());
        holder.visit_coin.setText(website.getVisitCoin());

        holder.visitCard.setOnClickListener(v -> updateListener.UpdateListener(
                website.getVisitCoin(),
                website.getVisitTimer(),
                website.getVisitLink(),
                website.getBrowser(),
                website.getId(),
                position,
                website.getDescription(),
                website.getLogo(),
                website.getPackages()
        ));

        switch (type.toLowerCase()) {
            case "website":
                holder.visit_desc.setText("Visit for " + website.getVisitTimer() + " minutes");
                holder.visitLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.visit));
                break;
            case "video":
                holder.visit_desc.setText("Watch for " + website.getVisitTimer() + " minutes");
                holder.visitLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.video));
                break;
            case "app":
                if (ContextExtensions.isAndroid13(context)) { // Đảm bảo hàm này hoạt động đúng
                    holder.visit_desc.setText(Html.fromHtml(website.getDescription(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    holder.visit_desc.setText(Html.fromHtml(website.getDescription()));
                }
                Glide.with(context)
                        .load(website.getLogo())
                        .placeholder(R.drawable.loading)
                        .into(holder.visitLogo);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
