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
package com.alibaba.rocketmq.common;

import com.alibaba.rocketmq.common.annotation.ImportantField;
import com.alibaba.rocketmq.common.constant.PermName;
import com.alibaba.rocketmq.remoting.common.RemotingUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author shijia.wxr
 */
public class BrokerConfig {
    private String rocketmqHome = System.getProperty(MixAll.ROCKETMQ_HOME_PROPERTY,
            System.getenv(MixAll.ROCKETMQ_HOME_ENV));
    @ImportantField
    private String namesrvAddr = System.getProperty(MixAll.NAMESRV_ADDR_PROPERTY,
            System.getenv(MixAll.NAMESRV_ADDR_ENV));
    @ImportantField
    private String brokerIP1 = RemotingUtil.getLocalAddress();
    private String brokerIP2 = RemotingUtil.getLocalAddress();
    @ImportantField
    private String brokerName = localHostName();
    @ImportantField
    private String brokerClusterName = "DefaultCluster";
    @ImportantField
    private long brokerId = MixAll.MASTER_ID;
    private int brokerPermission = PermName.PERM_READ | PermName.PERM_WRITE;
    private int defaultTopicQueueNums = 8;
    @ImportantField
    private boolean autoCreateTopicEnable = true;
    private boolean clusterTopicEnable = true;
    private boolean brokerTopicEnable = true;
    @ImportantField
    private boolean autoCreateSubscriptionGroup = true;

    private int sendMessageThreadPoolNums = 16 + Runtime.getRuntime().availableProcessors() * 4;
    private int pullMessageThreadPoolNums = 16 + Runtime.getRuntime().availableProcessors() * 2;
    private int adminBrokerThreadPoolNums = 16;
    private int clientManageThreadPoolNums = 16;

    private int flushConsumerOffsetInterval = 1000 * 5;

    private int flushConsumerOffsetHistoryInterval = 1000 * 60;

    @ImportantField
    private boolean rejectTransactionMessage = false;

    @ImportantField
    private boolean fetchNamesrvAddrByAddressServer = false;

    private int sendThreadPoolQueueCapacity = 100000;

    private int pullThreadPoolQueueCapacity = 100000;

    private int filterServerNums = 0;

    private boolean longPollingEnable = true;

    private long shortPollingTimeMills = 1000;

    private boolean notifyConsumerIdsChangedEnable = true;
    private boolean highSpeedMode = false;

    private boolean transferMsgByHeap = false;


    public boolean isTransferMsgByHeap() {
        return transferMsgByHeap;
    }

