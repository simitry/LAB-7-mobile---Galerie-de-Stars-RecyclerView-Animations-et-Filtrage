package com.example.starsgallery.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starsgallery.R;
import com.example.starsgallery.adapter.StarAdapter;
import com.example.starsgallery.service.StarService;
import com.google.android.material.appbar.MaterialToolbar;

/*
 * MAIN LIST SCREEN
 * ----------------
 * This Activity owns the toolbar menu and the RecyclerView.
 * It does not own the data itself; StarService owns the data.
 */
public class ListActivity extends AppCompatActivity {

    private StarAdapter starAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        MaterialToolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        setTitle("Stars");

        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        /*
         * LinearLayoutManager means:
         * one vertical column, one item after another, like the TP screenshots.
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        starAdapter = new StarAdapter(this, StarService.getInstance().findAll());
        recyclerView.setAdapter(starAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint("Rechercher par nom");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // The list is already filtered while typing; no extra submit work is needed.
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (starAdapter != null) {
                        starAdapter.getFilter().filter(newText);
                    }

                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            shareApplication();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareApplication() {
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_title))
                .setText(getString(R.string.share_text))
                .startChooser();
    }
}
