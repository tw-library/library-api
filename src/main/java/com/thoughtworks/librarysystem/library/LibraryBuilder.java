package com.thoughtworks.librarysystem.library;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class LibraryBuilder {

    private String name;
    private String slug;

    public Library build(){
        Library library = new Library();

        library.setName(name);
        library.setSlug(slug);

        return library;
    }
}
