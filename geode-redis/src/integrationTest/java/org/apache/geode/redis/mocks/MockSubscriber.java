/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package org.apache.geode.redis.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import redis.clients.jedis.JedisPubSub;

public class MockSubscriber extends JedisPubSub {
  private final List<String> receivedMessages = Collections.synchronizedList(new ArrayList<>());
  private final List<String> receivedPMessages = Collections.synchronizedList(new ArrayList<>());

  public List<String> getReceivedMessages() {
    return new ArrayList<>(receivedMessages);
  }

  public List<String> getReceivedPMessages() {
    return new ArrayList<>(receivedPMessages);
  }

  public final List<UnsubscribeInfo> unsubscribeInfos =
      Collections.synchronizedList(new ArrayList<>());
  public final List<UnsubscribeInfo> punsubscribeInfos =
      Collections.synchronizedList(new ArrayList<>());

  @Override
  public void onUnsubscribe(String channel, int subscribedChannels) {
    unsubscribeInfos.add(new UnsubscribeInfo(channel, subscribedChannels));
  }

  @Override
  public void onPUnsubscribe(String pattern, int subscribedChannels) {
    punsubscribeInfos.add(new UnsubscribeInfo(pattern, subscribedChannels));
  }

  public static class UnsubscribeInfo {
    public final String channel;
    public final int count;

    public UnsubscribeInfo(String channel, int count) {
      this.channel = channel;
      this.count = count;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof UnsubscribeInfo)) {
        return false;
      }
      UnsubscribeInfo that = (UnsubscribeInfo) o;
      return count == that.count &&
          Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
      return Objects.hash(channel, count);
    }

    @Override
    public String toString() {
      return "UnsubscribeInfo{" +
          "channel='" + channel + '\'' +
          ", count=" + count +
          '}';
    }
  }


  @Override
  public void onMessage(String channel, String message) {
    receivedMessages.add(message);
  }

  @Override
  public void onPMessage(String pattern, String channel, String message) {
    receivedPMessages.add(message);
  }
}
