package com.ep.weare.post.dto;

import com.ep.weare.post.entity.Announcement;

public interface AnnounceWithUserInterface {

    Announcement getAnnouncement();
    String getUserName();


}
