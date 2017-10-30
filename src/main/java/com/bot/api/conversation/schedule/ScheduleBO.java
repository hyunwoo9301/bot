package com.bot.api.conversation.schedule;

import com.bot.api.core.Common;
import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.common.Meal;
import com.bot.api.model.common.Schedule;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;

@Service
public class ScheduleBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        Message message = new Message();
        Keyboard keyboard = new Keyboard();
        String text = "";

        HttpHeaders headers = new HttpHeaders();;
        HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
        Schedule[] schedules = new RestTemplate().postForObject("http://"+ Common.konkuk_server_ip+":"+Common.konkuk_server_port+"/konkuk/api/schedule.json", httpEntity, Schedule[].class);

        HashSet<String> dateSet = new HashSet<String>();
        if(userMapper.get(userKey).getEntityMap().containsKey("날짜")) {
            for(Value value : userMapper.get(userKey).getEntityMap().get("날짜")) {
                dateSet.add(value.getValue());
            }
        }

        int i = 0;
        for(int month = 1 ; month <= 12 ; month++) {
            if(dateSet.contains(month+"월")) {
                i++;
                for(Schedule schedule : schedules) {
                    if(schedule.getMON_SCH().equals(month+"월")) {
                        text += schedule.getDAY_SCH() + schedule.getBODY_PREIVIEW() + "\n";
                    }
                }
            }
        }

        if(i==0) {
            for(Schedule schedule : schedules) {
                text += schedule.getDAY_SCH() + schedule.getBODY_PREIVIEW() + "\n";
            }
        }

        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message, null);
    }
}