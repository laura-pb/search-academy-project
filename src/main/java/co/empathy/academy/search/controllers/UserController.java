package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.User;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import co.empathy.academy.search.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get a user by its id")
    @Parameter(name = "id", description = "Id of the user to be retrieved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id, it must be an integer", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) throws UserNotFoundException {
        User user = userService.getUser(id);
        ResponseEntity<User> response = ResponseEntity.ok(user);
        return response;
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        ResponseEntity<List<User>> response = ResponseEntity.ok(userService.getUsers());
        return response;
    }

    @Operation(summary = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User added", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "409", description = "User already exists, it can not be added", content = @Content)
    })
    @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.addUser(user);
        ResponseEntity<User> response = ResponseEntity.status(HttpStatus.CREATED).body(user);
        return response;
    }

    @Operation(summary = "Update a user by its id")
    @Parameter(name = "id", description = "Id of the user to be updated")
    @Parameter(name = "user", description = "User with the data that needs to be updated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id, it must be an integer", content = @Content)
    })
    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        User updatedUser = userService.updateUser(id, user);
        ResponseEntity<User> response = ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        return response;
    }

    @Operation(summary = "Delete a user by its id")
    @Parameter(name = "id", description = "Id of the user to be deleted")
    @DeleteMapping(value = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id, it must be an integer", content = @Content)
    })
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
        return response;
    }

}
