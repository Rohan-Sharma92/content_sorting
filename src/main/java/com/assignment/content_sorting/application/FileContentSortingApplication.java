package com.assignment.content_sorting.application;

import com.assignment.content_sorting.guice.ContentSortingApplicationModule;
import com.assignment.content_sorting.service.ContentSortingService;
import com.assignment.content_sorting.service.IService;
import com.assignment.content_sorting.service.InitialisationService;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The Class FileContentSortingApplication.
 * This is the entry point for the application
 * 
 * @author Rohan
 */
public class FileContentSortingApplication 
{
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main( String[] args )
    {
    	Injector injector = Guice.createInjector(new ContentSortingApplicationModule());
    	IService initialisationService = injector.getInstance(InitialisationService.class);
    	IService contentService = injector.getInstance(ContentSortingService.class);
    	initialisationService.addDependencies(contentService);
    	initialisationService.start();
    }
}
