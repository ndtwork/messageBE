package com.example.backend.controller;

import com.example.backend.model.Message;
import com.example.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<?> saveMessage(@RequestBody Map<String, String> messageData) {
        Long senderUserId;
        Long chatRoomId;
        String content = messageData.get("content");

        try {
            senderUserId = Long.parseLong(messageData.get("senderUserId"));
            chatRoomId = Long.parseLong(messageData.get("chatRoomId"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user or chat room ID"));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Message content cannot be empty"));
        }

        try {
            Message savedMessage = messageService.saveMessage(senderUserId, chatRoomId, content);

            // Publish message to WebSocket topic for real-time update
            messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoomId, savedMessage);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }


    @GetMapping("/chat-room/{chatRoomId}")
    public ResponseEntity<List<Message>> getMessagesByChatRoomId(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(messageService.getMessagesByChatRoomId(chatRoomId));
    }

    // WebSocket endpoint for receiving messages (MessageMapping) and broadcasting (SimpMessagingTemplate)
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        messagingTemplate.convertAndSend("/topic/chat-room/" + message.getChatRoom().getId(), message);
    }
}