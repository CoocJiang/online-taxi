package org.example.service;


import org.example.dto.ResponseResult;
import org.example.remote.TrackClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {
    @Autowired
    TrackClient trackClient;

    public ResponseResult addtrack(String tid){

        return trackClient.addtrack(tid);
    }

}
