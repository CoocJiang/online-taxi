package org.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.dto.PassengerUser;

@Mapper
public interface PassengerMapper extends BaseMapper<PassengerUser> {

}
