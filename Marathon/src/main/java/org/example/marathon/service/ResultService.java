package org.example.marathon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.marathon.domain.po.Player;
import org.example.marathon.domain.po.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResultService extends IService<Result> {
    Boolean addResults(List<Result> results);
}
