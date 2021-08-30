package ru.moreapart.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moreapart.manager.entity.Rights;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RightsRepository extends JpaRepository<Rights, Long> {
    Optional<Rights> findById(Long id);

    Optional<Rights> findByTitle(String title);

    List<Rights> findAll();

    long count();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM rights_siblings WHERE siblings = :id", nativeQuery = true)
    void deleteSiblingsByRoleId(@Param("id") Long id);
}