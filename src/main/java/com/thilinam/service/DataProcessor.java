package com.thilinam.service;

import com.thilinam.model.DeviceBasedData;

import java.io.Serializable;

/**
 * <P>
 *     This the service layer which is implemented for the  data processing from the IOT devices
 *     This contains both BINARY and JSON related data processing
 * </P>
 */
public interface DataProcessor extends Serializable {
    String convertData(byte[] sensorData);
    DeviceBasedData manipulateData(String data);
}
