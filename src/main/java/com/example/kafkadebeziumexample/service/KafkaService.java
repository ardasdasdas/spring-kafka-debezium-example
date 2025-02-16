package com.example.kafkadebeziumexample.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private static final String AFTER = "after";
    private static final String BEFORE = "before";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String ID = "id";

    @Autowired
    private CacheService cacheService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "dbserver.testdb.products", groupId = "cache-updater-group")
    public void listenProductUpdate(String message) {
        try {
            JsonNode eventNode = objectMapper.readTree(message);
            JsonNode payload = eventNode.get("payload");

            String operationType = payload.get("op").asText(); // 'c' -> create, 'u' -> update, 'd' -> delete

            switch (operationType) {
                case "c": // Insert
                    handleInsert(payload.get(AFTER));
                    break;
                case "u": // Update
                    handleUpdate(payload.get(BEFORE), payload.get(AFTER));
                    break;
                case "d": // Delete
                    handleDelete(payload.get(BEFORE));
                    break;
                default:
                    System.out.println("Unsupported operation: " + operationType);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleInsert(JsonNode afterNode) {
        Long productId = afterNode.get(ID).asLong();
        String name = afterNode.get(NAME).asText();
        Double price = afterNode.get(PRICE).asDouble();
        cacheService.saveToCache(productId, name, price);
        System.out.println("New product inserted: " + name);
    }

    private void handleUpdate(JsonNode beforeNode, JsonNode afterNode) {
        if (beforeNode != null && afterNode != null) {
            Long productId = afterNode.get(ID).asLong();
            String oldName = beforeNode.get(NAME).asText();
            String newName = afterNode.get(NAME).asText();
            Double oldPrice = beforeNode.get(PRICE).asDouble();
            Double newPrice = afterNode.get(PRICE).asDouble();

            if (!oldName.equals(newName) || !oldPrice.equals(newPrice)) {
                cacheService.saveToCache(productId, newName, newPrice);
                System.out.println("Product updated: " + oldName + " -> " + newName);
            }
        }
    }

    private void handleDelete(JsonNode beforeNode) {
        if (beforeNode != null) {
            Long productId = beforeNode.get(ID).asLong();
            cacheService.removeFromCache(productId);
            System.out.println("Product deleted with ID: " + productId);
        }
    }
}