    public void setTransferMsgByHeap(final boolean transferMsgByHeap) {
        this.transferMsgByHeap = transferMsgByHeap;
    }
    public static String localHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "DEFAULT_BROKER";
    }

    public boolean isHighSpeedMode() {
        return highSpeedMode;
    }

    public void setHighSpeedMode(final boolean highSpeedMode) {
        this.highSpeedMode = highSpeedMode;
    }

    public String getRocketmqHome() {
        return rocketmqHome;
    }


    public void setRocketmqHome(String rocketmqHome) {
        this.rocketmqHome = rocketmqHome;
    }


    public String getBrokerName() {
        return brokerName;
    }


    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }


    public int getBrokerPermission() {
        return brokerPermission;
    }


    public void setBrokerPermission(int brokerPermission) {
        this.brokerPermission = brokerPermission;
    }


    public int getDefaultTopicQueueNums() {
        return defaultTopicQueueNums;
    }


    public void setDefaultTopicQueueNums(int defaultTopicQueueNums) {
        this.defaultTopicQueueNums = defaultTopicQueueNums;
    }


    public boolean isAutoCreateTopicEnable() {
        return autoCreateTopicEnable;
    }


    public void setAutoCreateTopicEnable(boolean autoCreateTopic) {
        this.autoCreateTopicEnable = autoCreateTopic;
    }


    public String getBrokerClusterName() {
        return brokerClusterName;
    }


    public void setBrokerClusterName(String brokerClusterName) {
        this.brokerClusterName = brokerClusterName;
    }


    public String getBrokerIP1() {
        return brokerIP1;
    }


    public void setBrokerIP1(String brokerIP1) {
        this.brokerIP1 = brokerIP1;
    }


    public String getBrokerIP2() {
        return brokerIP2;
    }


    public void setBrokerIP2(String brokerIP2) {
        this.brokerIP2 = brokerIP2;
    }


    public int getSendMessageThreadPoolNums() {
        return sendMessageThreadPoolNums;
    }


    public void setSendMessageThreadPoolNums(int sendMessageThreadPoolNums) {
        this.sendMessageThreadPoolNums = sendMessageThreadPoolNums;
    }


    public int getPullMessageThreadPoolNums() {
        return pullMessageThreadPoolNums;
    }


    public void setPullMessageThreadPoolNums(int pullMessageThreadPoolNums) {
        this.pullMessageThreadPoolNums = pullMessageThreadPoolNums;
    }


    public int getAdminBrokerThreadPoolNums() {
        return adminBrokerThreadPoolNums;
    }


    public void setAdminBrokerThreadPoolNums(int adminBrokerThreadPoolNums) {
        this.adminBrokerThreadPoolNums = adminBrokerThreadPoolNums;
    }


    public int getFlushConsumerOffsetInterval() {
        return flushConsumerOffsetInterval;
    }


    public void setFlushConsumerOffsetInterval(int flushConsumerOffsetInterval) {
        this.flushConsumerOffsetInterval = flushConsumerOffsetInterval;
    }


    public int getFlushConsumerOffsetHistoryInterval() {
        return flushConsumerOffsetHistoryInterval;
    }


    public void setFlushConsumerOffsetHistoryInterval(int flushConsumerOffsetHistoryInterval) {
        this.flushConsumerOffsetHistoryInterval = flushConsumerOffsetHistoryInterval;
    }


    public boolean isClusterTopicEnable() {
        return clusterTopicEnable;
    }


    public void setClusterTopicEnable(boolean clusterTopicEnable) {
        this.clusterTopicEnable = clusterTopicEnable;
    }


    public String getNamesrvAddr() {
        return namesrvAddr;
    }


    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }


    public long getBrokerId() {
        return brokerId;
    }


    public void setBrokerId(long brokerId) {
        this.brokerId = brokerId;
    }


    public boolean isAutoCreateSubscriptionGroup() {
        return autoCreateSubscriptionGroup;
    }


    public void setAutoCreateSubscriptionGroup(boolean autoCreateSubscriptionGroup) {
        this.autoCreateSubscriptionGroup = autoCreateSubscriptionGroup;
    }


    public boolean isRejectTransactionMessage() {
        return rejectTransactionMessage;
    }


    public void setRejectTransactionMessage(boolean rejectTransactionMessage) {
        this.rejectTransactionMessage = rejectTransactionMessage;
    }


    public boolean isFetchNamesrvAddrByAddressServer() {
        return fetchNamesrvAddrByAddressServer;
    }


    public void setFetchNamesrvAddrByAddressServer(boolean fetchNamesrvAddrByAddressServer) {
        this.fetchNamesrvAddrByAddressServer = fetchNamesrvAddrByAddressServer;
    }


    public int getSendThreadPoolQueueCapacity() {
        return sendThreadPoolQueueCapacity;
    }


    public void setSendThreadPoolQueueCapacity(int sendThreadPoolQueueCapacity) {
        this.sendThreadPoolQueueCapacity = sendThreadPoolQueueCapacity;
    }


    public int getPullThreadPoolQueueCapacity() {
        return pullThreadPoolQueueCapacity;
    }


    public void setPullThreadPoolQueueCapacity(int pullThreadPoolQueueCapacity) {
        this.pullThreadPoolQueueCapacity = pullThreadPoolQueueCapacity;
    }


    public boolean isBrokerTopicEnable() {
        return brokerTopicEnable;
    }


    public void setBrokerTopicEnable(boolean brokerTopicEnable) {
        this.brokerTopicEnable = brokerTopicEnable;
    }


    public int getFilterServerNums() {
        return filterServerNums;
    }


    public void setFilterServerNums(int filterServerNums) {
        this.filterServerNums = filterServerNums;
    }


    public boolean isLongPollingEnable() {
        return longPollingEnable;
    }


    public void setLongPollingEnable(boolean longPollingEnable) {
        this.longPollingEnable = longPollingEnable;
    }


    public boolean isNotifyConsumerIdsChangedEnable() {
        return notifyConsumerIdsChangedEnable;
    }


    public void setNotifyConsumerIdsChangedEnable(boolean notifyConsumerIdsChangedEnable) {
        this.notifyConsumerIdsChangedEnable = notifyConsumerIdsChangedEnable;
    }


    public long getShortPollingTimeMills() {
        return shortPollingTimeMills;
    }


    public void setShortPollingTimeMills(long shortPollingTimeMills) {
        this.shortPollingTimeMills = shortPollingTimeMills;
    }


    public int getClientManageThreadPoolNums() {
        return clientManageThreadPoolNums;
    }


    public void setClientManageThreadPoolNums(int clientManageThreadPoolNums) {
        this.clientManageThreadPoolNums = clientManageThreadPoolNums;
    }
}
