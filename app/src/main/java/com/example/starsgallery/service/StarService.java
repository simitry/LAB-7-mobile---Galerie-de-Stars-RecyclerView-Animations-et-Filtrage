package com.example.starsgallery.service;

import com.example.starsgallery.beans.Star;
import com.example.starsgallery.dao.IDao;
import com.example.starsgallery.R;

import java.util.ArrayList;
import java.util.List;

/*
 * SERVICE SINGLETON
 * -----------------
 * In a real application this layer could talk to SQLite, Room, Retrofit, or Firebase.
 * For this TP, keeping the data in memory is enough and makes the MVC separation visible.
 */
public class StarService implements IDao<Star> {

    private static StarService instance;

    private final List<Star> stars;

    private StarService() {
        stars = new ArrayList<>();
        seed();
    }

    public static StarService getInstance() {
        if (instance == null) {
            instance = new StarService();
        }

        return instance;
    }

    private void seed() {
        /*
         * The portraits are stored locally in drawable-nodpi.
         * Result: no more red star placeholders in the list when the emulator network is slow.
         */
        stars.add(new Star("Tom Cruise", R.drawable.tom_cruise, 4.2f));
        stars.add(new Star("Scarlett Johansson", R.drawable.scarlett_johansson, 4.7f));
        stars.add(new Star("Leonardo DiCaprio", R.drawable.leonardo_dicaprio, 4.8f));
        stars.add(new Star("Emma Watson", R.drawable.emma_watson, 4.5f));
        stars.add(new Star("Charlie Kirk", R.drawable.charlie_kirk, 2.8f));
        stars.add(new Star("Zendaya", R.drawable.zendaya, 4.6f));
        stars.add(new Star("Keanu Reeves", R.drawable.keanu_reeves, 4.9f));
        stars.add(new Star("Dwayne Johnson", R.drawable.dwayne_johnson, 4.1f));
        stars.add(new Star("Will Smith", R.drawable.will_smith, 3.9f));
        stars.add(new Star("Mohamed El Khiyari", R.drawable.mohamed_el_khiyari, 4.0f));
        stars.add(new Star("Mohamed Daoudi", R.drawable.mohamed_daoudi, 4.2f));
        stars.add(new Star("P. Diddy", R.drawable.p_diddy, 2.0f));

        // Requested order: Epstein stays at the very bottom of the full list.
        stars.add(new Star("Jeffrey Epstein", R.drawable.jeffrey_epstein, 1.0f));
    }

    @Override
    public boolean create(Star object) {
        return stars.add(object);
    }

    @Override
    public boolean update(Star object) {
        for (Star savedStar : stars) {
            if (savedStar.getId() == object.getId()) {
                savedStar.setName(object.getName());
                savedStar.setImg(object.getImg());
                savedStar.setRating(object.getRating());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(Star object) {
        return stars.remove(object);
    }

    @Override
    public Star findById(int id) {
        for (Star savedStar : stars) {
            if (savedStar.getId() == id) {
                return savedStar;
            }
        }

        return null;
    }

    @Override
    public List<Star> findAll() {
        return stars;
    }
}
