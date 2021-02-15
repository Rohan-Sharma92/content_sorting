package com.assignment.content_sorting.service;

import java.io.File;
import java.nio.file.Paths;

import com.assignment.content_sorting.properties.IServerConfig;
import com.google.inject.Inject;

public class InitialisationService extends AbstractDependentService {

	private final IServerConfig serverConfig;

	@Inject
	public InitialisationService(final IServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Override
	protected void performProcessing() {
		createDir(serverConfig.getListenDirectory(),false);
		createDir(serverConfig.getOutputDirectory(),true);
		createDir(serverConfig.getTempDirectory(),true);
	}

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
