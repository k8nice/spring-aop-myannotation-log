package com.nice.service;

import com.nice.domain.SystemLog;

public interface SystemLogService {
    Integer deleteSystemLog(Integer id);
    Integer insert(SystemLog record);
    Integer insertTest(SystemLog record);
    SystemLog selectSystemLog(Integer id);
    Integer updateSystemLog(SystemLog record);
}
