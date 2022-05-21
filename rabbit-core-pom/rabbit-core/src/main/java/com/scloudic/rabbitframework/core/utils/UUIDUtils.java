package com.scloudic.rabbitframework.core.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtils {
    private static final Logger logger = LoggerFactory.getLogger(UUIDUtils.class);
    public static String DEFAULT_UUID36 = "00000000-0000-0000-0000-000000000000";
    public static String DEFAULT_UUID32 = "00000000000000000000000000000000";
    public static final UUID ZERO_UUID = new UUID(0, 0);
    private static NoArgGenerator timeGenerator;
    private static NoArgGenerator randomGenerator;

    static {
        ensureGeneratorInitialized();
    }

    private static void ensureGeneratorInitialized() {
        if (timeGenerator == null) {
            synchronized (UUIDUtils.class) {
                if (timeGenerator == null) {
                    timeGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
                }
            }
        }
        if (randomGenerator == null) {
            synchronized (com.fasterxml.uuid.UUIDGenerator.class) {
                if (randomGenerator == null) {
                    randomGenerator = Generators.randomBasedGenerator();
                }
            }
        }
    }

    public static UUID getRandomUUID() {
        return randomGenerator.generate();
    }

    public static String getRandomUUID36() {
        return getRandomUUID().toString();
    }

    public static String getRandomUUID32() {
        return getUUID32(getRandomUUID36());
    }

    public static UUID getTimeUUID() {
        return timeGenerator.generate();
    }

    public static String getTimeUUID36() {
        return getTimeUUID().toString();
    }

    public static String getTimeUUID32() {
        return getUUID32(getTimeUUID36());
    }

    private static String getUUID32(String generator) {
        return generator.replace("-", "");
    }

    public static UUID uuid(ByteBuffer bb) {
        if (bb == null) {
            return null;
        }
        if (bb.remaining() < 16) {
            return null;
        }
        bb = bb.slice();
        return new UUID(bb.getLong(), bb.getLong());
    }

    public static UUID uuid(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ZERO_UUID;
    }
}
