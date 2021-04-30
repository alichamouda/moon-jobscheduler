package com.moon.jobscheduler.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ScheduleRequest {

//    public ScheduleRequest() {
//        this.triggers = new ArrayList<>();
//    }

    private String topicName;
    private String name;
    private String serviceName;
    private String key;
    private String message;
//    @JsonIgnore
    private List<ScheduleRequestTrigger> triggers;
}
