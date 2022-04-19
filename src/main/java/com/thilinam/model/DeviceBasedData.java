package com.thilinam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@Data
@EqualsAndHashCode
@ToString
public class DeviceBasedData {
    private String deviceId;
    private String timeStamp;
    private List<SensorBasedData> sensorData;
}
