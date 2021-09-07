package com.assignment.content_sorting.util;

import java.util.logging.Logger;

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

	private Logger logger;

	/**
	 * Instantiates a new timer task.
	 *
	 * @param name the name
	 * @param logger 
	 */
	public TimerTask(String name, Logger logger) {
		this.name = name;
		this.logger = logger;
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
		logger.info("Stage: " + stage + "\t TimeTaken: " + (end - stageStart) + " ms.");
		this.stageStart=end;
	}
	
	/**
	 * Processing complete.
	 */
	public void processingComplete() {
		Long end = System.currentTimeMillis();
		logger.info("Completed "+name+ "\t TimeTaken: " + (end - start) + " ms.");
	}
}
