package org.example.service;


import org.example.dto.ResponseResult;
import org.example.remote.TerminalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class TerminalService {

    @Autowired
    TerminalClient terminalClient;

    public ResponseResult addterminal(String name,String desc){

        return terminalClient.addteminal(name,desc);
    }

    public ResponseResult aroundSearch(String center, String radius) {

        return terminalClient.aroundSearch(center,radius);
    }

    public ResponseResult trsearch(String tid, Long starttime, Long endtime) {

        return terminalClient.trsearch(tid, starttime, endtime);

    }
}
