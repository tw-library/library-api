package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.copy.projections.CopyWithBookInline;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = CopyWithBookInline.class)
public interface CopyRepository extends CrudRepository<Copy, Integer> {
}
