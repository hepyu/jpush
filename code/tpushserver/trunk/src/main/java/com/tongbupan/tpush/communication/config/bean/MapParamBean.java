package com.tongbupan.tpush.communication.config.bean;

public class MapParamBean {

	public int concurrencyLevel;// = 64;

	public float loadFactor;// = 0.75f;

	public int initCapacity;// = 1024 * 10;

	public int getConcurrencyLevel() {
		return concurrencyLevel;
	}

	public void setConcurrencyLevel(int concurrencyLevel) {
		this.concurrencyLevel = concurrencyLevel;
	}

	public float getLoadFactor() {
		return loadFactor;
	}

	public void setLoadFactor(float loadFactor) {
		this.loadFactor = loadFactor;
	}

	public int getInitCapacity() {
		return initCapacity;
	}

	public void setInitCapacity(int initCapacity) {
		this.initCapacity = initCapacity;
	}

}