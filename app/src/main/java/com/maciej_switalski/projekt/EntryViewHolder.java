package com.maciej_switalski.projekt;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

    private static final int REMOVE_ANIMATION_DELAY = 300;
    private static final int REMOVE_ANIMATION_DURATION = 400;
    private final Handler animationHandler = new Handler();
    private final Runnable animationRunnable;
    private final Drawable originalBackground;
    private final EntryAdapter entryAdapter;
    EntryDbHelper dbHelper;
    TextView titleTextView;
    TextView dateTextView;
    TextView contentTextView;
    TextView categoryTextView;
    Entry entry; // Reference to the Entry object
    boolean animationPlayed = false;

    public EntryViewHolder(@NonNull View itemView, EntryAdapter adapter) {
        super(itemView);

        entryAdapter = adapter;
        titleTextView = itemView.findViewById(R.id.title_text_view);
        dateTextView = itemView.findViewById(R.id.date_text_view);
        contentTextView = itemView.findViewById(R.id.content_text_view);
        categoryTextView = itemView.findViewById(R.id.category_text_view);

        originalBackground = itemView.getBackground();

        dbHelper = new EntryDbHelper(itemView.getContext());

        animationRunnable = () -> {
            // Change background color of the item view
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                    itemView.getBackground(), // Existing background
                    new ColorDrawable(Color.RED) // Transparent overlay
            });
            // Set the layer drawable as the background
            itemView.setBackground(layerDrawable);
            ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofArgb(layerDrawable.getDrawable(1), "color", Color.TRANSPARENT, Color.RED);
            backgroundColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

            ObjectAnimator titleColorAnimator = ObjectAnimator.ofArgb(titleTextView, "textColor", Color.BLACK, Color.WHITE);
            titleColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

            ObjectAnimator contentColorAnimator = ObjectAnimator.ofArgb(contentTextView, "textColor", Color.BLACK, Color.WHITE);
            contentColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

            ObjectAnimator dateColorAnimator = ObjectAnimator.ofArgb(dateTextView, "textColor", Color.parseColor("#AAAAAA"), Color.WHITE);
            dateColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(backgroundColorAnimator, titleColorAnimator, contentColorAnimator, dateColorAnimator);
            animatorSet.start();
            animationPlayed = true;
        };

        itemView.setOnClickListener(this);
        itemView.setOnTouchListener(this);
    }

    // Method to bind the Entry object to the ViewHolder
    public void bind(Entry entry) {
        this.entry = entry;
        titleTextView.setText(entry.getTitle());
        dateTextView.setText(entry.getDate());
        contentTextView.setText(entry.getContent());
        categoryTextView.setText(entry.getCategory());
    }

    @Override
    public void onClick(View v) {
        // Perform action with the Entry object, for example:
        // Create an Intent to start the NoteActivity
        Intent intent = new Intent(itemView.getContext(), NoteActivity.class);

        // Pass data to the NoteActivity using Intent extras
        intent.putExtra("entry_title", entry.getTitle());
        intent.putExtra("entry_content", entry.getContent());
        intent.putExtra("entry_date", entry.getDate());
        intent.putExtra("id", entry.getId());
        intent.putExtra("category", entry.getCategory());

        // Start the NoteActivity
        itemView.getContext().startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animationHandler.postDelayed(animationRunnable, REMOVE_ANIMATION_DELAY);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                animationHandler.removeCallbacks(animationRunnable);
                if (!animationPlayed) {
                    v.performClick();
                    break;
                }
                showDeleteItemDialog();
            case MotionEvent.ACTION_CANCEL:
                animationHandler.removeCallbacks(animationRunnable);
                if (animationPlayed) {
                    animationPlayed = false;
                    LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                            originalBackground,
                            new ColorDrawable(Color.TRANSPARENT)
                    });
                    itemView.setBackground(layerDrawable);

                    ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofArgb(layerDrawable.getDrawable(1), "color", Color.RED, Color.TRANSPARENT);
                    backgroundColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

                    ObjectAnimator titleColorAnimator = ObjectAnimator.ofArgb(titleTextView, "textColor", Color.WHITE, Color.BLACK);
                    titleColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

                    ObjectAnimator contentColorAnimator = ObjectAnimator.ofArgb(contentTextView, "textColor", Color.WHITE, Color.BLACK);
                    contentColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

                    ObjectAnimator dateColorAnimator = ObjectAnimator.ofArgb(dateTextView, "textColor", Color.WHITE, Color.parseColor("#AAAAAA"));
                    dateColorAnimator.setDuration(REMOVE_ANIMATION_DURATION); // Adjust the duration as needed

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(backgroundColorAnimator, titleColorAnimator, contentColorAnimator, dateColorAnimator);
                    animatorSet.start();

                    titleTextView.setTextColor(Color.BLACK);
                    contentTextView.setTextColor(Color.BLACK);
                    dateTextView.setTextColor(Color.parseColor("#AAAAAA"));
                }
                break;
        }
        return true;
    }

    private void showDeleteItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Usuń wspomnienie")
                .setMessage("Czy jesteś pewien że chcesz usunąć to wspomnienie?")
                .setPositiveButton("Tak", (dialog, which) -> {
                    dbHelper.removeEntry(entry.getId());

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        entryAdapter.removeEntry(position);
                    }
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
