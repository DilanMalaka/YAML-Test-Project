package com.example.TestYaml.Controllers;

import java.util.List;
import java.util.Optional;

import com.example.TestYaml.Models.UserModel;
import com.example.TestYaml.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/fetch")
    public ResponseEntity<?> fetchData(){

        List<UserModel> data = userRepository.findAll();
        if(data.size()>0){
            return new ResponseEntity<List<UserModel>>(data, HttpStatus.OK);

        }else {

            return new ResponseEntity<>("No data available", HttpStatus.NOT_FOUND);
        }


    }


    @PostMapping("/insert")
    public ResponseEntity<?> insertData(@RequestBody UserModel userModel){


        try{

            userRepository.save(userModel);
            return new ResponseEntity<UserModel>(userModel, HttpStatus.OK);

        }catch(Exception e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }



    //to fetch single document
    @GetMapping("/fetch/{id}")
    public ResponseEntity<?> fetchSingle(@PathVariable("id") String id ) {

        Optional<UserModel> userModelOptional = userRepository.findById(id);
        if(userModelOptional.isPresent()){
            return new ResponseEntity<>(userModelOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Data not found with id" +id, HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody UserModel userModel ) {

        Optional<UserModel> userModelOptional = userRepository.findById(id);
        if(userModelOptional.isPresent()){
            //userSave holds the orginal values
            UserModel userSave = userModelOptional.get();
            //set the values, first check whether null or not, if it is not null
            userSave.setCompleted(userModel.getCompleted() != null ? userModel.getCompleted() : userSave.getCompleted());
            userSave.setUsername(userModel.getUsername() != null ? userModel.getUsername() : userSave.getUsername());
            userSave.setPassword(userModel.getPassword() != null ? userModel.getPassword() : userSave.getPassword());
            userRepository.save(userSave);
            return new ResponseEntity<>(userSave, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Data not found with id" +id, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id")String id){

        try{
            userRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted with Id"+ id, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }



    /*@PostMapping("/subs")
    private ResponseEntity<?> subscribeClient(@RequestBody AuthenticationRequest authenticationRequest){

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setPassword(password);
        try{

            userRepository.save(userModel);

        }catch(Exception e){

            return ResponseEntity.ok(new AuthenticateResponse("Error during client registration" + username));

        }


        return ResponseEntity.ok(new AuthenticateResponse("Successful subscription for client" + username));

    }*/


    //@PostMapping("/auth")
    //private ResponseEntity<?> authenticateClient(@RequestBody AuthenticationRequest authenticationRequest){

    //}



}
