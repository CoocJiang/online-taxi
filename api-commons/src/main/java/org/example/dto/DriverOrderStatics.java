package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverOrderStatics implements Serializable {

    private Long id;

    private Long driverId;

    private LocalDateTime grab_order_date;

    private Long grabOrderSuccessCount;
}
