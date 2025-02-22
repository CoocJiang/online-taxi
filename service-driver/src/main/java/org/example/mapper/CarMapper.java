package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.dto.Car;


@Mapper
public interface CarMapper extends BaseMapper<Car> {

}
