package com.sathwik.auth.auth_service.repository;

import com.sathwik.auth.auth_service.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    List<TodoEntity> findByUser_UserId(String userId);

    Optional<TodoEntity> findByIdAndUser_UserId(String id, String userId);

    void deleteByIdAndUser_UserId(String id, String userId);
}

