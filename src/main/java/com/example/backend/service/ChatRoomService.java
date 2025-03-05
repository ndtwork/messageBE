package com.example.backend.service;

import com.example.backend.model.ChatRoom;
import com.example.backend.model.User;
import com.example.backend.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserService userService;

    public ChatRoom createChatRoom(String name, Long createdByUserId) {
        User createdByUser = userService.getUserById(createdByUserId);
        if (createdByUser == null) {
            throw new RuntimeException("User not found");
        }
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setName(name);
        newChatRoom.setCreatedByUser(createdByUserId != null ? createdByUser : null); // Allow null for system rooms if needed
        return chatRoomRepository.save(newChatRoom);
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElse(null);
    }

    public List<ChatRoom> searchChatRoomsByName(String name) {
        return chatRoomRepository.findByNameContainingIgnoreCase(name);
    }
}