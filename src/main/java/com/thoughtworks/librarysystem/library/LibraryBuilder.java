package com.thoughtworks.librarysystem.library;

import com.thoughtworks.librarysystem.copy.Copy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

import java.util.Set;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class LibraryBuilder {

    private String name;
    private String slug;
    private Set<Copy> copies;

    public Library build(){
        Library library = new Library();

        library.setName(name);
        library.setSlug(slug);
        library.setCopies(copies);

        return library;
    }
}
