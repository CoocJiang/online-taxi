package org.example.service;

import org.example.dto.ResponseResult;
import org.example.request.DriverGrabRequest;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;


@Service
public interface GrabService {

    public ResponseResult grab(DriverGrabRequest driverGrabRequest);
}
