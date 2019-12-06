package com.gll;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author gll
 * 2019/12/6 13:26
 */
public class MyOrderAlgorithm implements PreciseShardingAlgorithm<Integer> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        if(preciseShardingValue.getValue()>2000){
            return preciseShardingValue.getLogicTableName()+"_1";
        }else{
            return preciseShardingValue.getLogicTableName()+"_0";
        }
    }
}
