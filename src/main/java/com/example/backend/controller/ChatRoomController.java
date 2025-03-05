package com.example.backend.controller;

import com.example.backend.model.ChatRoom;
import com.example.backend.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestBody Map<String, String> chatRoomData) {
        String name = chatRoomData.get("name");
        Long createdByUserId = null;
        if (chatRoomData.containsKey("createdByUserId")) {
            try {
                createdByUserId = Long.parseLong(chatRoomData.get("createdByUserId"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid createdByUserId"));
            }
        }


        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Chat room name cannot be empty"));
        }
        try {
            ChatRoom createdChatRoom = chatRoomService.createChatRoom(name, createdByUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdChatRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChatRoomById(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        if (chatRoom != null) {
            return ResponseEntity.ok(chatRoom);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Chat room not found"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChatRoom>> searchChatRooms(@RequestParam String name) {
        return ResponseEntity.ok(chatRoomService.searchChatRoomsByName(name));
    }
}