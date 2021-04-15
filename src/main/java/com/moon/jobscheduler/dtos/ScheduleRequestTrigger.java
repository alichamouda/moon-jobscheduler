package com.moon.jobscheduler.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class ScheduleRequestTrigger {

    private String cronTrigger;
    private String dateTrigger;
}
