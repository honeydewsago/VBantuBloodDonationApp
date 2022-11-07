package com.example.vbantublooddonationapp.Model;

public class OrganiserImage {

    private String organiserID;
    private String url;

    public OrganiserImage() {
    }

    public OrganiserImage(String organiserID, String url) {
        this.organiserID = organiserID;
        this.url = url;
    }

    public String getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(String organiserID) {
        this.organiserID = organiserID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
