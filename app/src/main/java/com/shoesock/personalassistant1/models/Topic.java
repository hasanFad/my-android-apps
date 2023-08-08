package com.shoesock.personalassistant1.models;

import java.io.Serializable;
import java.util.Date;

public class Topic implements Serializable {


    private String topicName, topicContent, relatedTopics, topicKeywords, userName;
    private Date topicDate;


    public String getUserName() {
        return userName;
    }

    public String getRelatedTopics() {
        return relatedTopics;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public String getTopicName() {
        return topicName;
    }

    public Date getTopicDate() {
        return topicDate;
    }

    public String getTopicKeywords() {
        return topicKeywords;
    }

    public void Topic(String userName, String topicName, String topicContent, String relatedTopics,
                        String topicKeywords, Date topicDate){

        this.relatedTopics = relatedTopics;
        this.topicKeywords = topicKeywords;
        this.topicContent = topicContent;
        this.topicName = topicName;
        this.topicDate = topicDate;
        this.userName = userName;


    } // close the Topic function

} // close the Topic class
