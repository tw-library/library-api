package com.thoughtworks.librarysystem.commons.factories;

import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;

/**
 * Created by eferreir on 2/8/16.
 */
public class LibraryFactory {


    public Library createStardardLibrary() {
        String sameSlug = "BH";
        String sameName = "Belo Horizonte";
        Library library = new LibraryBuilder()
                .withName(sameName)
                .withSlug(sameSlug)
                .build();

        return library;
    }


    public Library createLibrary(String name, String slug) {
        Library library = new LibraryBuilder()
                .withName(name)
                .withSlug(slug)
                .build();

        return library;
    }
}
