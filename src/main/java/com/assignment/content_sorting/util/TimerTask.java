package com.assignment.content_sorting.util;

/**
 * The Class TimerTask.
 * @author Rohan
 */
public class TimerTask {

	/** The name. */
	private String name;
	
	/** The start. */
	private Long start;
	
	/** The stage start. */
	private Long stageStart;

	/**
	 * Instantiates a new timer task.
	 *
	 * @param name the name
	 */
	public TimerTask(String name) {
		this.name = name;
	}

	/**
	 * Start.
	 */
	public void start() {
		this.start = System.currentTimeMillis();
		this.stageStart=System.currentTimeMillis();
	}

	/**
	 * Complete stage.
	 *
	 * @param stage the stage
	 */
	public void completeStage(String stage) {
		Long end = System.currentTimeMillis();
		System.out.println("Stage: " + stage + "\t TimeTaken: " + (end - stageStart) + " ms.");
		this.stageStart=end;
	}
	
	/**
	 * Processing complete.
	 */
	public void processingComplete() {
		Long end = System.currentTimeMillis();
		System.out.println("Completed "+name+ "\t TimeTaken: " + (end - start) + " ms.");
	}
}
