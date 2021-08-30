package ru.moreapart.manager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.moreapart.manager.dto.PageDto;
import ru.moreapart.manager.entity.Inventory;
import ru.moreapart.manager.pojo.MessageResponse;
import ru.moreapart.manager.service.InventoryService;
import ru.moreapart.manager.service.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InventoryController {
    public static final int INVENTORY_PER_PAGE = 15;

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    public InventoryController(InventoryService inventoryService, ObjectMapper objectMapper, HttpServletRequest request) {
        this.inventoryService = inventoryService;
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<?> list(
            @PageableDefault(size = INVENTORY_PER_PAGE, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        PageDto list = inventoryService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> info(@PathVariable("id") Inventory inventory) {
        return ResponseEntity.status(HttpStatus.OK).body(inventory);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Inventory inventory,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Inventory updated  = inventoryService.create(inventory, userDetails.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create"));
        }
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> create(
            @RequestParam(value = "model") String jsonData,
            @RequestParam(value = "file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Inventory inventory = objectMapper.readValue(jsonData, Inventory.class);
            Inventory updated;
            if (!file.isEmpty()) {
                updated = inventoryService.create(inventory, file, userDetails.getId());
            } else {
                updated = inventoryService.create(inventory, userDetails.getId());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create-from-json"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create"));
        }
    }

    @RequestMapping(
            value = "/test",
            method = RequestMethod.POST,
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE },
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            inventoryService.create(file);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create"));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Inventory inventoryFromDb,
            @RequestBody Inventory inventory
    ) {
        try {
            Inventory updated  = inventoryService.update(inventoryFromDb, inventory);

            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new MessageResponse("error-item-update"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Inventory inventory) {
        inventoryService.delete(inventory);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("success-item-remove"));
    }
}