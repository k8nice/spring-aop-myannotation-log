package com.nice.service.impl;

import com.nice.domain.SystemLog;
import com.nice.mapper.SystemLogMapper;
import com.nice.service.SystemLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("systemLogService")
public class SystemLogServiceImpl implements SystemLogService {
   // @Resource
 //   private SystemLogMapper systemLogMapper;

    @Override
    public Integer deleteSystemLog(Integer id) {
    //    return systemLogMapper.deleteByPrimaryKey(id);
        System.out.println("id=" + id);
        return null;
    }

    @Override
    public Integer insert(SystemLog record) {
  //      return systemLogMapper.insertSelective(record);
        System.out.println("record=" + record);
        return null;
    }

    @Override
    public Integer insertTest(SystemLog record) {
   //     return systemLogMapper.insert(record);
        System.out.println("record=" + record);
        return null;
    }

    @Override
    public SystemLog selectSystemLog(Integer id) {
    //    return systemLogMapper.selectByPrimaryKey(id);
        System.out.println("id=" + id);
        return null;
    }

    @Override
    public Integer updateSystemLog(SystemLog record) {
    //    return systemLogMapper.updateByPrimaryKeySelective(record);
        System.out.println("record=" + record);
        return null;
    }
}
