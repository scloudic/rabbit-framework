package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.dataaccess.KeyGenerator;

import java.util.ArrayList;
import java.util.List;

public class CustomerBatchInsert extends CustomerInsert {
    @Override
    public boolean isBatchUpdate() {
        return true;
    }

    public List<KeyGenerator> getKeyGenerators() {
        return new ArrayList<KeyGenerator>();
    }
}
