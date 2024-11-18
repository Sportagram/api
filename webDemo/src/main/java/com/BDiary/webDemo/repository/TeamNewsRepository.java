package com.BDiary.webDemo.repository;

import com.BDiary.webDemo.entity.TeamNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TeamNewsRepository extends JpaRepository<TeamNews, Long> {

    List<TeamNews> findByTeamName(String teamName);
    List<TeamNews> findByTeamNameAndCategory(String teamName, String category);
    List<TeamNews> findByCreatedAtAfter(LocalDateTime date);
    List<TeamNews> findByTeamNameOrderByCreatedAtDesc(String teamName);

    boolean existsByTeamNameAndTitle(String teamName, String title);

}
