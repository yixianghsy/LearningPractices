package com.xdclass.search.controller;

import com.xdclass.search.model.NBAPlayer;
import com.xdclass.search.service.NBAPlayerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/nba")
public class NBASearchController {

    @Autowired
    private NBAPlayerService nbaPlayerService;

    @RequestMapping("/importAll")
    public String importAll(){
        try {
            nbaPlayerService.importAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping("/searchMatch")
    public List<NBAPlayer> searchMatch(@RequestParam(value = "displayNameEn", required = false) String displayNameEn) {
        try {
            return nbaPlayerService.searchMatch("displayNameEn",displayNameEn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/searchTerm")
    public List<NBAPlayer> searchTerm(@RequestParam(value = "country", required = false) String country,
                                      @RequestParam(value = "teamName", required = false) String teamName) {
        try {
            if(StringUtils.isNoneBlank(country))
                return nbaPlayerService.searchTerm("country",country);

            else if(StringUtils.isNoneBlank(teamName))
                return nbaPlayerService.searchTerm("teamName",teamName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/searchMatchPrefix")
    public List<NBAPlayer> searchMatchPrefix(@RequestParam(value = "displayNameEn", required = false) String displayNameEn) {
        try {
            return nbaPlayerService.searchMatchPrefix("displayNameEn",displayNameEn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
