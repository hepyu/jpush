package com.tongbupan.tpush.partitioner;

public abstract class TopicPartitioner {

	protected static TopicPartitioner partitioner;

	public abstract String getChannel(String topic);

	public abstract int getChannelNum();

	public abstract void setChannelNum(int channelNum);

	public abstract String[] getChannels();
}
