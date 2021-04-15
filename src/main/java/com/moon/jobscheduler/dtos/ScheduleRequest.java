package com.moon.jobscheduler.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ScheduleRequest {

    private String topicName;
    private String name;
    private String serviceName;
    private String key;
    private String message;

    private List<ScheduleRequestTrigger> triggers;
}
