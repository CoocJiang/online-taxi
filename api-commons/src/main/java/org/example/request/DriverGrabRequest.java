package org.example.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DriverGrabRequest {

    private Long orderId;

    private String receiveOrderCarLongitude;

    private String receiveOrderCarLatitude;

    private Long carId;

    private Long driverId;

    private String licenseId;

    private String vehicleNo;

    private String vehicleType;

    private String driverPhone;
}
