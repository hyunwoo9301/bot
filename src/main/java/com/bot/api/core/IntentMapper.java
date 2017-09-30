package com.bot.api.core;

import com.bot.api.conversation.profile.NoneBO;
import com.bot.api.conversation.profile.PartyBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class IntentMapper {
    private Map<String, Conversable> intentMap;

    @Autowired
    private NoneBO noneBO;

    @Autowired
    private PartyBO partyBO;

    @PostConstruct
    public void init(){
        intentMap = new HashMap<String, Conversable>();
        intentMap.put("None", noneBO);
        intentMap.put("프로필조회", partyBO);
    }

    public Conversable getIntent(String key) {
        return intentMap.get(key);
    }
}