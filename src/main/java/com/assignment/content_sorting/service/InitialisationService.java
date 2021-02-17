package com.assignment.content_sorting.service;

import java.io.File;
import java.nio.file.Paths;

import com.assignment.content_sorting.properties.IServerConfig;
import com.google.inject.Inject;

/**
 * The Class InitialisationService.
 * @author Rohan
 */
public class InitialisationService extends AbstractDependentService {

	/** The server config. */
	private final IServerConfig serverConfig;

	/**
	 * Instantiates a new initialisation service.
	 *
	 * @param serverConfig the server config
	 */
	@Inject
	public InitialisationService(final IServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performProcessing() {
		createDir(serverConfig.getListenDirectory(),false);
		createDir(serverConfig.getOutputDirectory(),true);
		createDir(serverConfig.getTempDirectory(),true);
	}

	/**
	 * Creates the dir.
	 *
	 * @param dirPath the dir path
	 * @param cleanUp the clean up
	 */
	private void createDir(String dirPath, boolean cleanUp) {
		File dir = Paths.get(dirPath).toFile();
		if (!dir.exists()) {
			dir.mkdirs();
		} else if(cleanUp) {
			for (File file : dir.listFiles()) {
				file.delete();
			}
		}
	}
}
