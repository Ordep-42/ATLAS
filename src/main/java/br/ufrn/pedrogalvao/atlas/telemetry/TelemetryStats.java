package br.ufrn.pedrogalvao.atlas.telemetry;

public class TelemetryStats {
	private long count;
	private double min;
	private double max;
	private double average;
	
	public TelemetryStats(long count, double min, double max, double average) {
		this.count = count;
		this.min = min;
		this.max = max;
		this.average = average;
	}

	public long getCount() {
		return count;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getAverage() {
		return average;
	}
}
