package com.petBook.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class UserController {

    @GetMapping("/users")
    @ApiOperation(value = "유저 조회", notes = "유저 조회 API")
    public Object Users() {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("msg", "success");
        return result;
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "유저 상세조회", notes = "유저 상세조회 API")
    @ApiImplicitParam(name = "id", value = "유저 ID") // Swagger 에 사용하는 파라미터에 대한 설명
    public Object user_detail(@PathVariable String id) {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("id", id);

        return result;
    }

    @PutMapping("/user/{id}")
    @ApiOperation(value = "유저 수정", notes = "유저 수정 API")
    @ApiImplicitParam(name = "id", value = "유저 ID")
    public Object user_update(@PathVariable String id) {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("id", id);

        return result;
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "유저 삭제", notes = "유저 삭제 API")
    @ApiImplicitParam(name = "id", value = "유저 ID")
    public Object user_delete(@PathVariable String id) {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("id", id);

        return result;
    }
}
