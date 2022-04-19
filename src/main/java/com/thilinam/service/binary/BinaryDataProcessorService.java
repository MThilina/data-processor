package com.thilinam.service.binary;

import com.thilinam.model.DeviceBasedData;
import com.thilinam.model.SensorBasedData;
import com.thilinam.service.DataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     This service layer convert the binary data from IOT device and convert it into POJO and provide it to outbound target
 *     This can further enhance to work with non binary json based IOT device input as well
 *
 *     Binary device input format :- device_id::sensor_id1@sensor_reading|sensor_id2@sensor_reading1,sensor_reading2::timestamp
 *     it consist with
 *     (1) device_id
 *     (2) sensor details (sensor_id with related reading or readings)
 *     (3) timestamp
 * </p>
 */
public class BinaryDataProcessorService implements DataProcessor {

    private static Logger logger = LoggerFactory.getLogger(BinaryDataProcessorService.class);

    /**
     * <p>
     *     This method converts the byte stream which is send by the device to a String
     * </p>
     * @param sensorData
     * @return
     */
    @Override
    public String convertData(byte[] sensorData) {
        String convertedIOTData = new String(sensorData, StandardCharsets.UTF_8);
        return convertedIOTData;
    }

    /**
     * <p>
     *     This method generate the pojo object {@link DeviceBasedData} which needed to be provided to the streams processor
     * </p>
     * @param data
     * @return
     */
    @Override
    public DeviceBasedData manipulateData(String data) {

        String[] splitData = data.split("::");
        // 1 . validate message format
        if(splitData.length!=3){
            logger.error("error : IOT device message format is incorrect please check the device output");
        }

        // 2. collect data to create the pojo
        String deviceId = splitData[0].trim(); // preformat data format
        String sensorBasedData = splitData[1].trim(); // preformat data format
        String timeStamp = splitData[2].trim(); // preformat data format
        List<SensorBasedData> sensorBasedDataList = this.convertSensorData(sensorBasedData);

        // 3. creating the pojo object
        DeviceBasedData deviceBasedData = new DeviceBasedData();
        deviceBasedData.setDeviceId(deviceId);
        deviceBasedData.setTimeStamp(timeStamp);
        deviceBasedData.setSensorData(sensorBasedDataList);

        return deviceBasedData;
    }

    /***************************************** Private Method ****************************************************/

    /**
     * <p>
     *     This method read out the sensor data for each sensor and list them up for the {@link DeviceBasedData} object
     * </p>
     * @param sensorBasedData
     * @return
     */
    private List<SensorBasedData> convertSensorData(String sensorBasedData){
        List<SensorBasedData> sensorBasedDataList = new ArrayList<>(); // This list contains sensor_id with readings

        String[] sensorWithData = sensorBasedData.split("\\|");

        for (String sensorWithDatum : sensorWithData) {
            SensorBasedData iotData = new SensorBasedData();
            List<String> sensorDataList = new ArrayList<>();

            String sensorId = sensorWithDatum.split("@")[0]; // fetch sensor id
            String data = sensorWithDatum.split("@")[1]; // fetch sensor readings

            sensorDataList.add(data); // adding single sensor data and multiple with comma separated. It will be separated when data model analysis going on

            // creating of sensor based object
            iotData.setSensorId(sensorId);
            iotData.setSensorData(sensorDataList);
            // adding sensor based object to a list to include in device based object
            sensorBasedDataList.add(iotData);
        }
        return sensorBasedDataList;
    }
}
