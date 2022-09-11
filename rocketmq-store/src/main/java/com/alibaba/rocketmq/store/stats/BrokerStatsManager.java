/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.alibaba.rocketmq.store.stats;

import com.alibaba.rocketmq.common.ThreadFactoryImpl;
import com.alibaba.rocketmq.common.constant.LoggerName;
import com.alibaba.rocketmq.common.stats.MomentStatsItemSet;
import com.alibaba.rocketmq.common.stats.StatsItem;
import com.alibaba.rocketmq.common.stats.StatsItemSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class BrokerStatsManager {

    public enum StatsType {
        SEND_SUCCESS,
        SEND_FAILURE,
        SEND_BACK_SUCCESS,
        SEND_BACK_FAILURE,
        SEND_TIMER_SUCCESS,
        SEND_TRANSACTION_SUCCESS,
        RCV_SUCCESS,
        RCV_EPOLLS
    }

    private static final Logger log = LoggerFactory.getLogger(LoggerName.RocketmqStatsLoggerName);
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl(
        "BrokerStatsThread"));

    private static final Logger commercialLog = LoggerFactory.getLogger(LoggerName.CommercialLoggerName);
    private final ScheduledExecutorService commercialStatsExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl(
        "CommercialStatsThread"));

    public static final String TOPIC_PUT_NUMS = "TOPIC_PUT_NUMS";
    public static final String TOPIC_PUT_SIZE = "TOPIC_PUT_SIZE";
    public static final String GROUP_GET_NUMS = "GROUP_GET_NUMS";
    public static final String GROUP_GET_SIZE = "GROUP_GET_SIZE";
    public static final String SNDBCK_PUT_NUMS = "SNDBCK_PUT_NUMS";
    public static final String BROKER_PUT_NUMS = "BROKER_PUT_NUMS";
    public static final String BROKER_GET_NUMS = "BROKER_GET_NUMS";
    public static final String GROUP_GET_FROM_DISK_NUMS = "GROUP_GET_FROM_DISK_NUMS";
    public static final String GROUP_GET_FROM_DISK_SIZE = "GROUP_GET_FROM_DISK_SIZE";
    public static final String BROKER_GET_FROM_DISK_NUMS = "BROKER_GET_FROM_DISK_NUMS";
    public static final String BROKER_GET_FROM_DISK_SIZE = "BROKER_GET_FROM_DISK_SIZE";

    // For commercial
    public static final String COMMERCIAL_TOPIC_SEND_TIMES = "COMMERCIAL_TOPIC_SEND_TIMES";
    public static final String COMMERCIAL_GROUP_SNDBCK_TIMES = "COMMERCIAL_GROUP_SNDBCK_TIMES";
    public static final String COMMERCIAL_GROUP_RCV_TIMES = "COMMERCIAL_GROUP_RCV_TIMES";
    public static final String COMMERCIAL_GROUP_RCV_EPOLLS = "COMMERCIAL_GROUP_RCV_EPOLLS";

    public static final String COMMERCIAL_TOPIC_SEND_SIZE = "COMMERCIAL_TOPIC_SEND_SIZE";
    public static final String COMMERCIAL_GROUP_SNDBCK_SIZE = "COMMERCIAL_GROUP_SNDBCK_SIZE";
    public static final String COMMERCIAL_GROUP_RCV_SIZE = "COMMERCIAL_GROUP_RCV_SIZE";

    // Message Size limit for one api-calling count.
    public static final double SIZE_PER_COUNT = 64 * 1024;

    // Counting base when sending transaction message.
    public static final int TRANSACTION_COUNT_BASE = 100;

    // Counting base when sending timer message.
    public static final int TIMER_COUNT_BASE = 100;

    private final HashMap<String, StatsItemSet> statsTable = new HashMap<String, StatsItemSet>();
    private final String clusterName;

    public static final String GROUP_GET_FALL = "GROUP_GET_FALL";
    private final MomentStatsItemSet momentStatsItemSet = new MomentStatsItemSet(GROUP_GET_FALL, scheduledExecutorService, log);


    public BrokerStatsManager(String clusterName) {
        this.clusterName = clusterName;

        this.statsTable.put(TOPIC_PUT_NUMS, new StatsItemSet(TOPIC_PUT_NUMS, this.scheduledExecutorService, log));
        this.statsTable.put(TOPIC_PUT_SIZE, new StatsItemSet(TOPIC_PUT_SIZE, this.scheduledExecutorService, log));
        this.statsTable.put(GROUP_GET_NUMS, new StatsItemSet(GROUP_GET_NUMS, this.scheduledExecutorService, log));
        this.statsTable.put(GROUP_GET_SIZE, new StatsItemSet(GROUP_GET_SIZE, this.scheduledExecutorService, log));
        this.statsTable.put(SNDBCK_PUT_NUMS, new StatsItemSet(SNDBCK_PUT_NUMS, this.scheduledExecutorService, log));
        this.statsTable.put(BROKER_PUT_NUMS, new StatsItemSet(BROKER_PUT_NUMS, this.scheduledExecutorService, log));
        this.statsTable.put(BROKER_GET_NUMS, new StatsItemSet(BROKER_GET_NUMS, this.scheduledExecutorService, log));
        this.statsTable.put(GROUP_GET_FROM_DISK_NUMS, new StatsItemSet(GROUP_GET_FROM_DISK_NUMS, this.scheduledExecutorService, log));
        this.statsTable.put(GROUP_GET_FROM_DISK_SIZE, new StatsItemSet(GROUP_GET_FROM_DISK_SIZE, this.scheduledExecutorService, log));
        this.statsTable.put(BROKER_GET_FROM_DISK_NUMS, new StatsItemSet(BROKER_GET_FROM_DISK_NUMS, this.scheduledExecutorService, log));
        this.statsTable.put(BROKER_GET_FROM_DISK_SIZE, new StatsItemSet(BROKER_GET_FROM_DISK_SIZE, this.scheduledExecutorService, log));

        // For commercial
        this.statsTable.put(COMMERCIAL_TOPIC_SEND_TIMES, new StatsItemSet(COMMERCIAL_TOPIC_SEND_TIMES, this.commercialStatsExecutor,
            commercialLog));

        this.statsTable.put(COMMERCIAL_GROUP_RCV_TIMES, new StatsItemSet(COMMERCIAL_GROUP_RCV_TIMES, this.commercialStatsExecutor,
            commercialLog));

        this.statsTable.put(COMMERCIAL_TOPIC_SEND_SIZE, new StatsItemSet(COMMERCIAL_TOPIC_SEND_SIZE, this.commercialStatsExecutor,
            commercialLog));

        this.statsTable.put(COMMERCIAL_GROUP_RCV_SIZE, new StatsItemSet(COMMERCIAL_GROUP_RCV_SIZE, this.commercialStatsExecutor,
            commercialLog));

        this.statsTable.put(COMMERCIAL_GROUP_RCV_EPOLLS, new StatsItemSet(COMMERCIAL_GROUP_RCV_EPOLLS, this.commercialStatsExecutor,
            commercialLog));

        this.statsTable.put(COMMERCIAL_GROUP_SNDBCK_TIMES, new StatsItemSet(COMMERCIAL_GROUP_SNDBCK_TIMES, this.commercialStatsExecutor,
            commercialLog));

        this.statsTable.put(COMMERCIAL_GROUP_SNDBCK_SIZE, new StatsItemSet(COMMERCIAL_GROUP_SNDBCK_SIZE, this.commercialStatsExecutor,
            commercialLog));
    }


    public void start() {
    }


    public void shutdown() {
        this.scheduledExecutorService.shutdown();
    }


    public StatsItem getStatsItem(final String statsName, final String statsKey) {
        try {
            return this.statsTable.get(statsName).getStatsItem(statsKey);
        }
        catch (Exception e) {
        }

        return null;
    }


    public void incTopicPutNums(final String topic) {
        this.statsTable.get(TOPIC_PUT_NUMS).addValue(topic, 1, 1);
    }


    public void incTopicPutSize(final String topic, final int size) {
        this.statsTable.get(TOPIC_PUT_SIZE).addValue(topic, size, 1);
    }


    public void incGroupGetNums(final String group, final String topic, final int incValue) {
        final String statsKey = buildStatsKey(topic, group);
        this.statsTable.get(GROUP_GET_NUMS).addValue(statsKey, incValue, 1);
    }


    public void incGroupGetSize(final String group, final String topic, final int incValue) {
        final String statsKey = buildStatsKey(topic, group);
        this.statsTable.get(GROUP_GET_SIZE).addValue(statsKey, incValue, 1);
    }


    public void incBrokerPutNums() {
        this.statsTable.get(BROKER_PUT_NUMS).getAndCreateStatsItem(this.clusterName).getValue().incrementAndGet();
    }


    public void incBrokerGetNums(final int incValue) {
        this.statsTable.get(BROKER_GET_NUMS).getAndCreateStatsItem(this.clusterName).getValue().addAndGet(incValue);
    }


    public void incSendBackNums(final String group, final String topic) {
        final String statsKey = buildStatsKey(topic, group);
        this.statsTable.get(SNDBCK_PUT_NUMS).addValue(statsKey, 1, 1);
    }


    public double tpsGroupGetNums(final String group, final String topic) {
        final String statsKey = buildStatsKey(topic, group);
        return this.statsTable.get(GROUP_GET_NUMS).getStatsDataInMinute(statsKey).getTps();
    }


    public void incBrokerGetFromDiskNums(final int incValue) {
        this.statsTable.get(BROKER_GET_FROM_DISK_NUMS).getAndCreateStatsItem(this.clusterName).getValue().addAndGet(incValue);
    }


    public void incGroupGetFromDiskSize(final String group, final String topic, final int incValue) {
        final String statsKey = buildStatsKey(topic, group);
        this.statsTable.get(GROUP_GET_FROM_DISK_SIZE).addValue(statsKey, incValue, 1);
    }


    public void incGroupGetFromDiskNums(final String group, final String topic, final int incValue) {
        final String statsKey = buildStatsKey(topic, group);
        this.statsTable.get(GROUP_GET_FROM_DISK_NUMS).addValue(statsKey, incValue, 1);
    }


    public void incBrokerGetFromDiskNums(final String group, final String topic, final int incValue) {
        final String statsKey = buildStatsKey(topic, group);
        this.statsTable.get(BROKER_GET_FROM_DISK_NUMS).addValue(statsKey, incValue, 1);
    }


    public void incBrokerGetFromDiskSize(final String group, final String topic, final int incValue) {
        final String statsKey = buildStatsKey(topic, group);
        this.statsTable.get(BROKER_GET_FROM_DISK_SIZE).addValue(statsKey, incValue, 1);
    }


    public String buildStatsKey(String topic, String group) {
        StringBuffer strBuilder = new StringBuffer();
        strBuilder.append(topic);
        strBuilder.append("@");
        strBuilder.append(group);
        return strBuilder.toString();
    }


    public String buildCommercialStatsKey(String topic, String group, String type) {
        StringBuffer strBuilder = new StringBuffer();
        strBuilder.append(topic);
        strBuilder.append("@");
        strBuilder.append(group);
        strBuilder.append("@");
        strBuilder.append(type);
        return strBuilder.toString();
    }


    public void recordDiskFallBehind(final String group, final String topic, final int queueId, final long fallBehind) {
        final String statsKey = String.format("%d@%s@%s", queueId, topic, group);
        this.momentStatsItemSet.getAndCreateStatsItem(statsKey).getValue().set(fallBehind);
    }


    // For commercial
    public void incCommercialTopicSendTimes(final String group, final String topic, final String type, final int incValue) {
        final String statsKey = buildCommercialStatsKey(topic, group, type);
        this.statsTable.get(COMMERCIAL_TOPIC_SEND_TIMES).addValue(statsKey, incValue, 1);
    }


    public void incCommercialTopicSendSize(final String group, final String topic, final String type, final int size) {
        final String statsKey = buildCommercialStatsKey(topic, group, type);
        this.statsTable.get(COMMERCIAL_TOPIC_SEND_SIZE).addValue(statsKey, size, 1);
    }


    public void incCommercialGroupRcvTimes(final String group, final String topic, final String type, final int incValue) {
        final String statsKey = buildCommercialStatsKey(topic, group, type);
        this.statsTable.get(COMMERCIAL_GROUP_RCV_TIMES).addValue(statsKey, incValue, 1);
    }


    public void incCommercialGroupRcvSize(final String group, final String topic, final String type, final int size) {
        final String statsKey = buildCommercialStatsKey(topic, group, type);
        this.statsTable.get(COMMERCIAL_GROUP_RCV_SIZE).addValue(statsKey, size, 1);
    }


    public void incCommercialGroupRcvEpolls(final String group, final String topic, final String type, final int incValue) {
        final String statsKey = buildCommercialStatsKey(topic, group, type);
        this.statsTable.get(COMMERCIAL_GROUP_RCV_EPOLLS).addValue(statsKey, incValue, 1);
    }


    public void incCommercialGroupSndBckTimes(final String group, final String topic, final String type, final int incValue) {
        final String statsKey = buildCommercialStatsKey(topic, group, type);
        this.statsTable.get(COMMERCIAL_GROUP_SNDBCK_TIMES).addValue(statsKey, incValue, 1);
    }


    public void incCommercialGroupSndBckSize(final String group, final String topic, final String type, final int size) {
        final String statsKey = buildCommercialStatsKey(topic, group, type);
        this.statsTable.get(COMMERCIAL_GROUP_SNDBCK_SIZE).addValue(statsKey, size, 1);
    }
}
