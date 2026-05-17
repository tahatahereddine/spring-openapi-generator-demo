package example.api.web.controller.api;

import example.api.core.model.CreateResourceRequest;
import example.api.core.model.CreateUserRequest;
import example.api.core.model.GetHealth200Response;
import example.api.core.model.ResourceDto;
import example.api.core.model.ResourcePage;
import example.api.core.model.UpdateResourceRequest;
import example.api.core.model.UpdateUserRequest;
import example.api.core.model.UserDto;
import example.api.core.model.UserPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the API delegate for all endpoints
 */
@Service
public class ApiApiDelegateImpl implements ApiApiDelegate {

    // In-memory storage for demo purposes
    private final Map<UUID, UserDto> users = new HashMap<>();
    private final Map<UUID, ResourceDto> resources = new HashMap<>();

    @Override
    public ResponseEntity<GetHealth200Response> getHealth() {
        GetHealth200Response response = new GetHealth200Response();
        response.setStatus(GetHealth200Response.StatusEnum.UP);
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.ok(response);
    }

    // User endpoints

    @Override
    public ResponseEntity<UserPage> listUsers(Integer page, Integer size) {
        if (page == null) page = 0;
        if (size == null) size = 20;
        
        UserPage response = new UserPage();
        List<UserDto> content = new ArrayList<>(users.values());
        int start = page * size;
        int end = Math.min(start + size, content.size());
        response.setContent(content.subList(start, end));
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(content.size());
        response.setTotalPages((content.size() + size - 1) / size);
        response.setLastPage(end >= content.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserDto> createUser(CreateUserRequest createUserRequest) {
        UserDto user = new UserDto();
        user.setId(UUID.randomUUID());
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setActive(createUserRequest.getActive() != null ? createUserRequest.getActive() : true);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
        
        users.put(user.getId(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID userId) {
        UserDto user = users.get(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        UserDto user = users.get(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (updateUserRequest.getUsername() != null) {
            user.setUsername(updateUserRequest.getUsername());
        }
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getActive() != null) {
            user.setActive(updateUserRequest.getActive());
        }
        user.setUpdatedAt(OffsetDateTime.now());
        
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        if (!users.containsKey(userId)) {
            return ResponseEntity.notFound().build();
        }
        users.remove(userId);
        return ResponseEntity.noContent().build();
    }

    // Resource endpoints

    @Override
    public ResponseEntity<ResourcePage> listResources(Integer page, Integer size) {
        if (page == null) page = 0;
        if (size == null) size = 20;
        
        ResourcePage response = new ResourcePage();
        List<ResourceDto> content = new ArrayList<>(resources.values());
        int start = page * size;
        int end = Math.min(start + size, content.size());
        response.setContent(content.subList(start, end));
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(content.size());
        response.setTotalPages((content.size() + size - 1) / size);
        response.setLastPage(end >= content.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResourceDto> createResource(CreateResourceRequest createResourceRequest) {
        ResourceDto resource = new ResourceDto();
        resource.setId(UUID.randomUUID());
        resource.setName(createResourceRequest.getName());
        resource.setDescription(createResourceRequest.getDescription());
        // Convert enum type from request to DTO
        if (createResourceRequest.getType() != null) {
            resource.setType(ResourceDto.TypeEnum.valueOf(createResourceRequest.getType().toString()));
        }
        resource.setActive(createResourceRequest.getActive() != null ? createResourceRequest.getActive() : true);
        resource.setCreatedAt(OffsetDateTime.now());
        resource.setUpdatedAt(OffsetDateTime.now());
        
        resources.put(resource.getId(), resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @Override
    public ResponseEntity<ResourceDto> getResourceById(UUID resourceId) {
        ResourceDto resource = resources.get(resourceId);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resource);
    }

    @Override
    public ResponseEntity<ResourceDto> updateResource(UUID resourceId, UpdateResourceRequest updateResourceRequest) {
        ResourceDto resource = resources.get(resourceId);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (updateResourceRequest.getName() != null) {
            resource.setName(updateResourceRequest.getName());
        }
        if (updateResourceRequest.getDescription() != null) {
            resource.setDescription(updateResourceRequest.getDescription());
        }
        if (updateResourceRequest.getType() != null) {
            resource.setType(ResourceDto.TypeEnum.valueOf(updateResourceRequest.getType().toString()));
        }
        if (updateResourceRequest.getActive() != null) {
            resource.setActive(updateResourceRequest.getActive());
        }
        resource.setUpdatedAt(OffsetDateTime.now());
        
        return ResponseEntity.ok(resource);
    }

    @Override
    public ResponseEntity<Void> deleteResource(UUID resourceId) {
        if (!resources.containsKey(resourceId)) {
            return ResponseEntity.notFound().build();
        }
        resources.remove(resourceId);
        return ResponseEntity.noContent().build();
    }
}

