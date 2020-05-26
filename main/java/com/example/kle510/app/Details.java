package com.example.kle510.app;

import java.util.List;

class Details {

    private String id;
    private String name;
    private DetailsPicture picture;
    private DetailsAlbums albums;
    private DetailsPosts posts;

    public Details() {
    }

    // Getters and setters
    public DetailsPicture getPicture() {
        return picture;
    }

    public DetailsAlbums getAlbums() {
        return albums;
    }

    public DetailsPosts getPosts() {
        return posts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        // String pictureString = ResponsePictureData.toString();

        return "id: " + id + ", name: " + name;
    }

}

class DetailsPicture {
    private DetailsPictureData data;

    public DetailsPicture() {
    }

    public DetailsPictureData getData() {
        return data;
    }
}

class DetailsPictureData {

    private int height;
    private int width;
    private String is_silhouette;
    private String url;

    public DetailsPictureData() {
    }

    // Getters and setters
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getIs_silhouette() {
        return is_silhouette;
    }

    public void setIs_silhouette(String is_silhouette) {
        this.is_silhouette = is_silhouette;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

class DetailsAlbums {
    private List<DetailsAlbumsData> data;

    public DetailsAlbums() {
    }

    public List<DetailsAlbumsData> getData() {
        return data;
    }
}

class DetailsAlbumsData {
    private String name;
    private String id;
    private DetailsAlbumsDataPhotos photos;

    public DetailsAlbumsData() {
    }

    // Getters and setters
    public DetailsAlbumsDataPhotos getPhotos() {
        return photos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class DetailsAlbumsDataPhotos {
    private List<DetailsAlbumsDataPhotosData> data;

    public DetailsAlbumsDataPhotos() {
    }

    public List<DetailsAlbumsDataPhotosData> getData() {
        return data;
    }
}

class DetailsAlbumsDataPhotosData {
    private String name;
    private String id;
    private String picture;

    public DetailsAlbumsDataPhotosData() {
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // picture URL
    public String getPicture() {
        return picture;
    }

    public void setPicture(String url) {
        this.picture = picture;
    }
}

class DetailsPosts {
    private List<DetailsPostsData> data;

    public DetailsPosts() {
    }

    public List<DetailsPostsData> getData() {
        return data;
    }
}

class DetailsPostsData {
    private String created_time;
    private String message;
    private String id;

    public DetailsPostsData() {
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
