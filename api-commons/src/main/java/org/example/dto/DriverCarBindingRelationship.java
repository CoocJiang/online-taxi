package org.example.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverCarBindingRelationship implements Serializable {

    private Long id;


     //司机ID
    private Long driverId;

     //车辆ID
    private Long carId;


    //绑定状态：1：绑定，2：解绑
    private Integer bindState;


     //绑定时间
    private LocalDateTime bindingTime;


     // 解绑时间
    private LocalDateTime unBindingTime;

}
