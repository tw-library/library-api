package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.copy.projections.CopyWithBookInline;
import com.thoughtworks.librarysystem.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection = CopyWithBookInline.class)
public interface CopyRepository extends PagingAndSortingRepository<Copy, Integer> {

    @Query(name = "select c from Copy c where c.library.slug = :slug")
    List<Copy> findCopiesByLibrarySlug(@Param("slug") String slug);

    //List<Copy> findCopiesByLibrarySlugAndByUserId(@Param("slug") String slug, @Param("userId") Integer userId);

    @Query(name = "select count(Copy.copy_id) from Copy where Copy.library.slug = :slug and Copy.book_id= :book")
    Long countByLibrarySlugAndBookId(@Param("slug") String slug, @Param("book") Integer book);


    @Query(name = "select count(Copy.copy_id) from Copy where Copy.library.slug = :slug and Copy.book_id= :book and status = AVAILABLE")
    Long countByLibrarySlugAndBookIdAndStatus(@Param("slug") String slug, @Param("book") Integer book, @Param("status") CopyStatus status);

    List<Copy> findDistinctCopiesByLibrarySlugAndBookIdAndStatus(@Param("slug") String slug, @Param("book") Integer book, @Param("status") CopyStatus status);

    List<Copy> findCopiesByLoansAndLoansUserId(@Param("id") Integer id);
}
