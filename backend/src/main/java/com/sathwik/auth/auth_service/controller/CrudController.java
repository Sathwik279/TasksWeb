package com.sathwik.auth.auth_service.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sathwik.auth.auth_service.dto.TodoCreateRequest;
import com.sathwik.auth.auth_service.entity.TodoEntity;
import com.sathwik.auth.auth_service.entity.UserEntity;
import com.sathwik.auth.auth_service.repository.TodoRepository;
import com.sathwik.auth.auth_service.repository.UserRepository;
import com.sathwik.auth.auth_service.service.AiService;
import com.sathwik.auth.auth_service.service.TodoService;

@RestController
@RequestMapping("/crud")
@CrossOrigin(origins="*")
public class CrudController {

    private final TodoRepository todoRepo;
    private final UserRepository userRepo;
    private final TodoService todoService;
    private final AiService aiService;

    public CrudController(TodoService todoService,TodoRepository todoRepo, UserRepository userRepo,AiService aiService){
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
        this.todoService = todoService;
        this.aiService = aiService;
    }

    @GetMapping("/todos")
    public List<TodoEntity> getTodos(Authentication authentication) {

        String userId = (String) authentication.getPrincipal(); // from JWT filter

        return todoRepo.findByUser_UserId(userId);
    }

    @PostMapping("/todo")
    public ResponseEntity<?> createTodo(@RequestBody TodoCreateRequest dto, Authentication authentication) {
        String userId = authentication.getName(); 

        UserEntity user = userRepo.findById(userId).orElseThrow();
        TodoEntity newTodo = new TodoEntity(user, dto.getTitle(), dto.getDescription());
        TodoEntity savedTodo = todoRepo.save(newTodo); 
        
        return ResponseEntity.status(201).body(savedTodo); 
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<?> getTodoById(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        return todoRepo.findByIdAndUser_UserId(id, userId)
                .map(todo -> ResponseEntity.ok(todo)) 
                .orElseGet(() ->
                        ResponseEntity.status(404).body(null)
                );
    }

    public static boolean calculateFetchAi(boolean titleChanged, boolean descriptionChanged, boolean aiEnabledChanged, String currentAiContent) {
    // Trigger if title/desc changed OR if aiEnabled was just turned on OR if we don't have AI content yet
    return titleChanged || descriptionChanged || aiEnabledChanged || currentAiContent == null || currentAiContent.trim().isEmpty();
}
  @PutMapping("/todo/{id}")
    public ResponseEntity<TodoEntity> updateTodo(
            @PathVariable String id,
            @RequestBody TodoEntity dto,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return todoRepo.findByIdAndUser_UserId(id, userId)
                .map(todo -> {
                    // 1. Detect changes
                    boolean titleChanged = !todo.getTitle().equals(dto.getTitle());
                    boolean descriptionChanged = !Objects.equals(todo.getDescription(), dto.getDescription());
                    boolean aiEnabledChanged = todo.isAiEnabled() != dto.isAiEnabled();

                    // 2. Update memory fields
                    todo.setTitle(dto.getTitle());
                    todo.setDescription(dto.getDescription());
                    todo.setDone(dto.isDone());
                    todo.setAiEnabled(dto.isAiEnabled());

                    // 3. Save basic updates first
                    TodoEntity savedTodo = todoRepo.save(todo);

                    // 4. SYNC CALL (Wait for AI)
                    if (dto.isAiEnabled() && calculateFetchAi(titleChanged, descriptionChanged, aiEnabledChanged, todo.getAiContent())) {
                        String newAiContent = aiService.generateAndSaveAiContent(id, dto.getDescription(), userId);
                        savedTodo.setAiContent(newAiContent);
                    }

                    // 5. Return the full object (including AI content if generated)
                    return ResponseEntity.ok(savedTodo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteTodo(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        return todoRepo.findByIdAndUser_UserId(id, userId)
                .map(todo -> {
                    todoRepo.delete(todo);
                    return ResponseEntity.ok(Map.of("msg", "Todo deleted"));
                })
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(Map.of("error", "Todo not found"))
                );
    }
}
