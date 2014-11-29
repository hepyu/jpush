package com.tongbupan.tpush.partitioner;

public class DefaultPartitioner extends TopicPartitioner {

	public DefaultPartitioner() {
		partitioner = this;
	}

	private int channelNum;

	private static String CHANNEL_PREFIX = "tpush:channel:";

	@Override
	public String getChannel(String topic) {
		int hcode = topic.hashCode();
		int cn = hcode % channelNum;
		return CHANNEL_PREFIX + cn;
	}

	@Override
	public String[] getChannels() {
		int total = channelNum * 2 - 1;
		String[] channels = new String[total];
		for (int i = 0; i < total; i++) {
			channels[i] = CHANNEL_PREFIX + (-channelNum + 1 + i);
		}
		return channels;
	}

	@Override
	public int getChannelNum() {
		return channelNum;
	}

	@Override
	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}

}
