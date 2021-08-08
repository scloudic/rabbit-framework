package com.scloudic.rabbitframework.core.uid;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.core.utils.UUIDUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Random;

/**
 * 订单编码生成类 ,主要采取snowflake生成
 * </p>
 * 根据{@link GeneratorType}匹配生成id
 */
public class UIdGeneratorImpl implements UIdGenerator {
    /**
     * 开始时间截 (2019-08-06)
     */
    private long twepoch;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long dataCenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long dataCenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;
    private static final Random RANDOM = new Random();
    private WorkerNum workerNum;

    private String dateFormat = "yyyyMMddHHMMssSSS";

    public UIdGeneratorImpl() {
        this.workerId = 1;
        this.dataCenterId = 0;
        this.twepoch = 1565020800000L;
    }

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public UIdGeneratorImpl(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        // 2019-10-26 00:13:21(中国标准时间)
        this.twepoch = 1572020001075L;
    }

    /**
     * 根据生成参数生成三种不同的ID,
     * GeneratorType说明：
     * <p/>
     * SNOWFLAKE： 标准算法，根据当前时间生成,不低于12位数
     * <p/>
     * TIMESTAMP:时间戳方法，此算法是17位时间+6位后缀形成,共23位
     * <p/>
     * UUIDHASHCODE:UUID的hashCode生成,共18位
     *
     * @param generatorType
     * @return
     */
    public synchronized String nextId(GeneratorType generatorType) {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                sequence = RANDOM.nextInt(4095);
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = RANDOM.nextInt(4095);
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        String id = "";
        long result = 0;
        long cDatacenterId = 1;
        switch (generatorType) {
            // 此算法不低于17位数
            case SNOWFLAKE:
                // 移位并通过或运算拼到一起组成64位的ID
                result = ((timestamp - twepoch) << timestampLeftShift) | (dataCenterId << datacenterIdShift)
                        | (workerId << workerIdShift) | sequence;
                id = result + "";
                break;
            // 此算法是17位时间+6位后缀形成,共21位
            case TIMESTAMP:
                String datePrefix = DateFormatUtils.format(timestamp, dateFormat);
                result = (cDatacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
                id = datePrefix + result;
                break;
            // 此算法是根据UUID的hashCode生成,共18位
            case UUIDHASHCODE:
                result = (cDatacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
                String uuidHashCode = getUUIDHashCode();
                String worker = "0" + workerId;
                if (workerId > 10) {
                    worker = workerId + "";
                }
                id = uuidHashCode + worker + result;
                break;
        }

        return id;
    }

    private String getUUIDHashCode() {
        int hashCodeV = UUIDUtils.getRandomUUID32().toString().hashCode();
        if (hashCodeV < 0) {// 有可能是负数
            hashCodeV = -hashCodeV;
        }
        return StringUtils.rightPad(String.format("%d", hashCodeV), 10, "0");
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * 动态获取workId
     *
     * @param workerNum
     */
    public void setWorkerNum(WorkerNum workerNum) {
        this.workerNum = workerNum;
        if (this.workerNum != null) {
            workerId = this.workerNum.getWorkerId();
        }
    }

    public static void main(String[] args) throws Exception {
        UIdGeneratorImpl idWorker = new UIdGeneratorImpl(1, 1);
        for (int i = 0; i < 5; i++) {
            String id = idWorker.nextId(GeneratorType.SNOWFLAKE);
            System.out.println(id + "," + id.length());
        }
    }
}