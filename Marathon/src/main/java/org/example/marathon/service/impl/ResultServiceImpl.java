package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.marathon.domain.po.Result;
import org.example.marathon.mapper.ResultMapper;
import org.example.marathon.service.ResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl extends ServiceImpl<ResultMapper, Result> implements ResultService {
    @Override
    public Boolean addResults(List<Result> results) {
        // 使用 MyBatis-Plus 提供的 saveBatch 方法批量插入
        return this.saveBatch(results);
    }
}
