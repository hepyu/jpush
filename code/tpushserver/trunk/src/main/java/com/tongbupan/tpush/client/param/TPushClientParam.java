package com.tongbupan.tpush.client.param;

public class TPushClientParam {

	private int batchSubmitCount;

	private int batchSubmitReTryCount;

	private int batchSleepBetweenReTry;

	private int batchSleepBeforeNextBatch;

	private int threadCount;

	private int topicExpireTime;

	public int getBatchSubmitCount() {
		return batchSubmitCount;
	}

	public void setBatchSubmitCount(int batchSubmitCount) {
		this.batchSubmitCount = batchSubmitCount;
	}

	public int getBatchSubmitReTryCount() {
		return batchSubmitReTryCount;
	}

	public void setBatchSubmitReTryCount(int batchSubmitReTryCount) {
		this.batchSubmitReTryCount = batchSubmitReTryCount;
	}

	public int getBatchSleepBetweenReTry() {
		return batchSleepBetweenReTry;
	}

	public void setBatchSleepBetweenReTry(int batchSleepBetweenReTry) {
		this.batchSleepBetweenReTry = batchSleepBetweenReTry;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getBatchSleepBeforeNextBatch() {
		return batchSleepBeforeNextBatch;
	}

	public void setBatchSleepBeforeNextBatch(int batchSleepBeforeNextBatch) {
		this.batchSleepBeforeNextBatch = batchSleepBeforeNextBatch;
	}

	public int getTopicExpireTime() {
		return topicExpireTime;
	}

	public void setTopicExpireTime(int topicExpireTime) {
		this.topicExpireTime = topicExpireTime;
	}

}
