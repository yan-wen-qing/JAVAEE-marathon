package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.example.marathon.domain.po.Event;
import org.example.marathon.domain.po.Participate;
import org.example.marathon.domain.po.Player;
import org.example.marathon.domain.po.Result;
import org.example.marathon.mapper.EventMapper;
import org.example.marathon.mapper.ParticipateMapper;
import org.example.marathon.mapper.PlayerMapper;
import org.example.marathon.mapper.ResultMapper;
import org.example.marathon.service.ParticipateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Service
public class ParticipateServiceImpl extends ServiceImpl<ParticipateMapper, Participate> implements ParticipateService {

    @Autowired
    private ParticipateMapper participateMapper;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ResultMapper resultMapper;

    @Override
    public List<Map<String, Object>> getParticipantsByEventId(Integer eventId) {
        // 查询该赛事的所有参赛记录
        List<Participate> participates = participateMapper.selectList(
                new QueryWrapper<Participate>().eq("event_id", eventId)
        );

        // 获取选手详细信息
        List<Map<String, Object>> participantDetails = new ArrayList<>();
        for (Participate participate : participates) {
            Player player = playerMapper.selectById(participate.getPlayerId());
            if (player != null) {
                // 将选手信息和参赛记录整合
                Map<String, Object> details = new HashMap<>();
                details.put("playerId", player.getId());
                details.put("name", player.getName());
                details.put("gender", player.getGender());
                details.put("age", player.getAge());
                details.put("role", participate.getRole());
                details.put("number", participate.getNumber());
                participantDetails.add(details);
            }
        }
        log.info("获取选手信息完成，赛事ID={}，选手数量={}", eventId, participantDetails.size());
        return participantDetails;
    }

    @Override
    public List<Map<String, Object>> getPlayerHistory(Integer playerId) {
        log.info("获取玩家历史成绩，playerId={}", playerId); // 添加日志
        List<Result> results = resultMapper.selectList(
                new QueryWrapper<Result>().eq("player_id", playerId)
        );

        log.info("查询到的历史成绩记录数量: {}", results.size());

        List<Map<String, Object>> playerHistory = new ArrayList<>();
        for (Result result : results) {
            Event event = eventMapper.selectById(result.getEventId());
            log.info("查询到的赛事: {}", event);

            if (event != null) {
                Map<String, Object> historyDetails = new HashMap<>();
                historyDetails.put("eventName", event.getName());
                historyDetails.put("eventDate", event.getEventDate());
                historyDetails.put("eventType", event.getCategory());
                historyDetails.put("netResult", result.getNetResult());
                historyDetails.put("ranking", result.getPlayerRank());
                playerHistory.add(historyDetails);
            }
        }

        log.info("返回玩家历史记录: {}", playerHistory.size());
        return playerHistory;
    }



    @Override
    @Transactional
    public void drawLots(Integer eventId, Integer totalParticipants) {
        // 检查赛事是否已经抽签
        Event event = eventMapper.selectById(eventId);
        if (event.getIsDrawn()) {
            throw new IllegalStateException("该赛事已抽签，请勿重复操作！");
        }

        // 查询普通跑者和慈善跑者
        List<Participate> normalParticipants = participateMapper.selectList(
                new QueryWrapper<Participate>()
                        .eq("event_id", eventId)
                        .ne("role", "charity") // role 不是 charity
        );

        List<Participate> charityParticipants = participateMapper.selectList(
                new QueryWrapper<Participate>().eq("event_id", eventId).eq("role", "charity")
        );

        // 随机选出普通跑者中签名单
        // 打乱 normalParticipants
        Collections.shuffle(normalParticipants, new Random());

        // 深拷贝子列表，避免共享引用问题
        List<Participate> selectedParticipants = new ArrayList<>(normalParticipants.subList(0, Math.min(totalParticipants, normalParticipants.size())));
        List<Participate> unselectedParticipants = new ArrayList<>(normalParticipants.subList(
                Math.min(totalParticipants, normalParticipants.size()), normalParticipants.size()));

        // 为中签普通跑者和慈善跑者分配唯一号码
        Set<String> usedNumbers = new HashSet<>();
        assignNumbers(selectedParticipants, usedNumbers);
        assignNumbers(charityParticipants, usedNumbers);

        // 更新普通跑者状态
        unselectedParticipants.forEach(p -> p.setRole("not_selected")); // 未中签选手状态设置为“未中签”

        // 批量更新数据库participate表
        for (Participate participant : selectedParticipants) {
            participateMapper.update(participant, new UpdateWrapper<Participate>()
                    .eq("player_id", participant.getPlayerId())
                    .eq("event_id", participant.getEventId()));
        }
        for (Participate participant : unselectedParticipants) {
            participateMapper.update(participant, new UpdateWrapper<Participate>()
                    .eq("player_id", participant.getPlayerId())
                    .eq("event_id", participant.getEventId()));
        }
        for (Participate participant : charityParticipants) {
            participateMapper.update(participant, new UpdateWrapper<Participate>()
                    .eq("player_id", participant.getPlayerId())
                    .eq("event_id", participant.getEventId()));
        }

        // 更新赛事状态为已抽签
        event.setIsDrawn(true);
        eventMapper.updateById(event);

        log.info("抽签完成：中签普通跑者数量={}, 未中签普通跑者数量={}, 慈善跑者数量={}",
                selectedParticipants.size(), unselectedParticipants.size(), charityParticipants.size());
    }


    /**
     * 分配唯一参赛号码
     */
    private void assignNumbers(List<Participate> participants, Set<String> usedNumbers) {
        if (participants == null || participants.isEmpty()) return;

        String[] letters = {"A", "B", "C", "D", "E"}; // 字母池

        for (Participate participant : participants) {
            String number;
            do {
                String letter = letters[new Random().nextInt(letters.length)]; // 随机选择字母
                number = letter + (1000 + new Random().nextInt(9000)); // 生成字母+数字的号码
            } while (usedNumbers.contains(number)); // 确保唯一性
            usedNumbers.add(number);
            participant.setNumber(number);
        }
    }

    @Override
    public void submitPlayers(Integer eventId, List<Integer> playerIds, String role, String chosenField){
        // 更新同一 eventId 下其他原本 role 为 'pacer' 的选手的 role 为 'normal'
        LambdaUpdateWrapper<Participate> normalUpdateWrapper = new LambdaUpdateWrapper<>();
        normalUpdateWrapper.eq(Participate::getEventId, eventId)
                .eq(Participate::getRole, role)
                .notIn(Participate::getPlayerId, playerIds)
                .set(Participate::getRole, "normal");
        participateMapper.update(null, normalUpdateWrapper);

        // 更新 event 表，设置 pacer_is_chosen 为 true
        Event event = eventMapper.selectById(eventId);
        if (event != null) {
            try {
                // 通过反射动态设置字段
                Field field = Event.class.getDeclaredField(chosenField);
                field.setAccessible(true);
                field.set(event, true);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 处理异常情况
                e.printStackTrace();
            }
            eventMapper.updateById(event);
        }
    }

}

