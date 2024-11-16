package com.BDiary.webDemo.repository;

import com.BDiary.webDemo.entity.UserEntity;
import com.BDiary.webDemo.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}

 */

public interface UserRepository extends JpaRepository<user, Long>{
    user findByGooglename(String googlename);

    // 필요한 경우 추가 쿼리 메서드 정의
    Optional<user> findByEmail(String email);
    boolean existsByNickname(String nickname);

}