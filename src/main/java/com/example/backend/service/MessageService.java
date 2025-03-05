package com.example.backend.service;

import com.example.backend.model.ChatRoom;
import com.example.backend.model.Message;
import com.example.backend.model.User;
import com.example.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatRoomService chatRoomService;

    public Message saveMessage(Long senderUserId, Long chatRoomId, String content) {
        User senderUser = userService.getUserById(senderUserId);
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        if (senderUser == null || chatRoom == null) {
            throw new RuntimeException("Invalid sender or chat room");
        }
        Message newMessage = new Message();
        newMessage.setSenderUser(senderUser);
        newMessage.setChatRoom(chatRoom);
        newMessage.setContent(content);
        return messageRepository.save(newMessage);
    }

    public List<Message> getMessagesByChatRoomId(Long chatRoomId) {
        return messageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }
}