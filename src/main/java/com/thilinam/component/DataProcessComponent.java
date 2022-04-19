package com.thilinam.component;

import com.thilinam.constant.DataProcessorTypes;
import com.thilinam.model.DeviceBasedData;
import com.thilinam.service.DataProcessor;
import com.thilinam.service.binary.BinaryDataProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

public class DataProcessComponent {

    private static Logger logger = LoggerFactory.getLogger(DataProcessComponent.class);

    private static DataProcessComponent dataProcessComponent;
    private DataProcessor binaryDataService = new BinaryDataProcessorService(); // single instantiate of object
    DeviceBasedData deviceBasedData;

    private DataProcessComponent(){}

    public static DataProcessComponent getDataInstance(){
        if(Objects.isNull(dataProcessComponent)){
            dataProcessComponent = new DataProcessComponent();
        }
        return dataProcessComponent;
    }


    /**
     * <p>
     *     This method will process IOT data which are based on Binary Data
     * </p>
     * @param type
     * @param payload
     * @return
     */
    public DeviceBasedData processBinaryIOTData(DataProcessorTypes type, String payload){
        DataProcessor processor = this.getDataProcessor(type);
        if(Objects.isNull(processor)){
            logger.error("error: related data processor does not implemented, please check data input type ");
        }else{
            byte[] payloadBytes = reflectByteArrayFromString(payload);
            String dataset = processor.convertData(payloadBytes);
            deviceBasedData = processor.manipulateData(dataset);
        }
        return deviceBasedData;
    }


    /*********************************************** Private Methods *************************************************/

    /**
     * <p>
     *     This method returns a Data Processor depend on the {@link DataProcessorTypes} type.
     *     If not given default will be Binary if JSON selected hence it's not any implementation it will log the warn msg and return null
     *     Not null safe
     * </p>
     * @param type
     * @return DataProcessor
     */
    private DataProcessor getDataProcessor(DataProcessorTypes type){
        switch (type) {
            case JSON:
                // returns the json service { which is not implemented and this can be scalable to implementation }
                logger.warn("warn : JSON DataProcessor still not implemented");
                return null;
            case BINARY:
            default:
                return binaryDataService;

        }
    }

    /**
     * <p>
     *     This method reads the String data in the producer and convert the same value to Byte array
     * </p>
     * @param payload
     * @return
     */
    private byte[] reflectByteArrayFromString(String payload){
        byte[] bytes = new BigInteger(payload,16).toByteArray();
        if(bytes[0]==0){
            bytes = Arrays.copyOfRange(bytes,1,bytes.length);
        }
        return bytes;
    }
}
