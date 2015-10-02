package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.copy.projections.CopyWithBookInline;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection = CopyWithBookInline.class)
public interface CopyRepository extends PagingAndSortingRepository<Copy, Integer> {

    @Query(name = "select c from Copy c where c.library.slug = :slug")
    List<Copy> findCopiesByLibrarySlug(@Param("slug") String slug);

}
