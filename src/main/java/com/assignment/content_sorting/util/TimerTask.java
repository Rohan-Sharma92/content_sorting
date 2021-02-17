package com.assignment.content_sorting.util;

public class TimerTask {

	private String name;
	private Long start;
	private Long stageStart;

	public TimerTask(String name) {
		this.name = name;
	}

	public void start() {
		this.start = System.currentTimeMillis();
		this.stageStart=System.currentTimeMillis();
	}

	public void completeStage(String stage) {
		Long end = System.currentTimeMillis();
		System.out.println("Stage: " + stage + "\t TimeTaken: " + (end - stageStart) + " ms.");
		this.stageStart=end;
	}
	
	public void processingComplete() {
		Long end = System.currentTimeMillis();
		System.out.println("Completed "+name+ "\t TimeTaken: " + (end - start) + " ms.");
	}
}
