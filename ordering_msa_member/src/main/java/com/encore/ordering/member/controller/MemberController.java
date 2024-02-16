package com.encore.ordering.member.controller;


import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginRequest;
import com.encore.ordering.member.dto.MemberResponse;
import com.encore.ordering.member.dto.MemberSaveRequest;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.securities.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService service;

    private final JwtTokenProvider jwtTokenProvider;
    public MemberController(MemberService service, JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody MemberSaveRequest request) {
        Member member = service.save(request);
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.CREATED,"member successfully created.",member.getId()),
                HttpStatus.CREATED
        );
    }


    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponse> signIn(@Valid @RequestBody LoginRequest request) {
        Member member =service.login(request);
        //json web token
        //토큰 생성 로직
        String jwt = jwtTokenProvider.createdToken(member.getEmail(), member.getRole().name());

        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("id", member.getId());
        memberInfo.put("token", jwt);
        return new ResponseEntity<>(
                new CommonResponse(HttpStatus.OK,"member successfully logind.",memberInfo),
                HttpStatus.OK
        );
    }


    @GetMapping("/members")
    public List<MemberResponse> members() {
        return service.findAll();
    }


    @GetMapping("/member/{id}")
    public MemberResponse findById (@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/member/myInfo")
    public MemberResponse findMyInfo(@RequestHeader("myEmail") String email) {
        return service.findMyInfo(email);
    }

    @GetMapping("/member/findByEmail/{email}")
    public MemberResponse findByEmail(@PathVariable("email") String email) {
        return service.findMyInfo(email);
    }

}
