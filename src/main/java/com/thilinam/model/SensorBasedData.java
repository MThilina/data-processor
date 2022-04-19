package com.thilinam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode
@ToString
public class SensorBasedData {
    private String sensorId;
    private List<String> sensorData;
}
