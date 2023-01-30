package com.xdclass.search.dao;

import com.xdclass.search.model.NBAPlayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NBAPlayerDao {

    @Select("select * from nba_player")
    public List<NBAPlayer> selectAll();
}
