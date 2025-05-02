
package swguan.middleware.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import swguan.middleware.support.ResponseDTO;
import swguan.middleware.support.exception.FlowException;
import swguan.middleware.support.header.HeaderContext;
import swguan.middleware.user.dto.UserAuthRequestDTO;
import swguan.middleware.user.dto.UserLoginResponseDTO;
import swguan.middleware.user.dto.UserRequestDTO;
import swguan.middleware.user.dto.UserResponseDTO;
import swguan.middleware.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/v1/user/register")
	public ResponseDTO<?> insertUser(@Valid @RequestBody UserRequestDTO reqDto) throws FlowException {
		userService.insertUser(reqDto);
		return ResponseDTO.success();
	}

	@PostMapping("/v1/user/login")
	public ResponseDTO<UserResponseDTO> selectUser(@Valid @RequestBody UserRequestDTO reqDto) throws Exception {
		return ResponseDTO.success(userService.selectUser(reqDto));
	}

	@PostMapping("/v1/user/auth")
	public ResponseDTO<?> authUser(@Valid @RequestBody UserAuthRequestDTO reqDto) throws FlowException {
		userService.authUser(HeaderContext.getHeaderTokenDTO(), reqDto);
		return ResponseDTO.success();
	}

	@GetMapping("/v1/user/login/record")
	public ResponseDTO<UserLoginResponseDTO> getLastLogin() throws FlowException {
		return ResponseDTO.success(userService.getLastLogin(HeaderContext.getHeaderTokenDTO()));
	}

}
