package org.example.marathon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.marathon.domain.po.Result;

import java.util.List;

@Mapper
public interface TopPlayerMapper {

    /**
     * 查询前N名玩家信息，支持按性别筛选和排序
     */
    @Select({
            "<script>",
            "SELECT r.id, r.player_rank AS playerRank, r.gender_rank AS genderRank, ",
            "       r.net_result AS netResult, r.gun_result AS gunResult, ",
            "       r.player_id AS playerId, p.name, p.gender ",
            "FROM result r ",
            "JOIN player p ON r.player_id = p.id ",
            "WHERE r.event_id = #{eventId} ",
            "<if test='gender != null and gender != \"全部\"'>",
            "   AND p.gender = #{gender} ",
            "</if>",
            "ORDER BY ",
            "<choose>",
            "   <when test='sortOrder == \"asc\"'> r.player_rank ASC </when>",
            "   <when test='sortOrder == \"desc\"'> r.player_rank DESC </when>",
            "   <otherwise> r.player_rank ASC </otherwise>",
            "</choose>",
            "LIMIT #{topN}",
            "</script>"
    })
    List<Result> getTopPlayers(@Param("eventId") Integer eventId,
                               @Param("topN") Integer topN,
                               @Param("gender") String gender,
                               @Param("sortOrder") String sortOrder);
}
