package com.model.movies;

public class MovieModel {
    private int id;

    public MovieModel(int id, String title, String description, String releaseYear, String genres, Double userRatings) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.userRatings = userRatings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String title;
    private String description;
    private String releaseYear;
    private String genres;
    private Double userRatings;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Double getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(Double userRatings) {
        this.userRatings = this.userRatings + userRatings / 2;
    }

    public void print(){
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Release Year: " + releaseYear);
        System.out.println("Genres: " + genres);
        System.out.println("User Ratings: " + userRatings);
    }


}
