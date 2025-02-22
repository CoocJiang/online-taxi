package org.example.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class DriverUserExistsResponse {

    private String driverPhone;

    private int ifExists;
}
