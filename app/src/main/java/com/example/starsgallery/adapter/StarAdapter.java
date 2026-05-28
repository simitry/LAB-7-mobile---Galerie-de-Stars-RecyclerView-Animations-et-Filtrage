package com.example.starsgallery.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starsgallery.R;
import com.example.starsgallery.beans.Star;
import com.example.starsgallery.service.StarService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * ADAPTER + VIEWHOLDER + FILTER
 * -----------------------------
 * This class is intentionally very commented for the TP.
 * RecyclerView asks the adapter three big questions:
 *
 * 1. How do I create a row?        -> onCreateViewHolder()
 * 2. How do I fill a row?          -> onBindViewHolder()
 * 3. How many rows do I display?   -> getItemCount()
 *
 * The SearchView asks one more question:
 * 4. How do I filter the rows?     -> getFilter()
 */
public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {

    private final Context context;
    private final List<Star> completeStarList;

    // This list is the one currently visible on screen.
    private List<Star> visibleStarList;

    private final Filter nameFilter;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;

        // We copy the input list so filtering does not destroy the original data set.
        this.completeStarList = new ArrayList<>(stars);
        this.visibleStarList = new ArrayList<>(stars);

        this.nameFilter = new FilterByBeginningOfName();
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.star_item, parent, false);
        StarViewHolder holder = new StarViewHolder(row);

        /*
         * Item click behavior:
         * The user touches a star row, then a custom popup appears.
         * The popup edits only the rating, because that is the requested interaction.
         */
        holder.itemView.setOnClickListener(clickedView -> showRatingPopup(holder));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        Star star = visibleStarList.get(position);

        holder.id.setText(String.valueOf(star.getId()));
        holder.name.setText(star.getName().toUpperCase(Locale.getDefault()));
        holder.rating.setRating(star.getRating());

        /*
         * The portrait is now a local drawable resource.
         * Directly setting it avoids showing the red star placeholder in recycled rows.
         */
        holder.image.setImageResource(star.getImg());
    }

    @Override
    public int getItemCount() {
        return visibleStarList.size();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private void showRatingPopup(StarViewHolder holder) {
        int adapterPosition = holder.getBindingAdapterPosition();

        // RecyclerView can briefly report NO_POSITION during animations; ignore that tiny window.
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        Star selectedStar = visibleStarList.get(adapterPosition);

        View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);

        TextView popupId = popup.findViewById(R.id.popupStarId);
        TextView popupName = popup.findViewById(R.id.popupStarName);
        CircleImageView popupImage = popup.findViewById(R.id.popupStarImage);
        RatingBar popupRating = popup.findViewById(R.id.popupRatingBar);

        popupId.setText(String.valueOf(selectedStar.getId()));
        popupName.setText(selectedStar.getName());
        popupRating.setRating(selectedStar.getRating());

        popupImage.setImageResource(selectedStar.getImg());

        new AlertDialog.Builder(context)
                .setTitle("Notez :")
                .setMessage("Donner une note entre 1 et 5 :")
                .setView(popup)
                .setPositiveButton("Valider", (dialog, which) -> {
                    float newRating = popupRating.getRating();
                    int starId = Integer.parseInt(popupId.getText().toString());

                    Star starToUpdate = StarService.getInstance().findById(starId);
                    if (starToUpdate != null) {
                        starToUpdate.setRating(newRating);
                        StarService.getInstance().update(starToUpdate);

                        /*
                         * The object in visibleStarList points to the same Star instance.
                         * notifyItemChanged() is enough: no full list refresh is needed here.
                         */
                        notifyItemChanged(holder.getBindingAdapterPosition());
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    public static class StarViewHolder extends RecyclerView.ViewHolder {

        final TextView id;
        final TextView name;
        final CircleImageView image;
        final RatingBar rating;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.tvId);
            image = itemView.findViewById(R.id.imgStar);
            name = itemView.findViewById(R.id.tvName);
            rating = itemView.findViewById(R.id.rating);
        }
    }

    private class FilterByBeginningOfName extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence query) {
            List<Star> filteredStars = new ArrayList<>();

            if (query == null || query.length() == 0) {
                filteredStars.addAll(completeStarList);
            } else {
                String cleanQuery = query.toString().toLowerCase(Locale.getDefault()).trim();

                for (Star star : completeStarList) {
                    String cleanName = star.getName().toLowerCase(Locale.getDefault());

                    // startsWith() matches the TP requirement: search by name beginning.
                    if (cleanName.startsWith(cleanQuery)) {
                        filteredStars.add(star);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredStars;
            results.count = filteredStars.size();
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence query, FilterResults results) {
            visibleStarList = (List<Star>) results.values;

            // Filtering changes the whole visible collection, so notifyDataSetChanged() is correct here.
            notifyDataSetChanged();
        }
    }
}
