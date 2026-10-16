package com.example.taske_api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final List<Task> tasks = new ArrayList<>(
            List.of(
                    new Task(1L, "Learn Java", false),
                    new Task(2L, "Learn Spring Boot", false)
            )
    );

    private final AtomicLong nextId = new AtomicLong(2);

    @GetMapping
    public List<Task> getAll() {
        return tasks;
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping
    public Task create(@RequestBody CreateTask request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Title is required");
        }

        Task task = new Task(
                nextId.incrementAndGet(),
                request.title(),
                false
        );

        tasks.add(task);
        return task;
    }

    public record CreateTask(String title) {}
}
