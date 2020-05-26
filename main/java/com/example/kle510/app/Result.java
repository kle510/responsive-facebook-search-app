package com.example.kle510.app;


class Result {
    private String id;
    private String name;
    private ResultPicture picture;

    public Result() {
    }
    //Getters and setters
    public ResultPicture getPicture(){
        return picture;
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
        //String pictureString = ResponsePictureData.toString();

        return "id: " + id + ", name: " + name;
    }

}

class ResultPicture{
    private ResultPictureData data;
    public ResultPicture() {
    }
    public  ResultPictureData getData(){
        return data;
        
    }
}

class ResultPictureData{

    private int height;
    private int width;
    private String is_silhouette;
    private String url;

    public  ResultPictureData() {
    }
    //Getters and setters
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

    public String toString() {
        return "height: " + height + ", width: " + width + ", is_silhouette: " + is_silhouette + ", url: " + url;
    }

}