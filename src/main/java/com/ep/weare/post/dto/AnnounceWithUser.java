package com.ep.weare.post.dto;

import com.ep.weare.post.entity.Announcement;
import lombok.Data;

@Data
public class AnnounceWithUser {

    private Announcement announcement;
    private String userName;
}
